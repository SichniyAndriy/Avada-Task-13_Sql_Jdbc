package app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

public class ConnectionProvider {
    private static final DataSource DATA_SOURCE;

    static {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("src/main/resources/db_config.properties")) {
            properties.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException("Не знайдено файла конфігурації", e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("db_url"));
        config.setUsername(properties.getProperty("db_user"));
        config.setPassword(properties.getProperty("db_password"));
        config.setDriverClassName(properties.getProperty("db_driver"));
        config.setMaximumPoolSize(32);
        DATA_SOURCE = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
