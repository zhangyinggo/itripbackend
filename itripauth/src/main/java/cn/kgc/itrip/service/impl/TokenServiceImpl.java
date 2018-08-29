package cn.kgc.itrip.service.impl;

import cn.kgc.itrip.common.DigestUtil;
import cn.kgc.itrip.common.RedisAPI;
import cn.kgc.itrip.common.UserAgentUtil;
import cn.kgc.itrip.model.ItripUser;
import cn.kgc.itrip.service.TokenService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
