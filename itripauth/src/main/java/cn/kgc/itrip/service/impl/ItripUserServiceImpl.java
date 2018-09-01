package cn.kgc.itrip.service.impl;
import cn.kgc.itrip.common.*;
import cn.kgc.itrip.exception.ItripException;
import cn.kgc.itrip.service.ItripUserService;
import cn.kgc.itrip.mapper.ItripUserMapper;
import cn.kgc.itrip.model.ItripUser;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

@Service("itripUserService")
public class ItripUserServiceImpl implements ItripUserService {

    @Resource
    private ItripUserMapper itripUserMapper;

    @Resource
    private RedisAPI redisAPI;
    public ItripUser getById(Long id)throws Exception{
        return itripUserMapper.getById(id);
    }

    public List<ItripUser> getListByMap(Map<String,Object> param)throws Exception{
        return itripUserMapper.getListByMap(param);
    }

    public Integer getCountByMap(Map<String,Object> param)throws Exception{
        return itripUserMapper.getCountByMap(param);
    }
    //注册
    public Integer save(ItripUser itripUser)throws Exception{
            itripUser.setCreationDate(new Date());
            return itripUserMapper.save(itripUser);
    }

    public Integer modify(ItripUser itripUser)throws Exception{
        itripUser.setModifyDate(new Date());
        return itripUserMapper.modify(itripUser);
    }

    public Integer removeById(Long id)throws Exception{
        return itripUserMapper.removeById(id);
    }

    public Page<List<ItripUser>> queryPageByMap(Map<String,Object> param,Integer pageNo,Integer pageSize)throws Exception{
        Integer total = itripUserMapper.getCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripUser> itripUserList = itripUserMapper.getListByMap(param);
        page.setRows(itripUserList);
        return page;
    }

    @Override
    public ItripUser login(String userCode, String userPassword) throws Exception {
        //判断用户账号是否存在
        ItripUser itripUser=this.findByUserCode(userCode);
        if(itripUser != null){
        //判断密码是否正确
           String md5UserPassword= DigestUtil.hmacSign(userPassword);
           if(md5UserPassword.equals(itripUser.getUserPassword())){
              //判断用户是否被激活
              if(itripUser.getActivated()==1){
                  return itripUser;
              } else {
                  throw new ItripException("用户未激活",ErrorCode.AUTH_LOGIN_FAILURE);
              }
           }else{
               throw new ItripException("用户名或密码错误",ErrorCode.AUTH_LOGIN_FAILURE);
           }
        }else{
            throw new ItripException("用户不存在",ErrorCode.AUTH_LOGIN_FAILURE);
        }
    }

    @Override
    public ItripUser findByUserCode(String userCode) throws Exception{
        Map<String ,Object> param = new HashMap<>();
        param.put("userCode",userCode);
        List<ItripUser> itripUserList = getListByMap(param);
        return  itripUserList !=null && itripUserList.size()>0
                ? itripUserList.get(0) : null;

    }


    public ItripUser findByCode(String name) throws Exception{
        ItripUser byUserCode = this.findByUserCode(name);
         return  byUserCode;
    }

    public  void sendTemplateSMS(String to, String templateId, String[] datas){
        HashMap<String, Object> result = null;
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");
        // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
        restAPI.setAccount("\n" +
                "8aaf070865796a5701658d9bc2100fc9", "d6a3f31171124a44968563a680639408");
        // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
        restAPI.setAppId("8aaf070865796a5701658d9bc2650fcf");
        // 请使用管理控制台中已创建应用的APPID。
        result = restAPI.sendTemplateSMS(to,"1" ,datas);
        System.out.println("SDKTestGetSubAccounts result=" + result);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }
public  void send(ItripUser itripUser) throws Exception {
        itripUserMapper.save(itripUser);
        Integer i = DigestUtil.randomCode();
        this.sendTemplateSMS(itripUser.getUserCode(),"1",new String[]{String.valueOf(i),"1"});
    redisAPI.set("action"+itripUser.getUserCode(),String.valueOf(i),100);
}

}
