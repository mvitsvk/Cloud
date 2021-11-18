package su.mvitsvk.common;

import java.io.Serializable;
import java.util.LinkedList;

public class NetworkObject implements Serializable {
    private String file;
    private String oldFile;
    private Long position;
    private byte[] bytes;
    private String command;
    private String dir;
    private LinkedList<String> listFileDir;
    private LinkedList<String> listCopyFile;
    private LinkedList<String> listCopyDir;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getOldFile() {
        return oldFile;
    }

    public void setOldFile(String oldFile) {
        this.oldFile = oldFile;
    }

    public Long getPosition() {
        if (position == null ) position = 0L;
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public LinkedList<String> getListFileDir() {
        return listFileDir;
    }

    public void setListFileDir(LinkedList<String> listFileDir) {
        this.listFileDir = listFileDir;
    }

    public LinkedList<String> getListCopyFile() {
        return listCopyFile;
    }

    public void setListCopyFile(LinkedList<String> listCopyFile) {
        this.listCopyFile = listCopyFile;
    }

    public LinkedList<String> getListCopyDir() {
        return listCopyDir;
    }

    public void setListCopyDir(LinkedList<String> listCopyDir) {
        this.listCopyDir = listCopyDir;
    }

    public void listRemoveFirst() {
        this.setFile(this.getListCopyFile().get(0));
        LinkedList<String> temp = this.getListCopyFile();
        temp.remove(0);
        if (temp.size() > 0) {
            this.setListCopyFile(temp);
        } else this.setListCopyFile(null);
    }
}
