package com.cjrequena.sample.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "entityManagerFactoryProjectionDB",
  transactionManagerRef = "transactionManagerProjectionDB",
  basePackages = {"com.cjrequena.sample.persistence.repository.postgresql"}
)
public class ProjectionDBDataSourceConfiguration {

  @Bean(name = "dataSourceProjectionDB", destroyMethod = "")
  @Validated
  @ConfigurationProperties(prefix = "spring.datasource.projectiondb")
  @ConditionalOnClass({HikariDataSource.class})
  public HikariDataSource dataSourceProjectionDB() {
    return new HikariDataSource();
  }

  @Bean(name = "entityManagerFactoryProjectionDB")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryProjectionDB(
    EntityManagerFactoryBuilder builder, @Qualifier("dataSourceProjectionDB") DataSource dataSource) {
    return builder
      .dataSource(dataSource)
      .packages("com.cjrequena.sample.persistence.entity.postgresql")
      .persistenceUnit("projectionDB")
      .build();
  }

  @Bean("transactionManagerProjectionDB")
  public PlatformTransactionManager transactionManagerProjectionDB(
    @Qualifier("entityManagerFactoryProjectionDB") EntityManagerFactory entityManagerFactoryProjectionDB) {
    return new JpaTransactionManager(entityManagerFactoryProjectionDB);
  }
}
