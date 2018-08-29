package cn.kgc.itrip.exception;

/**
 * @className ItripException
 * @Description
 * @Author 张赢
 * @Date 2018/8/29 11:44
 * @Version 1.0
 **/
public class ItripException extends Exception {
    private String errorCode;

    public ItripException(String message, String errorCode) {
        super(message);/*调用父级的构造方法*/
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return errorCode;
    }
}
