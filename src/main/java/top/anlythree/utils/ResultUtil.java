package top.anlythree.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import top.anlythree.api.amapimpl.res.AMapResult;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanResult;
import top.anlythree.utils.exceptions.AException;

import java.util.Objects;

/**
 * 结果工具类
 *
 * @author anlythree
 * @time 2022/3/111:23 上午
 */
@Component
public class ResultUtil {

    public static <T extends XiaoYuanResult> T getXiaoYuanModel(ResponseEntity<T> responseEntity){
        if(!Objects.equals(HttpStatus.OK,responseEntity.getStatusCode())){
            throw new AException("api调用错误");
        }
        if(!responseEntity.hasBody()){
            // 成功返回，但返回值没有内容
            return null;
        }
        responseEntity.getBody().isApiError();
        // 具体返回值中是否存在异常
        return responseEntity.getBody();
    }

    public static <T extends AMapResult> getAMapModel(ResponseEntity<T> responseEntity){
        if(!Objects.equals(HttpStatus.OK,responseEntity.getStatusCode())){
            throw new AException("api调用错误");
        }
        if(!responseEntity.hasBody()){
            // 成功返回，但返回值没有内容
            return null;
        }
        responseEntity.getBody().isApiError();
        // 具体返回值中是否存在异常
        return responseEntity.getBody();
    }
}
