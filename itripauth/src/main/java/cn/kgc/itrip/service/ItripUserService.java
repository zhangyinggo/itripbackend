package cn.kgc.itrip.service;
import cn.kgc.itrip.model.ItripUser;
import java.util.List;
import java.util.Map;
import cn.kgc.itrip.common.Page;


public interface ItripUserService {

    public ItripUser getById(Long id)throws Exception;

    public List<ItripUser>	getListByMap(Map<String, Object> param)throws Exception;

    public Integer getCountByMap(Map<String, Object> param)throws Exception;
    //注册
    public Integer save(ItripUser itripUser)throws Exception;

    public Integer modify(ItripUser itripUser)throws Exception;

    public Integer removeById(Long id)throws Exception;

    public Page<List<ItripUser>> queryPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;

    //登录
    public ItripUser login(String userCode,String userPassword) throws Exception;

    //根据suerCode查询用户信息
    public ItripUser findByUserCode(String userCode) throws Exception;

    public ItripUser findByCode(String name) throws Exception;
    //发短信激活码接口  参数1,手机号 2，模板号 3，数组含 随机激活码和“表达内容” 接口
    public  void sendSMS(String to, String templateId, String[] datas);
    //手机1，调新增方法 2，制作随机数 3，调发短信方法 4，拼接字符串存redis  接口
    public  void sendphone(ItripUser itripUser) throws Exception;

    //发邮件激活码接口
    public void sendEmail(String email,String mailJHM)throws Exception;
    //邮件
    public void sendEmailLB(ItripUser itripUser) throws Exception;

}
