package cn.kgc.itrip.service;

import cn.kgc.itrip.common.DigestUtil;
import cn.kgc.itrip.exception.ItripException;
import cn.kgc.itrip.model.ItripUser;

/**
 * @className TokenService
 * @Description TODO
 * @Author 张赢
 * @Date 2018/8/29 12:06
 * @Version 1.0
 **/
public interface TokenService {
    //生成token
    public String generateToken(ItripUser itripuser,String userAgent);

    //保存Token
    public void saveToken(String token , ItripUser itripUser);


    //判断Token是否存在
    Boolean exists(String token);

    //删除
    Boolean del (String  token);

    //重置token
    public String retoken(String token ,String userAgent) throws ItripException;

    //验证token
    Boolean validateToken(String token, String userAgent);




}
