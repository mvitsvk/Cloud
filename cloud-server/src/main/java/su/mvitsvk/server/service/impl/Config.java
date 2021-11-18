package su.mvitsvk.server.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static Map<String, String> config;
    private static final Logger LOGGER = LogManager.getLogger(Config.class);
    public Config(){
        LOGGER.debug("Load Config");
        Properties props = new Properties();
        config = new HashMap<>();
        Path filePath = Paths.get("cloud-server","config.ini");
        if (Files.exists(filePath)) {
            try {
                props.load(Files.newInputStream(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            props.forEach((key,value) -> config.put(key.toString(),value.toString()));
        }
    }

    public String getValue(String key)
    {
        String res = config.get(key);
        LOGGER.debug("Config param " + key + " = " + res);
        return res;
    }

    public Level getValueLog()
    {
        LOGGER.info("select log level");
        if (config.get("LOG").equals("FATAL")) return Level.FATAL;
        if (config.get("LOG").equals("ERROR")) return Level.ERROR;
        if (config.get("LOG").equals("WARN")) return Level.WARN;
        if (config.get("LOG").equals("INFO")) return Level.INFO;
        if (config.get("LOG").equals("DEBUG")) return Level.DEBUG;
        if (config.get("LOG").equals("TRACE")) return Level.TRACE;
        return Level.OFF;
    }
}
