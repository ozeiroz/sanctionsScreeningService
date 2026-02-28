package com.aml.sanctionsscreeningservice.screening.configuration;

import com.aml.sanctionsscreeningservice.screening.repository.ScreeningRepository;
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
@EnableJpaRepositories(basePackageClasses = ScreeningRepository.class,
        entityManagerFactoryRef = "screeningEntityManagerFactory",
        transactionManagerRef = "screeningTransactionManager")
public class ScreeningDatabaseConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.screening")
    public DataSource screeningDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean screeningEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(screeningDataSource())
                .packages("com.aml.sanctionsscreeningservice.screening.entity")
                .persistenceUnit("screening")
                .build();
    }

    @Bean
    public PlatformTransactionManager screeningTransactionManager(
            @Qualifier("screeningEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }
}
