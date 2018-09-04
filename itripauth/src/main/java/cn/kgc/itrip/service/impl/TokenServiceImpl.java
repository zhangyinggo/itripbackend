package cn.kgc.itrip.service.impl;

import cn.kgc.itrip.common.*;
import cn.kgc.itrip.exception.ItripException;
import cn.kgc.itrip.model.ItripUser;
import cn.kgc.itrip.service.TokenService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className TokenServiceImpl
 * @Description
 * @Author 张赢
 * @Date 2018/8/29 12:09
 * @Version 1.0
 **/
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisAPI redisAPI;

    @Resource
    private ValidationUtil validationUtil;

    //token:客户端标示 token唯一标示的令牌 按照下面的顺序格式排列
    @Override
    public String generateToken(ItripUser itripuser ,String userAgent) {
        StringBuffer sbToken=new StringBuffer("token:");
        //客户单表示 pc mobile 拼接
        if (UserAgentUtil.CheckAgent(userAgent)){
            sbToken.append("MOBILE-");
        }else{
            sbToken.append("PC-");
        }
        //usercode md5加密
        sbToken.append(DigestUtil.hmacSign(itripuser.getUserCode())+"-");
        //userID
        sbToken.append(itripuser.getId()+"-");
        //创建时间 yyyymmddhhmmss
        String dateTime=new SimpleDateFormat(
                "yyyyMMddHHmmss").format(new Date());
        sbToken.append(dateTime+"-");
        //userAgent 加密取前六位
        String md5UserAgent=DigestUtil.hmacSign(userAgent).substring(0,6);
        sbToken.append(md5UserAgent);

        return sbToken.toString();
    }

    @Override
    public void saveToken(String token, ItripUser itripUser) {
        String  itripUserJSON = JSON.toJSONString(itripUser);
        if(token.indexOf("PC") != -1){
            redisAPI.set(token,itripUserJSON,60*60*2);
        }else{
            redisAPI.set(token,itripUserJSON);
        }
    }

    @Override
    public Boolean exists(String token) {

        return redisAPI.exists(token);
    }

    @Override
    public Boolean del(String token) {
        if(redisAPI.del(token) ==1){
            return true;
        }
        return false;
    }

    @Override
    public String retoken(String token, String userAgent) throws ItripException {
        //1,验证token是否有效
        if(!this.validateToken(token,userAgent))
            throw new ItripException("token失效",ErrorCode.BIZ_TOKEN_INVALID);
        //2,能不能够置换，是否处于保护器

        try {
            Long currTime =System.currentTimeMillis();
            //token 构建时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Long  createTime=sdf.parse(token.split("-")[3]).getTime();
            //保护器 1个小时
            int protectTime = 1000*60*60*1;
            if(currTime - createTime < protectTime){
                throw new ItripException("Token处于保护期，无法置换",ErrorCode.AUTH_REPLACEMENT_FAILED);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //判断Token剩余过期时间
        Long ttl=redisAPI.ttl(token);
        //3,进行置换（兼容移动端和PC端）
        if(ttl>0 || ttl==-1){
            //4,老的token延迟过期
            redisAPI.expire(token,60*3);
            //5,新的 token 保存到Redis中
            //token key value itripUser json --->转换一个对象bean
            ItripUser user=validationUtil.getUser(token);
            String  newToken = this.generateToken(user,userAgent);
            //保存Token
            this.saveToken(newToken,user);
            return  newToken;
        }else {
            throw new ItripException("token失效",ErrorCode.BIZ_TOKEN_INVALID);
        }
    }

    @Override
    public Boolean validateToken(String token , String userAgent){
        //判断token是否存在
        if (!this.exists(token)) {
            return false;
        }
        //判断登录浏览器和退出浏览器是否一致
        String md5UserAgent = DigestUtil.hmacSign(userAgent).substring(0, 6);
        //token 规则 :token:客户端标示 -USERCODE-USERID-CREATIONSATE-RONDEM[6位]
        if (!token.split("-")[4].equals(md5UserAgent)) {
            return false;
        }
        return  true;
    }
}
