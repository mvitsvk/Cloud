package su.mvitsvk.common;

import java.util.LinkedList;
import java.util.List;

public interface FileService {

    String getPathDir();
    boolean moveToDirs(String dir);
    void reloadFileDir();
    LinkedList<String> getListFileDir();
    void rename (String oldName, String newName);
    void delete (String name);
    boolean writeFile (String name, byte [] buffer);
    byte [] readFile (String name, Long position, Integer bufferLength);
    void scanCopy(String path);
    void scanDelete(String name);
    List<String> getListCopyDir();
    List<String> getListCopyFile();
    boolean isDir (String name);
    void createDir(LinkedList<String> listCopyDir);
    void createDir(String path);

}
