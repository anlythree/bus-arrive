package top.anlythree.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thymeleaf.standard.expression.GreaterThanExpression;
import top.anlythree.utils.ResultUtil;

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
