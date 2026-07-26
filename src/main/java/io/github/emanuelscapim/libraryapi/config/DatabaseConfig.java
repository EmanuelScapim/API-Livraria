package io.github.emanuelscapim.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    @Bean
    public DataSource hikariDataSource(){
        HikariConfig config = new HikariConfig();

        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        config.setMaximumPoolSize(10); //número máximo de conexões no banco de dados
        config.setMinimumIdle(1); //tamanho inicial do pool
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(600000); //600 mil ms (10 minutos)
        config.setConnectionTimeout(100000); //100 mil ms (1,66 minuntos)
        config.setConnectionTestQuery("select 1"); // query de teste


        return new HikariDataSource(config);
    }
}
