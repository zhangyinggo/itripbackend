package cn.kgc.itrip.controller;

import cn.kgc.itrip.common.Dto;
import cn.kgc.itrip.common.DtoUtil;
import cn.kgc.itrip.common.ErrorCode;
import cn.kgc.itrip.common.ValidationUtil;
import cn.kgc.itrip.model.ItripUser;
import cn.kgc.itrip.model.ItripUserLinkUser;
import cn.kgc.itrip.service.ItripUserLinkUserService;
import jdk.nashorn.internal.parser.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className UserController
 * @Description
 * @Author 张赢
 * @Date 2018/9/3 20:40
 * @Version 1.0
 **/
@Controller
@RequestMapping("/api")
public class UserController {

    @Resource
    private ItripUserLinkUserService itripUserLinkUserService;

    @Resource
    private ValidationUtil validationUtil;

    //@Resource
   // private ItripUser itripUser;

    //新增常用旅客信息
    @RequestMapping(value = "/userinfo/adduserlinkuser", method = RequestMethod.POST)
    @ResponseBody
    public Dto adduserlinkuser(@RequestBody ItripUserLinkUser itripSearchUserLinkUserVO, HttpServletRequest request) {
        ItripUser itripUserToken = validationUtil.getUser(request.getHeader("token"));
        if (null==itripUserToken) {
            return DtoUtil.returnFail("Token已失效", ErrorCode.BIZ_TOKEN_INVALID);
        } else {
            Integer  result = null;
            try {
                itripSearchUserLinkUserVO.setCreatedBy(itripUserToken.getId());
                itripSearchUserLinkUserVO.setUserId(itripUserToken.getId().intValue());
                result = itripUserLinkUserService.save(itripSearchUserLinkUserVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result > 0) {
                    return DtoUtil.returnSuccess("新增常用联系人成功");
                } else {
                    return DtoUtil.returnFail("新增常用联系人失败",ErrorCode.BIZ_USER_OFTEN_ADD_FAILED);
                }

        }
    }


    @RequestMapping(value = "userinfo/queryuserlinkuser", method = RequestMethod.POST)
    @ResponseBody
    public Dto queryuserlinkuser(@RequestBody ItripUserLinkUser itripSearchUserLinkUserVO, HttpServletRequest request) {
        ItripUser itripUserToken = validationUtil.getUser(request.getHeader("token"));
        if (null==itripUserToken) {
            return DtoUtil.returnFail("Token已失效", ErrorCode.BIZ_TOKEN_INVALID);
        } else {
            Long DQid = itripUserToken.getId();
            itripSearchUserLinkUserVO.setUserId(DQid.intValue());
            try {
                List<ItripUserLinkUser> itripUserLinkUsers = itripUserLinkUserService.getlinkUserName(itripSearchUserLinkUserVO);

                return DtoUtil.returnDataSuccess(itripUserLinkUsers);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("查询常用联系人失败",ErrorCode.BIZ_USER_OFTEN_FAILED);
            }
        }
    }



    @RequestMapping(value = "/userinfo/deluserlinkuser", method = RequestMethod.GET)
    @ResponseBody
    public Dto deluserlinkuser( Long ids , HttpServletRequest request) {
        ItripUser itripUserToken = validationUtil.getUser(request.getHeader("token"));
        if (null==itripUserToken) {
            return DtoUtil.returnFail("Token已失效", ErrorCode.BIZ_TOKEN_INVALID);
        } else {
            try {
                itripUserLinkUserService.removeById(ids);
                return DtoUtil.returnSuccess("删除成功");
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("删除常用联系人失败",ErrorCode.BIZ_USER_OFTEN_DEL_FAILED);
            }
        }
    }


    @RequestMapping(value = "userinfo/modifyuserlinkuser", method = RequestMethod.POST)
    @ResponseBody
    public Dto modifyuserlinkuser(@RequestBody ItripUserLinkUser itripModifyUserLinkUserVO, HttpServletRequest request) {
        ItripUser itripUserToken = validationUtil.getUser(request.getHeader("token"));
        if (null==itripUserToken) {
            return DtoUtil.returnFail("Token已失效", ErrorCode.BIZ_TOKEN_INVALID);
        } else {
            try {
                itripUserLinkUserService.modify(itripModifyUserLinkUserVO);
                return DtoUtil.returnSuccess("修改成功");
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("修改常用联系人失败",ErrorCode.BIZ_USER_OFTEN_DEL_FAILED);
            }
        }
    }

}
