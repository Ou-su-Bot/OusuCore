package me.skiincraft.sql.repository;

import me.skiincraft.sql.BasicSQL;
import me.skiincraft.sql.annotation.Id;
import me.skiincraft.sql.annotation.Ignore;
import me.skiincraft.sql.annotation.Table;
import me.skiincraft.sql.exceptions.RepositoryException;
import me.skiincraft.sql.platform.SQLPlatform;
import me.skiincraft.sql.reflection.ClassUtils;
import me.skiincraft.sql.util.SQLField;
import me.skiincraft.sql.util.SQLFieldType;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicRepository<T, ID> implements Repository<T, ID> {

    private String tableName;
    private Field idField;
    private ClassUtils<T> classUtils;
    private List<SQLField> fields;

    public BasicRepository(Class<?> item, Class<?> idClass) throws RepositoryException {
        mapClass(new ClassUtils<T>(item){});
        checkIdField();
        prepareTable();
    }

    private void mapClass(ClassUtils<T> classUtils) throws RepositoryException {
        this.classUtils = classUtils;
        this.idField = classUtils.getFieldWithAnnotation(Id.class);
        this.fields = SQLField.of(classUtils.getGenericClass().getDeclaredFields())
                .stream()
                .filter(field -> !field.getField().isAnnotationPresent(Ignore.class))
                .collect(Collectors.toList());
    }

    private void checkIdField() throws RepositoryException {
        if (Objects.isNull(idField)){
            throw new RepositoryException("Você não apontou o field: ID");
        }
    }

    private void prepareTable() throws RepositoryException {
        Table tableAnnotation = classUtils.containsAnnotation(Table.class);
        this.tableName = (tableAnnotation == null) ? classUtils.getGenericClass().getSimpleName() : tableAnnotation.value();
        createTable();
    }

    private void createTable() throws RepositoryException {
        List<String> str = new ArrayList<>();
        fields.forEach(sqlField -> {
            Field field = sqlField.getField();
            SQLFieldType type = sqlField.getType();
            if (!field.getName().equals(idField.getName()))
                str.add(String.format(" %s %s %s", field.getName(), type.getName(), ((type.equals(SQLFieldType.VARCHAR) ? "(255)" : ""))));
        });
        try {
            Statement statement = getStatement();
            statement.execute(String.format("CREATE TABLE IF NOT EXISTS %s (%s %s PRIMARY KEY,%s);",
                    tableName, idField.getName(), SQLFieldType.getByClass(idField.getType()).getName(), String.join(",", str)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RepositoryException("Não foi possivel criar esta tabela");
        }
        commit();
    }

    private SQLPlatform getSQL(){
        return BasicSQL.getSQL();
    }

    @Override
    public Optional<T> get(int index) {
        rangeCheck(index);
        try {
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM \"%s\"", tableName));
            int toIndex = 0;
            while (resultSet.next()) {
                if (toIndex == index) {
                    return Optional.of(createInstance(resultSet));
                }
                toIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        commit();
        return Optional.empty();
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    @Override
    @Nonnull
    public Optional<T> getById(ID id) {
        try {
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM \"%s\" WHERE %s = '%s'", tableName, idField.getName(), id));
            if (resultSet.next()) {
                return Optional.of(createInstance(resultSet));
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public T createInstance(ResultSet resultSet) {
        try {
            T newInstance = classUtils.getGenericClass().newInstance();
            for (SQLField field : fields) {
                field.getField().setAccessible(true);
                field.parseItem(newInstance, resultSet.getObject(field.getField().getName()));
            }
            return newInstance;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        List<T> all = new ArrayList<>();
        try {
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM \"%s\"", tableName));
            while (resultSet.next()) {
                all.add(createInstance(resultSet));
            }
            return all;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return all;
    }

    @Override
    public boolean contains(T item) {
        try {
            Statement statement = getStatement();
            Field field = item.getClass().getDeclaredField(idField.getName());
            field.setAccessible(true);
            ResultSet result = statement.executeQuery(String.format("SELECT * FROM %s WHERE \"%s\" = %s;", tableName, idField.getName(), field.get(item)));
            return result.next() && result.getString(idField.getName()) != null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public long size() {
        try {
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM \"%s\";", tableName));
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public void remove(int index) {
        rangeCheck(index);
        try {
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM \"%s\"", tableName));
            int toIndex = 0;
            while (resultSet.next()) {
                if (toIndex == index) {
                    statement.execute(String.format("DELETE FROM %s WHERE %s = %s", tableName, idField.getName(), resultSet.getObject(idField.getName())));
                    break;
                }
                toIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        commit();
    }

    @Override
    public void remove(T index) {
        if (contains(index)){
            try {
                Statement statement = getStatement();
                Field field = index.getClass().getDeclaredField(idField.getName());
                field.setAccessible(true);
                statement.execute(String.format("DELETE FROM %s WHERE %s = %s", tableName, idField.getName(), idField.get(index)));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(T item) {
        try {
            Statement statement = getStatement();
            idField.setAccessible(true);
            if (!contains(item)) {
                statement.execute(String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns(), values(item)));
                commit();
                return;
            }
            statement.execute(String.format("UPDATE %s SET %s WHERE %s = '%s'", tableName, updateSet(item), idField.getName(), idField.get(item)));
            commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String updateSet(T item){
        return fields.stream().map(sqlField -> String.format("%s = '%s'", sqlField.getField().getName(), sqlField.toSQLFormat(item))).collect(Collectors.joining(", "));
    }

    private String columns(){
        return fields.stream()
                .map(sqlField -> sqlField.getField().getName())
                .collect(Collectors.joining(", "));
    }

    private String values(T item){
        return fields.stream()
                .map(sqlField -> "'" + sqlField.toSQLFormat(item) + "'")
                .collect(Collectors.joining(", "));
    }

    private Statement getStatement() throws SQLException {
        return getSQL().createNewStatement(this);
    }

    private void commit(){
        try {
            getSQL().getConnection().commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
