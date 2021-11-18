import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import su.mvitsvk.common.FileService;
import su.mvitsvk.common.ImplementFileService;

import java.nio.file.Path;



public class ImplementFileServiceTest {
    FileService fs;

    @BeforeEach
public void Init(){
        fs = new ImplementFileService("data","test");
}

    @Test
    public void isDirTest (){
        fs.createDir(Path.of(fs.getPathDir(),"DT").toString());
        Assertions.assertEquals(true, fs.isDir("DT"));
        fs.delete("DT");
    }

    @Test
    public void renameTest (){
        fs.createDir(Path.of(fs.getPathDir(),"DT2").toString());
        fs.rename("DT2", "DT3");
        Assertions.assertEquals(true, fs.isDir("DT3"));
        fs.delete("DT3");
    }

}
