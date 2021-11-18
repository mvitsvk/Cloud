package su.mvitsvk.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class ImplementFileService implements FileService {
    private static final Logger LOGGER = LogManager.getLogger(ImplementFileService.class);
    private Path currentPath;
    private Path userHomePath;
    private List<Path> listFileDir;
    private LinkedList<String> listCopyDir = new LinkedList<>();
    private LinkedList<String> listCopyFile = new LinkedList<>();


    public ImplementFileService() {
        LOGGER.debug("Start file service not param");
        //расположение при старте
        currentPath = Paths.get("").toAbsolutePath();
        reloadFileDir();
    }

    public ImplementFileService(String rootDir, String username) {
        LOGGER.debug("Start file servece with param");
        //расположение при старте
        currentPath = Paths.get("").toAbsolutePath();
        currentPath = Paths.get(currentPath.normalize().toString(), rootDir, username);
        userHomePath = currentPath;
        if (!isDir(currentPath.toString()))
            createDir(currentPath.toString());
        reloadFileDir();
    }

    @Override
    public String getPathDir() {
        LOGGER.debug("getPathDir");
        return currentPath.toAbsolutePath().toString();
    }

    @Override
    public boolean moveToDirs(String dir) {
        LOGGER.debug("moveToDirs");
        if ((dir.equals("..")) & (currentPath.getNameCount() > 0)) {
            if (userHomePath == null) {
                currentPath = currentPath.getParent();
                reloadFileDir();
                LOGGER.debug("moveToDirs user root");
                return true;
            } else if (currentPath.getNameCount() > userHomePath.getNameCount()) {
                currentPath = currentPath.getParent();
                reloadFileDir();
                LOGGER.debug("moveToDirs up to user root");
                return true;
            }
        } else {
            File isDir = new File(String.valueOf(Paths.get(currentPath.normalize().toString(), dir)));
            if (isDir.isDirectory()) {
                currentPath = Paths.get(currentPath.normalize().toString(), dir);
                reloadFileDir();
                LOGGER.debug("moveToDirs down to dir");
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void reloadFileDir() {
        LOGGER.debug("reloadFileDir");
        try {
            listFileDir = Files.walk(currentPath, 1).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        listFileDir.remove(0);
    }

    @Override
    public LinkedList<String> getListFileDir() {
        LOGGER.debug("getPathDir");
        reloadFileDir();
        LinkedList<String> listFile = new LinkedList<>();
        if (currentPath.getNameCount() > 0) {
            listFile.add("..");
        }
        for (Path list : listFileDir) {
            listFile.add(list.getFileName().toString());
        }
        return listFile;
    }

    @Override
    public void rename(String oldName, String newName) {
        LOGGER.debug("rename");
        try {
            Files.move(Paths.get(getPathDir(), oldName), Paths.get(getPathDir(), newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String name) {
        LOGGER.debug("delete");
        try {
            if (isDir(name)) {
                scanDelete(name);
            } else
                Files.delete(Paths.get(getPathDir(), name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] readFile(String name, Long position, Integer bufferLength) {
        LOGGER.debug("readFile");
        RandomAccessFile file = null;
        byte[] buffer;
        long lengthFile;

        try {
            file = new RandomAccessFile((Paths.get(getPathDir(), name).toString()), "r");
            lengthFile = file.length();
            if ((position + bufferLength) <= lengthFile) {
                buffer = new byte[bufferLength];
            } else buffer = new byte[(int) (lengthFile - position)];

            file.seek(position);
            file.read(buffer);
            return buffer;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    public boolean writeFile(String name, byte[] buffer) {
        LOGGER.debug("writeFile");
        RandomAccessFile file = null;
        long lengthFile;
        try {
            file = new RandomAccessFile((Paths.get(getPathDir(), name).toString()), "rw");
            lengthFile = file.length();
            file.seek(lengthFile);
            file.write(buffer);
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> getListCopyDir() {
        LOGGER.debug("getListCopyDir");
        return listCopyDir;
    }

    @Override
    public List<String> getListCopyFile() {
        LOGGER.debug("getListCopyFile");
        return listCopyFile;
    }

    @Override
    public void scanCopy(String name) {
        LOGGER.debug("scanCopy");
        listCopyDir.clear();
        listCopyFile.clear();

        if (isDir(name)) {
            try {
                Files.walkFileTree(
                        Paths.get(currentPath.toString(), name),
                        new SimpleFileVisitor<Path>() {

                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                    throws IOException {
                                listCopyFile.add(currentPath.relativize(file).toString());
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                                listCopyDir.add(currentPath.relativize(dir).toString());
                                return FileVisitResult.CONTINUE;
                            }

                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void scanDelete(String name) {
        LOGGER.debug("scanDelete");
        if (isDir(name)) {
            try {
                Files.walkFileTree(
                        Paths.get(currentPath.toString(), name),
                        new SimpleFileVisitor<Path>() {

                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                    throws IOException {
                                Files.delete(file);
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                                Files.delete(dir);
                                return FileVisitResult.CONTINUE;
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean isDir(String name) {
        LOGGER.debug("isDir");
        File isDir = new File(String.valueOf(Paths.get(currentPath.toString(), name)));
        if (isDir.isDirectory()) return true;
        return false;
    }

    @Override
    public void createDir(LinkedList<String> listCopyDir) {
        LOGGER.debug("createDir list");
        for (String path : listCopyDir) {
            try {
                Files.createDirectories(Paths.get(currentPath.normalize().toString(), String.valueOf(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void createDir(String path) {
        LOGGER.debug("createDir");
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



