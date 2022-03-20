package top.anlythree.api.xiaoyuanimpl.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 首班车时间
     */
    private String firstTime;
    /**
     * 末班车时间
     */
    private String endTime;
    /**
     * 票价
     */
    private BigDecimal moneyQty;

    private List<String> stationList;

}
