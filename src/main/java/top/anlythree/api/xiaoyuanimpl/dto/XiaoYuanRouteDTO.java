package top.anlythree.api.xiaoyuanimpl.dto;

import lombok.Data;

/**
 * 公交路线
 * @author anlythree
 * @time 2022/3/110:33 上午
 */
@Data
public class XiaoYuanRouteDTO {
    /**
     * 起始站
     */
    private String startStation;

    /**
     * 终点站
     */
    private String endStation;

    /**
     * 路线ID
     */
    private String routeId;

    /**
     * 路线编号
     */
    private String routeCode;

    /**
     * 路线名称
     */
    private String routeName;

    /**
     * 城市id
     */
    private String cityId;
}
