package cn.kgc.itrip.controller;

import cn.kgc.itrip.VO.LoginResultVO;
import cn.kgc.itrip.common.Dto;
import cn.kgc.itrip.common.DtoUtil;
import cn.kgc.itrip.exception.ItripException;
import cn.kgc.itrip.service.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @className TokenController
 * @Description
 * @Author 张赢
 * @Date 2018/8/30 10:44
 * @Version 1.0
 **/
@Controller
@RequestMapping("/api")
public class TokenController {

    @Resource
    private TokenService tokenService;

    @RequestMapping(value ="/retoken",method = RequestMethod.POST)
    @ResponseBody
    public Dto retoken(HttpServletRequest request){
        //获取token
        try {
            String token =request.getHeader("token");
            String userAgent=request.getHeader("user-agent");
            //生成新的Token
            String newToken = tokenService.retoken(token,userAgent);
            //返回数据
            Long genTime =System.currentTimeMillis();
            Long expTime = genTime + (1000 * 60 * 60 * 2);
            return DtoUtil.returnDataSuccess(new LoginResultVO(newToken, genTime, expTime));
        } catch (ItripException e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),e.getErrorCode());
        }
    }
}

