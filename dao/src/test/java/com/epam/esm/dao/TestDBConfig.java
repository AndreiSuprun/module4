package com.epam.esm.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.dao"})
@EnableTransactionManagement
@PropertySource("classpath:/database.properties")
public class TestDBConfig {

    @Value("${database.driver}") String driver;
    @Value("${database.name}") String name;
    @Value("${database.encoding}") String encoding;
    @Value("${database.script}") String script;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName(name)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding(encoding)
                .ignoreFailedDrops(true)
                .addScript(script)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
