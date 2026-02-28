package com.aml.sanctionsscreeningservice.sanctions.configuration;

import com.aml.sanctionsscreeningservice.sanctions.repository.SanctionedPersonRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = SanctionedPersonRepository.class,
entityManagerFactoryRef = "sanctionsEntityManagerFactory",
transactionManagerRef = "sanctionsTransactionManager")
public class SanctionsDatabaseConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.sanctions")
    public DataSource sanctionsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean sanctionsEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(sanctionsDataSource())
                .packages("com.aml.sanctionsscreeningservice.sanctions.entity")
                .persistenceUnit("sanctions")
                .build();
    }

    @Bean
    public PlatformTransactionManager sanctionsTransactionManager(
            @Qualifier("sanctionsEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }
}
