package io.aryby.spring_boot_crud.generator;

public interface IRessourcesGenerator {

    default String generateRessources(){
        return "" +
            "server.port=9090\n"+
            "spring.datasource.url=${JDBC_DATABASE_URL\\:jdbc\\:mysql\\://localhost\\:3306/spring-generator?serverTimezone\\=UTC}\n" +
            "spring.datasource.username=${JDBC_DATABASE_USERNAME\\:root}\n" +
            "spring.datasource.password=${JDBC_DATABASE_PASSWORD\\:root}\n" +
            "spring.datasource.hikari.connection-timeout=30000\n" +
            "spring.datasource.hikari.maximum-pool-size=10\n" +
            "spring.jpa.hibernate.ddl-auto=create-drop\n" +
            "spring.jpa.open-in-view=false\n" +
            "spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true\n" +
            "spring.jpa.properties.hibernate.id.new_generator_mappings=true\n";
    };
}
