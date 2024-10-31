package com.cjrequena.eventstore.sample.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
  entityManagerFactoryRef = "entityManagerFactoryEventStore",
  transactionManagerRef = "transactionManagerEventStore",
  basePackages = {"com.cjrequena.eventstore.sample.repository"}
)
public class EventStoreDataSourceConfiguration {

  @Primary
  @Bean(name = "dataSourceEventStore", destroyMethod = "")
  @Validated
  @ConfigurationProperties(prefix = "spring.datasource.eventstore")
  @ConditionalOnClass({HikariDataSource.class})
  public HikariDataSource dataSourceEventStore() {
    return new HikariDataSource();
  }

  @Primary
  @Bean(name = "entityManagerFactoryEventStore")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryEventStore(
    EntityManagerFactoryBuilder builder, @Qualifier("dataSourceEventStore") DataSource dataSource) {
    return builder
      .dataSource(dataSource)
      .packages("com.cjrequena.eventstore.sample.entity")
      .persistenceUnit("eventstore")
      .build();
  }

  @Primary
  @Bean(name = "transactionManagerEventStore")
  public PlatformTransactionManager transactionManagerEventStore(
    @Qualifier("entityManagerFactoryEventStore") EntityManagerFactory entityManagerFactoryEventStore) {
    return new JpaTransactionManager(entityManagerFactoryEventStore);
  }
}
