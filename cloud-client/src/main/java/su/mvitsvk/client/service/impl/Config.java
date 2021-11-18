package su.mvitsvk.client.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    private Map<String, String> config;
    public Config (){
        Properties props = new Properties();
        config = new HashMap<>();
        Path filePath = Paths.get("cloud-client","config.ini");
        if (Files.exists(filePath)) {
            try {
                props.load(Files.newInputStream(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            props.forEach((key,value) -> config.put(key.toString(),value.toString()));
        }
    }

    public String getValue(String key) {
        return config.get(key);
    }

}
