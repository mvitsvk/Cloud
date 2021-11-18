import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.mvitsvk.server.service.impl.SqlMariadb;

public class Sqltest {

    @Test
    public void AuthFindTest (){
        SqlMariadb sql = new SqlMariadb();
        Assertions.assertEquals("test", sql.AuthFind("test", "test1"));
    }
}
