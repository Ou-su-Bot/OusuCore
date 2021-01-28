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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.*;
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
        this.idField.setAccessible(true);
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
                    tableName, idField.getName(), SQLFieldType.getBy(idField.getType()).getName(), String.join(",", str)));
        } catch (SQLException throwables) {
            revert();
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
            revert();
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
            revert();
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public T createInstance(ResultSet resultSet) {
        try {
            T newInstance = classUtils.getGenericClass().newInstance();
            for (SQLField field : fields) {
                field.getField().setAccessible(true);
                if (field.isBlob()){
                    Blob blob = resultSet.getBlob(field.getField().getName());
                    if (blob == null){
                        continue;
                    }
                    if (field.getType() == SQLFieldType.TEXT){
                        field.parseItem(newInstance, new BufferedReader(new InputStreamReader(blob.getBinaryStream(), StandardCharsets.UTF_8))
                                .lines()
                                .collect(Collectors.joining("\n")));
                        continue;
                    }
                    field.parseItem(newInstance, blob.getBinaryStream());
                    continue;
                }
                field.parseItem(newInstance, resultSet.getObject(field.getField().getName()));
            }
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            revert();
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
            revert();
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
            ResultSet result = statement.executeQuery(String.format("SELECT * FROM %s WHERE %s = '%s';", tableName, idField.getName(), field.get(item)));
            return result.next() && result.getString(idField.getName()) != null;
        } catch (Exception e){
            revert();
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
            revert();
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
            revert();
            e.printStackTrace();
        }
        commit();
    }

    @Override
    public void removeObject(T index) {
        if (contains(index)){
            try {
                Statement statement = getStatement();
                Field field = index.getClass().getDeclaredField(idField.getName());
                field.setAccessible(true);
                statement.execute(String.format("DELETE FROM %s WHERE %s = '%s'", tableName, field.getName(), field.get(index)));
                commit();
            } catch (Exception e){
                revert();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(T item) {
        try {
            if (containsBlob()){
                savePrepare(item);
                return;
            }
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
            revert();
            e.printStackTrace();
        }
    }

    private void savePrepare(T item) throws Exception {
        idField.setAccessible(true);
        if (!contains(item)) {
            PreparedStatement statement = getPreparedStatement(String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns(), wildCardValues()));
            statementUpdate(statement, item);
            statement.executeUpdate();
            commit();
            return;
        }
        PreparedStatement statement = getPreparedStatement(String.format("UPDATE %s SET %s WHERE %s = '%s'", tableName, wildCardUpdate(), idField.getName(), idField.get(item)));
        statementUpdate(statement, item);
        statement.executeUpdate();
        commit();
    }
    private void statementUpdate(PreparedStatement statement, T item) throws SQLException {
        int i = 1;
        for (SQLField sql : fields){
            if (sql.isBlob()) {
                statement.setBlob(i, (sql.getField().getType() == String.class)
                        ? (sql.toSQLFormat(item) == null) ? null :new ByteArrayInputStream(sql.toSQLFormat(item).getBytes(StandardCharsets.UTF_8))
                        : (InputStream) sql.getItem(item));
                i++;
                continue;
            }
            statement.setObject(i, sql.toSQLFormat(item, false));
            i++;
        }
    }

    private boolean containsBlob(){
        return fields.stream().anyMatch(SQLField::isBlob);
    }

    private String updateSet(T item){
        return fields.stream().map(sqlField -> String.format("%s = %s", sqlField.getField().getName(), (sqlField.toSQLFormat(item) == null)? null : "'" + sqlField.toSQLFormat(item) + "'")).collect(Collectors.joining(", "));
    }

    private String columns(){
        return fields.stream()
                .map(sqlField -> sqlField.getField().getName())
                .collect(Collectors.joining(", "));
    }

    private String wildCardUpdate(){
        return fields.stream().map(sqlField -> String.format("%s = ?", sqlField.getField().getName())).collect(Collectors.joining(", "));
    }

    private String wildCardValues(){
        return fields.stream()
                .map(sqlField -> "?")
                .collect(Collectors.joining(", "));
    }

    private String values(T item){
        return fields.stream()
                .map(sqlField -> (sqlField.toSQLFormat(item) == null) ? null : "'" + sqlField.toSQLFormat(item) + "'")
                .collect(Collectors.joining(", "));
    }

    private Statement getStatement() throws SQLException {
        return getSQL().createNewStatement(this);
    }

    private PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getSQL().createNewPreparedStatement(sql, this);
    }

    public Class<T> getParameterType(){
        return classUtils.getGenericClass();
    }

    private void commit(){
        try {
            getSQL().getConnection().commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void revert(){
        try {
            getSQL().getConnection().rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
