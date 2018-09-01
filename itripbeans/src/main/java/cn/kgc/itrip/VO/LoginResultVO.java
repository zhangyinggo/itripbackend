package cn.kgc.itrip.VO;

/**
 * @className LoginResultVO
 * @Description
 * @Author 张赢
 * @Date 2018/8/30 9:42
 * @Version 1.0
 **/
public class LoginResultVO {
    //token
    private String  token;
    //token 构建时间
    private Long genTime;
    //token 过期时间
    private Long expTime;

    public LoginResultVO(String token, Long genTime, Long expTime) {
        this.token = token;
        this.genTime = genTime;
        this.expTime = expTime;
    }

    public LoginResultVO() {
    }

    public String getToken() {
        return token;
    }

    public Long getGenTime() {
        return genTime;
    }

    public Long getExpTime() {
        return expTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setGenTime(Long genTime) {
        this.genTime = genTime;
    }

    public void setExpTime(Long expTime) {
        this.expTime = expTime;
    }
}
