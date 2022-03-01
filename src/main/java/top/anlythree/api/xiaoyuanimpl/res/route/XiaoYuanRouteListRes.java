package top.anlythree.api.xiaoyuanimpl.res.route;

import lombok.Data;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanResult;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/13:32 下午
 */
@Data
public class XiaoYuanRouteListRes extends XiaoYuanResult {

    private List<XiaoYuanRouteRes> xiaoYuanRouteResList;
}
