package top.anlythree.dto;

import lombok.Data;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/110:33 上午
 */
@Data
public class Route {
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
