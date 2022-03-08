package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anlythree.api.abs.RootResult;
import top.anlythree.utils.exceptions.AException;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapResult extends RootResult {

    private Integer status;

    private String info;

    private Integer count;

    @Override
    public void isApiError() {
        if (Objects.equals(0, status)) {
            throw new AException("高德api异常，status:" + status + ",info:" + info);
        }
    }
}
