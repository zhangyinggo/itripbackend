package cn.kgc.itrip.common;

import cn.kgc.itrip.model.ItripUser;
import com.alibaba.fastjson.JSON;

/**
 * @className ValidationUtil
 * @Description  获取redis中存储的用户信息
 * 先从token转成json 在转成javabean对象
 * @Author 张赢
 * @Date 2018/8/30 11:24
 * @Version 1.0
 **/
public class ValidationUtil {
    private RedisAPI redisAPI;

    public void setRedisAPI(RedisAPI redisAPI) {
        this.redisAPI = redisAPI;
    }

    public ItripUser getUser(String token){
        if(!redisAPI.exists(token))
            return  null;
            String  itripUserJSON = redisAPI.get(token);
            //json-->bean
            ItripUser itripUser = JSON.parseObject(
                    itripUserJSON,ItripUser.class);
            return itripUser;

    }
}
