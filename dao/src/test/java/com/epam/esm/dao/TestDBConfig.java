package com.epam.esm.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class TestDBConfig {

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .generateUniqueName(true)
                    .setType(EmbeddedDatabaseType.H2)
                    .setScriptEncoding("UTF-8")
                    .ignoreFailedDrops(true)
                    .addScript("h2_world.sql")
                    .build();
        }

        @Bean("testTemplate")
        public NamedParameterJdbcTemplate namedParamJdbcTemplate() {
            NamedParameterJdbcTemplate namedParamJdbcTemplate =
                    new NamedParameterJdbcTemplate(dataSource());
            return namedParamJdbcTemplate;
        }
    }
