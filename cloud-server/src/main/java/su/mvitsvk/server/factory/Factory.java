package su.mvitsvk.server.factory;

import su.mvitsvk.common.FileService;
import su.mvitsvk.server.service.Sql;
import su.mvitsvk.server.service.impl.CommandDictionary;
import su.mvitsvk.server.service.impl.Config;
import su.mvitsvk.common.ImplementFileService;
import su.mvitsvk.server.service.impl.NettyServerService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import su.mvitsvk.server.service.impl.SqlMariadb;


public class Factory {
    private static final Logger LOGGER = LogManager.getLogger(Factory.class);

    private static Config config;
    private static FileService fileService;
    private static CommandDictionary commandDictionary;
    private static Sql sqlService;

    public static NettyServerService getNetworkService(){
        return new NettyServerService();
    }

    public static Sql getSqlService(){
        LOGGER.debug("getSqlService");
        if (sqlService == null)
            sqlService = new SqlMariadb();
        return sqlService;
    }


    public static FileService getFileService(String username){
        LOGGER.debug("getFileService");
        if (fileService == null)
            fileService = new ImplementFileService(getConfig().getValue("ROOT"),username);
        return fileService;
    }

    public static CommandDictionary getCommandDictionary(){
        LOGGER.debug("getCommandDictionary");
        if (commandDictionary == null)
            commandDictionary = new CommandDictionary();
        return commandDictionary;
    }

    public static Config getConfig()
    {
        LOGGER.debug("getConfig");
        if (config == null)
            config = new Config();
        return config;
    }

    public static Integer getBuffer (){
        LOGGER.debug("getBuffer");
        return Integer.parseInt(Factory.getConfig().getValue("FILEBUFFER"));
    }

}
