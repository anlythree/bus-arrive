package top.anlythree.bussiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/93:58 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    /**
     * 起始站
     */
    private StationDTO startStation;

    /**
     * 终点站
     */
    private String endStation;

    /**
     * 路线名称
     */
    private String routeName;

    /**
     * 城市名称
     */
    private String cityName;
}
