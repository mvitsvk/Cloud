package su.mvitsvk.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.flywaydb.core.Flyway;
import su.mvitsvk.server.factory.Factory;


public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
        Configurator.setAllLevels(LogManager.getRootLogger().getName(), Factory.getConfig().getValueLog());

        LOGGER.trace("load Class com.mysql.jdbc.Driver");
        Flyway flyway = Flyway.configure().dataSource("jdbc:mariadb://localhost:3307/cloud", "cloud", "cloud").load();
        flyway.migrate();

        Factory.getNetworkService().startServer();
    }
}

