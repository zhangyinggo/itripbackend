package cn.kgc.itrip.controller;

import cn.kgc.itrip.VO.LoginResultVO;
import cn.kgc.itrip.common.DigestUtil;
import cn.kgc.itrip.common.Dto;
import cn.kgc.itrip.common.DtoUtil;
import cn.kgc.itrip.common.ErrorCode;
import cn.kgc.itrip.exception.ItripException;
import cn.kgc.itrip.model.ItripUser;
import cn.kgc.itrip.service.ItripUserService;
import cn.kgc.itrip.service.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpServletRequest;

/**
 * @className LoginController
 * @Description
 * @Author 张赢
 * @Date 2018/8/30 8:59
 * @Version 1.0
 **/
@Controller
@RequestMapping("/api")
public class LoginController {
    @Resource
    private ItripUserService itripUserService;


    @Resource
    private TokenService tokenService;


    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    @ResponseBody
    public Dto doLogin(String name, String password, HttpServletRequest request) {
        //登录,验证用户账号密码
        try {
            ItripUser itripUser = itripUserService.login(name, password);
            //登录成功
            String userAgent = request.getHeader("user-agent");
            //构建Token
            String token = tokenService.generateToken(itripUser, userAgent);
            //保存token
            tokenService.saveToken(token, itripUser);
            //返回数据
            Long genTime = System.currentTimeMillis();
            Long expTime = genTime + (1000 * 60 * 60 * 2);
            return DtoUtil.returnDataSuccess(new LoginResultVO(token, genTime, expTime));
        } catch (ItripException e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("登录失败", ErrorCode.AUTH_LOGIN_FAILURE);
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Dto logout(HttpServletRequest request) {
        //获取token
        String token = request.getHeader("token");
        String userAgent = request.getHeader("user-agent");
        if (!tokenService.validateToken(token, userAgent)) {
            return DtoUtil.returnFail("Token无效", ErrorCode.AUTH_TOKEN_INVALID);
        }
        //退出
        tokenService.del(token);
        //返回退出成功
        return DtoUtil.returnSuccess("注销成功");
    }


    @RequestMapping(value = "/ckusr", method = RequestMethod.GET)
    @ResponseBody
    public Dto ckusr(String name) throws Exception {
        ItripUser itripUser = itripUserService.findByCode(name);
        if (null != itripUser) {
            return DtoUtil.returnFail("用户名已经存在", ErrorCode.AUTH_USER_ALREADY_EXISTS);
        } else {
            return DtoUtil.returnSuccess("成功");
        }


    }

    @RequestMapping(value = "/doregister", method = RequestMethod.POST)
    @ResponseBody
    public Dto doregister(@RequestBody ItripUser userVO) throws Exception {
        //通过参数取出三个属性值（并把密码加密）
        String userPawordMD5 = DigestUtil.hmacSign(userVO.getUserPassword());
        //封装进一个新对象中
        userVO.setUserCode(userPawordMD5);
        //调接口新增用户方法把对象参数放进去
        Integer result = itripUserService.save(userVO);
        if (result > 0) {
            return DtoUtil.returnSuccess();
        } else {
            return DtoUtil.returnFail("注册失败", ErrorCode.AUTH_REGISTER_FAILURE);
        }
    }


    @RequestMapping(value = "/registerbyphone", method = RequestMethod.POST)
    @ResponseBody
    public Dto registerbyphone(@RequestBody ItripUser userVO) throws Exception {
        //通过参数取出三个属性值（并把密码加密）
        String userPawordMD5 = DigestUtil.hmacSign(userVO.getUserPassword());
        //封装进一个新对象中
        userVO.setUserPassword(userPawordMD5);
        //调接口新增用户方法把对象参数放进去
        itripUserService.send(userVO);
            return DtoUtil.returnSuccess();

    }


}
