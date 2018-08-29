package cn.kgc.itrip.service.impl;
import cn.kgc.itrip.common.*;
import cn.kgc.itrip.exception.ItripException;
import cn.kgc.itrip.service.ItripUserService;
import cn.kgc.itrip.mapper.ItripUserMapper;
import cn.kgc.itrip.model.ItripUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("itripUserService")
public class ItripUserServiceImpl implements ItripUserService {

    @Resource
    private ItripUserMapper itripUserMapper;

    public ItripUser getById(Long id)throws Exception{
        return itripUserMapper.getById(id);
    }

    public List<ItripUser> getListByMap(Map<String,Object> param)throws Exception{
        return itripUserMapper.getListByMap(param);
    }

    public Integer getCountByMap(Map<String,Object> param)throws Exception{
        return itripUserMapper.getCountByMap(param);
    }

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

}
