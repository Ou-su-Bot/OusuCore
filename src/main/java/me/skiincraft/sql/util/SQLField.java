package me.skiincraft.sql.util;

import me.skiincraft.sql.exceptions.RepositoryException;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLField {

    private final Field field;
    private final SQLFieldType type;

    public SQLField(Field field) throws RepositoryException {
        this.field = field;
        this.type = SQLFieldType.getByClass(field.getType());
        if (Objects.isNull(type)){
            throw new RepositoryException(String.format("O campo '%s' (%s) não é suportado! Veja os suportados em TableElementType.class", field.getName(), field.getType().getSimpleName()));
        }
    }

    public Field getField() {
        return field;
    }

    public SQLFieldType getType() {
        return type;
    }

    public void parseItem(Object item, Object value) {
        if (Objects.isNull(value))
            return;
        try {
            switch (type) {
                case DATE:
                    field.set(item, LocalDate.from(DateTimeFormatter.ISO_DATE.parse(value.toString())));
                    break;
                case TIMESTAMP:
                    field.set(item, Timestamp.valueOf(value.toString()).toLocalDateTime());
                    break;
                case TIMESTAMPZ:
                    field.set(item, Timestamp.valueOf(value.toString()).toLocalDateTime().atOffset(ZoneOffset.UTC));
                    break;
                case CHAR:
                    field.set(item, value.toString().charAt(0));
                    break;
                case INT:
                    if (field.getType().isEnum()){
                        field.set(item, field.getType().getEnumConstants()[Integer.parseInt(value.toString())]);
                        break;
                    }
                    field.set(item, value);
                    break;
                case SMALLINT:
                    field.set(item, new Short(value.toString()));
                    break;
                default:
                    field.set(item, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

        public String toSQLFormat(Object reference) {
        try {
            field.setAccessible(true);
            switch (type) {
                case DATE:
                    LocalDate date = (LocalDate) field.get(reference);
                    return DateTimeFormatter.ISO_DATE.format(date);
                case VARCHAR:
                    String varchar = String.valueOf(field.get(reference));
                    if (varchar.contains("'")){
                        return String.join("''", varchar.split("'"));
                    }
                    return varchar;
                case TEXT:
                    return "%s%s%s";
                case TIMESTAMP:
                    LocalDateTime timeStamp = (LocalDateTime) field.get(reference);
                    return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(timeStamp);
                case TIMESTAMPZ:
                    OffsetDateTime timeStampz = (OffsetDateTime) field.get(reference);
                    return Timestamp.valueOf(timeStampz.toLocalDateTime()).toString();
                case INT:
                    return field.getType().isEnum() ? String.valueOf(((Enum<?>) field.get(reference)).ordinal())
                            : String.valueOf(field.get(reference));
                default:
                    return String.valueOf(field.get(reference));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<SQLField> of(Field... fields) throws RepositoryException {
        List<SQLField> fieldList = new ArrayList<>();
        for (Field field : fields){
            fieldList.add(new SQLField(field));
        }
        return fieldList;
    }
}
