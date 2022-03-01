package top.anlythree.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description: 自定义异常
 * @time 2022/3/12:01 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AException extends RuntimeException{

    private String message;
}
