package top.anlythree.api.res.abs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/110:07 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class XiaoYuanResult {

    private String returnCode;

    private String errorCode;
}
