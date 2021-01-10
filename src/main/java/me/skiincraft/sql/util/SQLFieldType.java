package me.skiincraft.sql.util;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;

public enum SQLFieldType {

    CHAR      ("CHAR",     char.class  , Character.class),
    VARCHAR   ("VARCHAR",                   String.class),
    TEXT      ("TEXT",                      String.class),
    BOOLEAN   ("BOOLEAN", boolean.class,   Boolean.class),
    SMALLINT  ("SMALLINT",  short.class,     Short.class),
    INT       ("INT",       int.class,    Integer.class),
    BIGINT    ("BIGINT",    long.class,     Long.class),
    DOUBLE    ("FLOAT",     double.class,   Double.class),
    FLOAT     ("REAL",     float.class,       Float.class),
    DATE      ("DATE",                     LocalDate.class),
    TIMESTAMP ("TIMESTAMP",                LocalDateTime.class),
    TIMESTAMPZ("TIMESTAMP WITH TIME ZONE", OffsetDateTime.class);

    private String name;
    private final Class<?>[] classTypes;

    SQLFieldType(String name, Class<?>... classTypes) {
        this.name = name;
        this.classTypes = classTypes;
    }

    public Class<?>[] getClassTypes() {
        return classTypes;
    }

    public Class<?> getClassType() {
        return classTypes[0];
    }

    public static SQLFieldType getByClass(Class<?> clazz) {
        if (clazz.isEnum()){
            return INT;
        }
        return Arrays.stream(values())
                .filter(elementType -> {
                    for (Class<?> classType : elementType.getClassTypes()) {
                        if (classType == clazz || classType == clazz.getSuperclass()) {
                            return true;
                        }
                    }
                    return false;
                }).findFirst().orElse(null);
    }

    public String getName() {
        return name;
    }

    public static SQLFieldType getByType(Type clazz) {
        if (((Class<?>) clazz).isEnum()){
            return INT;
        }
        return Arrays.stream(values())
                .filter(elementType -> {
                    for (Class<?> classType : elementType.getClassTypes()) {
                        if (classType == clazz) {
                            return true;
                        }
                    }
                    return false;
                }).findFirst().orElse(null);
    }
}