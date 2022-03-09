package top.anlythree.api.xiaoyuanimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anlythree.api.absres.ApiRootResult;
import top.anlythree.utils.exceptions.AException;

import java.util.Objects;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/110:07 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoYuanResult extends ApiRootResult {

    private String returnCode;

    private String errorCode;

    @Override
    public void isApiError() {
        if (!(Objects.equals("ok", this.returnCode) || Objects.equals("0", this.errorCode))) {
            throw new AException("笑园api异常，returnCode:" + returnCode + ",errorCode:" + errorCode);
        }
    }

}
