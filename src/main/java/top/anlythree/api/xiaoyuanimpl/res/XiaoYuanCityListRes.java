package top.anlythree.api.xiaoyuanimpl.res;

import lombok.Data;
import top.anlythree.api.xiaoyuanimpl.res.results.XiaoYuanResult;

import java.util.ArrayList;

/**
 * @author anlythree
 * @description: 笑园api城市列表返回值
 * @time 2022/3/110:11 上午
 */
@Data
public class XiaoYuanCityListRes extends XiaoYuanResult {
    private ArrayList<XiaoYuanCityRes> returlList;
}
