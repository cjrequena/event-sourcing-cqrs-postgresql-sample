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
  entityManagerFactoryRef = "entityManagerFactoryProjection",
  transactionManagerRef = "transactionManagerProjection",
  basePackages = {"com.cjrequena.sample.persistence.repository"}
)
public class ProjectionDBDataSourceConfiguration {

  @Bean(name = "dataSourceProjection", destroyMethod = "")
  @Validated
  @ConfigurationProperties(prefix = "spring.datasource.projectiondb")
  @ConditionalOnClass({HikariDataSource.class})
  public HikariDataSource dataSourceProjection() {
    return new HikariDataSource();
  }

  @Bean("entityManagerFactoryProjection")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryProjection(
    EntityManagerFactoryBuilder builder, @Qualifier("dataSourceProjection") DataSource dataSource) {
    return builder
      .dataSource(dataSource)
      .packages("com.cjrequena.sample.persistence.entity")
      .persistenceUnit("projectionDB")
      .build();
  }

  @Bean("transactionManagerProjection")
  public PlatformTransactionManager transactionManagerProjection(
    @Qualifier("entityManagerFactoryProjection") EntityManagerFactory entityManagerFactoryProjection) {
    return new JpaTransactionManager(entityManagerFactoryProjection);
  }
}
