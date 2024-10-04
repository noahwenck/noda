package com.wenck.noda;

import org.mariadb.jdbc.MariaDbPoolDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class MariaDBConfig {

    @Bean
    public DataSource dataSource() throws SQLException {
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
        dataSource.setUser("user");
        dataSource.setPassword("mypass");
        dataSource.setUrl("jdbc:mariadb://localhost:3306/mariadb");
        return dataSource;
    }
}
