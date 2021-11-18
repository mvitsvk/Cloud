package su.mvitsvk.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import su.mvitsvk.client.factory.Factory;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/mainWindow.fxml"));
        Parent parent = loader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("Cloud Client");
        primaryStage.setResizable(true);

        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        Factory.getNetworkService().disconect();
    }
}
