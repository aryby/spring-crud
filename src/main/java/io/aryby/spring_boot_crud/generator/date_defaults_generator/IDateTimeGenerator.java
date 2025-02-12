package io.aryby.spring_boot_crud.generator.date_defaults_generator;




public interface IDateTimeGenerator {

    default StringBuilder generateNowByDefault(String PAKAGE_MAIN){

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(PAKAGE_MAIN)
            .append(".utils")
            .append(";\n")
            .append("""

                import java.time.OffsetDateTime;
                import java.util.Optional;
                import org.springframework.boot.autoconfigure.domain.EntityScan;
                import org.springframework.context.annotation.Bean;
                import org.springframework.context.annotation.Configuration;
                import org.springframework.data.auditing.DateTimeProvider;
                import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
                import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
                import org.springframework.transaction.annotation.EnableTransactionManagement;



                """);

                builder.append("@EntityScan(\"").append(PAKAGE_MAIN).append("\")\n");
                builder.append("@EnableJpaRepositories(\"").append(PAKAGE_MAIN).append("\")\n");
                builder.append("""
                @Configuration
                @EnableTransactionManagement
                @EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
                public class DateTimeDefaultsConfig {

                    @Bean(name = "auditingDateTimeProvider")
                    public DateTimeProvider dateTimeProvider() {
                        return () -> Optional.of(OffsetDateTime.now());
                    }

                }




                """);

        return builder;
    }
}
