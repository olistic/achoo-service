package uy.achoo.database;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public final class DBConnector {
    private static final Logger log = LoggerFactory.getLogger(DBConnector.class);

    private static final String URL = "jdbc:mysql://localhost:3306/achoo_dev";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static final DBConnector INSTANCE = new DBConnector();

    private DBConnector() {
    }

    public static synchronized DBConnector getInstance() {
        return INSTANCE;
    }

    public Connection createConnection() {
        return doConnection();
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();

            log.debug("Connection closed");
        } catch (SQLException e) {
            log.error("Error while closing the connection: {}", e);
        }
    }

    private Connection doConnection() {
        Connection conn;

        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            log.error("Error while creating the connection: {}", e);
            throw new RuntimeException(e);
        }

        log.debug("Connection created: {}", conn);

        return conn;
    }

    public Configuration getConfiguration(Connection connection) {
        return new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
    }

    public DSLContext getContext(Connection connection) {
        return DSL.using(getConfiguration(connection));
    }
}
