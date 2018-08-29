package cn.kgc.itrip.service;

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
}
