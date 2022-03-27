package top.anlythree.utils.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    public String exceptionHandler(Exception e){
        return "AException>>>"+e.getMessage();
    }
}
