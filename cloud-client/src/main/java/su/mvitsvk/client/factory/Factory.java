package su.mvitsvk.client.factory;

import su.mvitsvk.client.controler.MainController;
import su.mvitsvk.client.service.NetworkService;
import su.mvitsvk.client.service.impl.CommandDictionary;
import su.mvitsvk.client.service.impl.Config;
import su.mvitsvk.client.service.impl.ImplementNetworkService;
import su.mvitsvk.common.FileService;
import su.mvitsvk.common.ImplementFileService;

public class Factory {
    private static MainController mainController;
    private static CommandDictionary commandDictionary;
    private static NetworkService networkService;
    private static FileService fileService;
    private static Config config;

    public static FileService getFileService(){
        if (fileService == null)
            fileService = new ImplementFileService();
        return fileService;
    }

    public static Config getConfig()
    {
        if (config == null)
            config = new Config();
        return config;
    }

    public static NetworkService getNetworkService(){
        if (networkService == null)
            networkService = new ImplementNetworkService();
        return networkService;
        }

    public static MainController getMainController() { return mainController; }

    public static void setMainController(MainController controller) { mainController = controller; }

    public static CommandDictionary getCommandDictionary(){
        if (commandDictionary == null)
                commandDictionary = new CommandDictionary();
        return commandDictionary;
    }

    public static Integer getBuffer (){
        return Integer.parseInt(Factory.getConfig().getValue("FILEBUFFER"));
    }




}

