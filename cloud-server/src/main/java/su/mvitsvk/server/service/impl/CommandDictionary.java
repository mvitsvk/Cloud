package su.mvitsvk.server.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import su.mvitsvk.common.FileService;
import su.mvitsvk.common.NetworkObject;
import su.mvitsvk.server.factory.Factory;

import java.util.LinkedList;

public class CommandDictionary {
    private static final Logger LOGGER = LogManager.getLogger(CommandDictionary.class);
    private FileService fileWorker;
    private String username;

    public NetworkObject responceCMD (NetworkObject msg) {
        LOGGER.info("responceCMD");
        String command = msg.getCommand();
        String file = msg.getFile();

        if (command.equals("login")) {
            LOGGER.info("responceCMD login");
            username = Factory.getSqlService().AuthFind(file, msg.getOldFile());
            if (username != null) {
                msg.setFile(null);
                msg.setOldFile(null);
                msg.setCommand("cd");
//                String dir = msg.getDir();
                fileWorker = Factory.getFileService(username);
                msg.setListFileDir(fileWorker.getListFileDir());
            }
            return msg;
        }

        if (command.equals("copy")) {
            LOGGER.info("responceCMD copy");
            if (file != null) {
                if (fileWorker.writeFile(file, msg.getBytes())) {
                    msg.setPosition(msg.getPosition() + msg.getBytes().length);
                }
            } else if (msg.getListCopyDir().size() > 0) {
                fileWorker.createDir(msg.getListCopyDir());
                msg.setListCopyDir(null);
            }
            return msg;
        }

        if (command.equals("copyBACK")) {
            LOGGER.info("responceCMD copyBACK");
            if (fileWorker.isDir(file)) {
                fileWorker.scanCopy(file);
                msg.setListCopyFile((LinkedList<String>) fileWorker.getListCopyFile());
                msg.setListCopyDir((LinkedList<String>) fileWorker.getListCopyDir());
                byte[] t = new byte[0];
                msg.setBytes(t);
                msg.setFile(null);
                return msg;
            } else if (msg.getFile() != null) {
                msg.setBytes(fileWorker.readFile(file, msg.getPosition(), Factory.getBuffer()));
            }
                if (msg.getBytes().length != 0) {
                    return msg;
                } else if (msg.getListCopyFile() != null) {
            msg.setFile(msg.getListCopyFile().get(0));
            msg.setBytes(fileWorker.readFile(msg.getListCopyFile().get(0), 0L, Factory.getBuffer()));
            msg.setPosition(0L);
            msg.listRemoveFirst();
        } else {
            msg.setCommand("cdBACK");
            msg.setListFileDir(fileWorker.getListFileDir());
        }
            return msg;
        }

        if (command.equals("cd") ) {
            LOGGER.info("responceCMD cd");
            String dir = msg.getDir();
            if (dir != null)
                fileWorker.moveToDirs(dir);
            msg.setListFileDir(fileWorker.getListFileDir());
            return msg;
        }

        if (command.equals("rename") ) {
            LOGGER.info("responceCMD rename");
            String oldFile = msg.getOldFile();
            if (oldFile != null & file != null) {
                fileWorker.rename(oldFile, file);
                fileWorker.reloadFileDir();
                msg.setListFileDir(fileWorker.getListFileDir());
            }
            return msg;
        }

        if (command.equals("delete") ) {
            LOGGER.info("responceCMD delete");
            if (file != null) {
                fileWorker.delete(file);
                fileWorker.reloadFileDir();
                msg.setListFileDir(fileWorker.getListFileDir());
            }
            return msg;
        }

        return null;
    }

}
