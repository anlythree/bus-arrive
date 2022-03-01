package top.anlythree.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import top.anlythree.api.xiaoyuanimpl.res.results.XiaoYuanResult;

import java.util.Objects;

/**
 * 结果工具类
 *
 * @author anlythree
 * @time 2022/3/111:23 上午
 */
public class ResultUtil {

    public <T extends XiaoYuanResult> T getXiaoYuanModel(ResponseEntity<T> responseEntity) throws Exception {
        if(!Objects.equals(HttpStatus.OK,responseEntity.getStatusCode())){
            throw new Exception("api调用错误");
        }
        if(!responseEntity.hasBody()){
            // 成功返回，但返回值没有内容
            return null;
        }
        // 具体返回值中是否存在异常
        responseEntity.getBody().isApiError();
        return responseEntity.getBody()

    }
}
