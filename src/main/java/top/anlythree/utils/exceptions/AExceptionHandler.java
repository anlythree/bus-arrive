package top.anlythree.utils.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义异常处理
 * @author anlythree
 * @description:
 * @time 2022/3/11:36 下午
 */
@AllArgsConstructor
@ControllerAdvice
public class AExceptionHandler{

    @ExceptionHandler(value = AException.class)
    @ResponseBody
    public String exceptionHandler(HttpServletRequest request, Exception e){
        // todo-anlythree 上线后删除
        e.printStackTrace();
        return "AException>>>"+e.getMessage();
    }
}
