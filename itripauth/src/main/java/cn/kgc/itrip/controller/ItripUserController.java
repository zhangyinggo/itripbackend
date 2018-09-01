package cn.kgc.itrip.controller;

import cn.kgc.itrip.common.Dto;
import cn.kgc.itrip.common.DtoUtil;
import cn.kgc.itrip.model.ItripUser;
import cn.kgc.itrip.service.ItripUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @className ItripUserController
 * @Description
 * @Author 张赢
 * @Date 2018/8/28 12:00
 * @Version 1.0
 **/
@Controller
@RequestMapping("/api/user")
public class ItripUserController {
    @Resource
    private ItripUserService itripUserService;

    @RequestMapping("/getById/{userId}")
    @ResponseBody
    public Dto getById(@PathVariable("userId") Long userId) {

        ItripUser itripUser = null;
        try {
            itripUser = itripUserService.getById(userId);
            return DtoUtil.returnDataSuccess(itripUser);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("查询异常", "1001");
        }
    }




}
