package top.anlythree.api.xiaoyuanimpl.res.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanResult;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/13:32 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoYuanRouteListRes extends XiaoYuanResult {

    private List<XiaoYuanRouteRes> returlList;
}
