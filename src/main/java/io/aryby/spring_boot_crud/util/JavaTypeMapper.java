package io.aryby.spring_boot_crud.util;

import java.util.HashMap;
import java.util.Map;

public class JavaTypeMapper {

    private static final Map<String, JavaType> typeMapping = new HashMap<>();

    static {
        typeMapping.put("BOOLEAN", new JavaType("boolean"));
        typeMapping.put("UUID", new JavaType("UUID", "java.util.UUID"));
        typeMapping.put("LIST", new JavaType("List<T>", "java.util.List"));
        typeMapping.put("MAP", new JavaType("Map<T, U>", "java.util.Map"));

        typeMapping.put("STRING", new JavaType("String"));
        typeMapping.put("TEXT", new JavaType("String")); // Treated as String
        typeMapping.put("INTEGER", new JavaType("Integer"));
        typeMapping.put("LONG", new JavaType("Long"));
        typeMapping.put("DOUBLE", new JavaType("Double"));
        typeMapping.put("NUMERIC", new JavaType("BigDecimal", "java.math.BigDecimal"));

        typeMapping.put("LOCAL_DATE", new JavaType("LocalDate", "java.time.LocalDate"));
        typeMapping.put("LOCAL_TIME", new JavaType("LocalTime", "java.time.LocalTime"));
        typeMapping.put("LOCAL_DATE_TIME", new JavaType("LocalDateTime", "java.time.LocalDateTime"));
        typeMapping.put("OFFSET_DATE_TIME", new JavaType("OffsetDateTime", "java.time.OffsetDateTime"));
    }

    public static JavaType getJavaType(String nameTypeModifier) {
        return typeMapping.getOrDefault(nameTypeModifier, new JavaType("Object"));
    }

    public static class JavaType {
        private final String simpleType;
        private final String fullQualifiedName;

        public JavaType(String simpleType) {
            this.simpleType = simpleType;
            this.fullQualifiedName = simpleType;
        }

        public JavaType(String simpleType, String fullQualifiedName) {
            this.simpleType = simpleType;
            this.fullQualifiedName = fullQualifiedName;
        }

        public String getSimpleType() {
            return simpleType;
        }

        public String getFullQualifiedName() {
            return fullQualifiedName;
        }

        @Override
        public String toString() {
            return fullQualifiedName;
        }
    }
}
