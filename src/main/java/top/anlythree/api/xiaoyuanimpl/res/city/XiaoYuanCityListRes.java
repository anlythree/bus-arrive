package top.anlythree.api.xiaoyuanimpl.res.city;

import lombok.Data;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanResult;

import java.util.List;

/**
 * @author anlythree
 * @description: 笑园api城市列表返回值
 * @time 2022/3/110:11 上午
 */
@Data
public class XiaoYuanCityListRes extends XiaoYuanResult {

    private List<XiaoYuanCityRes> returlList;
}
