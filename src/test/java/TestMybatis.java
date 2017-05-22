import com.zhengyao.CachedThreadPool;
import com.zhengyao.dao.UserDao;
import com.zhengyao.util.ZhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/5/22.
 */
@ContextConfiguration("classpath*:/applicationContext.xml")
public class TestMybatis extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private UserDao userDao;

    @org.testng.annotations.Test
    public void TestMybatis(){
    String ul="a";
    userDao.insertUser(ul);

    }
    @org.testng.annotations.Test
    public void TestHandleTopic() throws InterruptedException, SQLException, IOException {
        ZhihuUtil.getTopicID();
        ZhihuUtil.getAllSubTopic();
        ZhihuUtil.getAllUserUrl();
    }
}
