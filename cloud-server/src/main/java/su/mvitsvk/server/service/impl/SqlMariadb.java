package su.mvitsvk.server.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import su.mvitsvk.server.service.Sql;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlMariadb implements Sql {
    private static final Logger LOGGER = LogManager.getLogger(SqlMariadb.class);
    Connection connSql;

    public SqlMariadb() {
        try {
            LOGGER.trace("load Class com.mysql.jdbc.Driver");
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException throwables) {
            LOGGER.throwing(Level.ERROR, throwables);
        }
    }

    @Override
    public String AuthFind( String login, String pass) {
        try {
            LOGGER.debug("AuthFind");
            connSql = DriverManager.getConnection("jdbc:mariadb://localhost:3307/cloud", "cloud", "cloud");
            PreparedStatement querySqlAuthFind = connSql.prepareStatement("SELECT * FROM PERSON WHERE NAME = ? AND PASSWORD = ? LIMIT 1");
            LOGGER.trace(querySqlAuthFind);
            querySqlAuthFind.setString(1, login);
            LOGGER.trace(querySqlAuthFind);
            querySqlAuthFind.setString(2, pass);
            LOGGER.trace(querySqlAuthFind);
            ResultSet resultSql = querySqlAuthFind.executeQuery();
            resultSql.next();
            LOGGER.trace(resultSql.getStatement());
            LOGGER.debug("Find " + resultSql.getString(2));
            return resultSql.getString(2);

        } catch (SQLException throwables) {
            LOGGER.throwing(Level.ERROR, throwables);
        }
        finally {
            try {
                connSql.close();
                LOGGER.info("SQL close");
            } catch (SQLException throwables) {
                LOGGER.throwing(Level.ERROR, throwables);
            }
        }
        return null;
    }

}

