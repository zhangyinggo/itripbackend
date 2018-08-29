package cn.kgc.itrip;

import static org.junit.Assert.assertTrue;

import cn.kgc.itrip.mapper.ItripUserMapper;
import cn.kgc.itrip.model.ItripUser;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

/**
 * Unit test for simple App.
 */
public class AppTest 
{


    public static void main(String[] args) throws Exception {
        InputStream inputStream =Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sessionFactory=
                new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sessionFactory.openSession();
        ItripUserMapper itripUserMapper =sqlSession.getMapper(ItripUserMapper.class);
        ItripUser itripUser=itripUserMapper.getById(12l);
        System.out.println("userName:"+ itripUser.getUserName());

    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}
