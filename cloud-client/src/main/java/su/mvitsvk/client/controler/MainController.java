package su.mvitsvk.client.controler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import su.mvitsvk.client.factory.Factory;
import su.mvitsvk.common.NetworkObject;

import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    //1-fxlocal
    //2-fxremote
    private byte positionTab;

    private Stage stageCopy;
    private Label labelCopy;
    private VBox layoutCopy;
    private Scene sceneCopy;

    @FXML
    public ListView<String> fxlocal;
    public ListView<String> fxremote;
    public Button fxrename;
    public Button fxcondiscon;
    public Button fxdelete;
    public Button fxmove;
    public Button fxcopy;

    private ObservableList<String> fxLocalList = FXCollections.observableArrayList();
    private ObservableList<String> fxRemoteList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fxLocalList.addAll(Factory.getFileService().getListFileDir());
        fxlocal.setItems(fxLocalList);
        fxremote.setItems(fxRemoteList);

        createDialogCopy();

        Factory.setMainController(this);
    }

    public void fxLocalEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName() == "Enter"){
            if (Factory.getFileService().moveToDirs(fxlocal.getSelectionModel().getSelectedItem())) {
                fxLocalList.clear();
                fxLocalList.addAll(Factory.getFileService().getListFileDir());
            }
        }
    }

    public void fxRemoteEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName() == "Enter"){
            NetworkObject out = new NetworkObject();
            out.setCommand("cd");
            out.setDir(fxremote.getSelectionModel().getSelectedItem());
            Factory.getNetworkService().sendCMD(out);
        }
    }

    public void fxCopyButton(ActionEvent actionEvent) {
        processF5();
    }

    public void fxMoveButton(ActionEvent actionEvent) {
        processF6();
    }

    public void fxRenameButton(ActionEvent actionEvent) {
        processF7();
    }

    public void fxDeleteButton(ActionEvent actionEvent) {
        processF8();
    }

    public void fxConDiscon(ActionEvent actionEvent) {
        conDiscon();
    }

    public void fxKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName() == "F5") processF5();
        if (keyEvent.getCode().getName() == "F6") processF6();
        if (keyEvent.getCode().getName() == "F7") processF7();
        if (keyEvent.getCode().getName() == "F8") processF8();
        if (keyEvent.getCode().getName() == "F10") conDiscon();
        if (keyEvent.getCode().getName() == "Tab") {
            if (positionTab == 1) {
                positionTab = 2;
                fxremote.requestFocus();
            } else {
                positionTab = 1;
                fxlocal.requestFocus();
            }
        }
        if (keyEvent.getCode().getName() == "Left") {
            fxlocal.requestFocus();
            positionTab = 1;
        }
        if (keyEvent.getCode().getName() == "Right") {
            fxremote.requestFocus();
            positionTab = 2;
        }
    }

    public void fxLocalMousePressed(MouseEvent mouseEvent) {
        positionTab = 1;
    }

    public void fxRemoteMousePressed(MouseEvent mouseEvent) {
        positionTab = 2;
    }

    private void conDiscon(){
        if (Factory.getNetworkService().getIsrun() == false) {
            Factory.getNetworkService().connect();
            fxcondiscon.setText("DISCONECT  F10");
        } else {
            Factory.getNetworkService().disconect();
            fxRemoteList.clear();
            fxcondiscon.setText("CONNECT F10");
        }
    }
    private void conDiscon(boolean off){
        if (off == false) {
            Factory.getNetworkService().connect();
            fxcondiscon.setText("DISCONECT  F10");
        } else {
            Factory.getNetworkService().disconect();
            fxRemoteList.clear();
            fxcondiscon.setText("CONNECT F10");
        }
    }


    private void processF5 (){
        String nameFile;
        if ((fxlocal.isFocused()) | (positionTab == 1)) {
            nameFile = fxlocal.getSelectionModel().getSelectedItem();
            NetworkObject out = new NetworkObject();
            out.setCommand("copy");
            if (Factory.getFileService().isDir(nameFile)) {
                Factory.getFileService().scanCopy(nameFile);
                out.setListCopyFile((LinkedList<String>) Factory.getFileService().getListCopyFile());
                out.setListCopyDir((LinkedList<String>) Factory.getFileService().getListCopyDir());
                byte[] t = new byte[0];
                out.setBytes(t);
            } else {
                out.setFile(nameFile);
                out.setBytes(Factory.getFileService().readFile(nameFile, 0L, Factory.getBuffer()));
                out.setPosition(0L);
            }
            Factory.getNetworkService().sendCMD(out);
        }
        if ((fxremote.isFocused()) | (positionTab == 2)) {
            nameFile = fxremote.getSelectionModel().getSelectedItem();
            NetworkObject out = new NetworkObject();
            out.setCommand("copyBACK");
            out.setFile(nameFile);
            Factory.getNetworkService().sendCMD(out);
        }
    }

    private void processF6 (){
            processF5();
            processF8();
        }

    private void processF7 () {
        String nameFile;
        String newNameFile;
        if ((fxlocal.isFocused()) | (positionTab == 1)) {
            nameFile = fxlocal.getSelectionModel().getSelectedItem();
            newNameFile = renameDialog(nameFile);
            if ((newNameFile != null) & (!newNameFile.equals(nameFile))) {
                Factory.getFileService().rename(nameFile, newNameFile);
                Factory.getFileService().reloadFileDir();
                fxLocalList.clear();
                fxLocalList.addAll(Factory.getFileService().getListFileDir());
            }
        }
            if ((fxremote.isFocused()) | (positionTab == 2)) {
                nameFile = fxremote.getSelectionModel().getSelectedItem();
                newNameFile = renameDialog(nameFile);
                if ((newNameFile != null) & (!newNameFile.equals(nameFile))) {
                    NetworkObject out = new NetworkObject();
                    out.setCommand("rename");
                    out.setOldFile(nameFile);
                    out.setFile(newNameFile);
                    Factory.getNetworkService().sendCMD(out);
                }
            }
    }

        private void processF8 () {
            String nameFile;
            if ((fxlocal.isFocused()) | (positionTab == 1)) {
                nameFile = fxlocal.getSelectionModel().getSelectedItem();
                if (deleteDialog(nameFile) == true) {
                    Factory.getFileService().delete(nameFile);
                    Factory.getFileService().reloadFileDir();
                    fxLocalList.clear();
                    fxLocalList.addAll(Factory.getFileService().getListFileDir());
                }
            }
            if ((fxremote.isFocused()) | (positionTab == 2)) {
                nameFile = fxremote.getSelectionModel().getSelectedItem();
                if (deleteDialog(nameFile) == true) {
                    NetworkObject out = new NetworkObject();
                    out.setCommand("delete");
                    out.setFile(nameFile);
                    Factory.getNetworkService().sendCMD(out);
                }
            }
        }

    private String renameDialog (String str){
        TextInputDialog dialog = new TextInputDialog(str);
        dialog.setTitle("Rename dialog box");
        dialog.setHeaderText("Rename file or DIR to:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) return result.get();
        return null;
    }

    public void loginDialog (){

        Platform.runLater(new Runnable() {
            @Override
            public void run() { // start runable

                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Login Dialog");
                ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));
                TextField username = new TextField();
                username.setPromptText("Username");
                PasswordField password = new PasswordField();
                password.setPromptText("Password");

                grid.add(new Label("Username:"), 0, 0);
                grid.add(username, 1, 0);
                grid.add(new Label("Password:"), 0, 1);
                grid.add(password, 1, 1);

                Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
                loginButton.setDisable(true);

                username.textProperty().addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(newValue.trim().isEmpty());
                });

                dialog.getDialogPane().setContent(grid);

                Platform.runLater(() -> username.requestFocus());
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType) {
                        return new Pair<>(username.getText(), password.getText());
                    }
                    conDiscon(true);
                    return null;
                });
                Optional<Pair<String, String>> result = dialog.showAndWait();
                result.ifPresent(usernamePassword -> {
                    NetworkObject out = new NetworkObject();
                    out.setCommand("login");
                    out.setFile(usernamePassword.getKey());
                    out.setOldFile(usernamePassword.getValue());
                    Factory.getNetworkService().sendCMD(out);
                });


            }//end runable
        });


    }

    private boolean deleteDialog (String str){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete File");
        alert.setHeaderText("Are you sure want to move this file?");
        alert.setContentText(str);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) return true;
        return false;
    }

    public void createDialogCopy(){
        stageCopy = new Stage();
        stageCopy.initModality(Modality.APPLICATION_MODAL);
        stageCopy.setTitle(" Copy / Move ");
        labelCopy = new Label("");
        layoutCopy = new VBox(labelCopy);
        layoutCopy.setAlignment(Pos.CENTER);
        sceneCopy = new Scene(layoutCopy, 200, 100);
        stageCopy.setScene(sceneCopy);
    }

    public void DialogCopy(boolean visible, String text) {
        labelCopy.setText(text);
        if (visible == true) {
            stageCopy.show();
        } else stageCopy.hide();
    }

    public void updateGraphic(Byte type, NetworkObject networkObject) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() { // start runable
                if (type == 1) {
                    fxRemoteList.clear();
                    fxRemoteList.addAll(networkObject.getListFileDir());
                }
                if (type == 2) {
                    if (networkObject.getListCopyFile() != null)
                        DialogCopy(true, "Copy later " + networkObject.getListCopyFile().size() + " files");
                }
                if (type == 3) {
                    DialogCopy(false, "Copy complite");
                }
                if (type == 4) {
                    fxLocalList.clear();
                    fxLocalList.addAll(Factory.getFileService().getListFileDir());
                }

            }//end runable
        });
    }



}
