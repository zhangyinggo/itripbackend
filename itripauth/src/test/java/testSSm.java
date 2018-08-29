import cn.kgc.itrip.common.RedisAPI;
import cn.kgc.itrip.model.ItripUser;
import cn.kgc.itrip.service.ItripUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @className testSSm
 * @Description
 * @Author 张赢
 * @Date 2018/8/28 10:14
 * @Version 1.0
 **/
/*指定运行平台*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-public.xml")
public class testSSm {
    @Resource
    private ItripUserService itripUserService;
    @Resource
    private RedisAPI redisAPI;

    @Test
    public void testRedis(){
        redisAPI.set("kgc","123",60);
    }

    @Test
    public void testSpring()throws Exception{
        ItripUser itripUser =itripUserService.getById(12l);
        System.out.println(itripUser.getUserName());
    }

}
