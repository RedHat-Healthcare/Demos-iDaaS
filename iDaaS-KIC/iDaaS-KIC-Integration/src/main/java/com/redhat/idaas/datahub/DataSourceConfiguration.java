package com.redhat.idaas.datahub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@ConditionalOnProperty(
        value="idaas.storeInDb",
        havingValue = "true"
)
public class DataSourceConfiguration {

    @Autowired
    private ConfigProperties config;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(config.getDbDriverClassName());
        dataSourceBuilder.url(config.getDbUrl());
        dataSourceBuilder.username(config.getDbUsername());
        dataSourceBuilder.password(config.getDbPassword());

        DataSource dataSource = dataSourceBuilder.build();

        if (config.isCreateDbTable()) {
            String fields = String.join(" VARCHAR(256), ", AuditMessage.DB_PERSISTABLE_FIELDS) + " VARCHAR(256)";

            try (Statement statement = dataSource.getConnection().createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS " + config.getDbTableName() + "(" +
                        fields + ")");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create db table", e);
            }
        }

        return dataSource;
    }
}
