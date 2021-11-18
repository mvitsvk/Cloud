package su.mvitsvk.client.service.impl;

import su.mvitsvk.client.factory.Factory;
import su.mvitsvk.common.NetworkObject;

public class CommandDictionary {

    public void responceCMD (NetworkObject networkObject) {

        if (networkObject.getCommand().equals("login")) {
            Factory.getMainController().loginDialog();
        }


                if (networkObject.getCommand().equals("cd") | networkObject.getCommand().equals("rename") | networkObject.getCommand().equals("delete")) {
                    Factory.getMainController().updateGraphic((byte) 1, networkObject);
                }

                if (networkObject.getCommand().equals("cdBACK")) {
                    Factory.getMainController().updateGraphic((byte) 3, networkObject);
                    Factory.getFileService().reloadFileDir();
                    Factory.getMainController().updateGraphic((byte) 4, null);
                }

                if (networkObject.getCommand().equals("copy")) {
                    Factory.getMainController().updateGraphic((byte) 2, networkObject);

                    if (networkObject.getFile() != null) {
                        networkObject.setBytes(Factory.getFileService().readFile(networkObject.getFile(), networkObject.getPosition(), Factory.getBuffer()));
                    }
                    if (networkObject.getBytes().length != 0) {
                        Factory.getNetworkService().sendCMD(networkObject);
                    } else if (networkObject.getListCopyFile() != null) {
                        networkObject.setFile(networkObject.getListCopyFile().get(0));
                        networkObject.setBytes(Factory.getFileService().readFile(networkObject.getListCopyFile().get(0), 0L, Factory.getBuffer()));
                        networkObject.setPosition(0L);
                        networkObject.listRemoveFirst();
                        Factory.getNetworkService().sendCMD(networkObject);
                    } else {
                        Factory.getMainController().updateGraphic((byte) 3, networkObject);
                        networkObject.setCommand("cd");
                        Factory.getNetworkService().sendCMD(networkObject);
                    }
                }

        if (networkObject.getCommand().equals("copyBACK")) {
            Factory.getMainController().updateGraphic((byte) 2, networkObject);
            String file = networkObject.getFile();

            if (file != null) {
                if (Factory.getFileService().writeFile(file, networkObject.getBytes())) {
                    networkObject.setPosition(networkObject.getPosition() + networkObject.getBytes().length);
                }
            } else if (networkObject.getListCopyDir().size() > 0) {
                Factory.getFileService().createDir(networkObject.getListCopyDir());
                networkObject.setListCopyDir(null);
                networkObject.setFile(networkObject.getListCopyFile().get(0));
                networkObject.setPosition(0L);
                networkObject.listRemoveFirst();
            }
            Factory.getNetworkService().sendCMD(networkObject);
    }
}

}
