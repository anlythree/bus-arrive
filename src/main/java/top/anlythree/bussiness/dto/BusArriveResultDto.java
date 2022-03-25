package top.anlythree.bussiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/2318:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusArriveResultDto {

    /**
     * 出发地名称
     */
    private String startLocationName;

    /**
     * 目的地名称
     */
    private String endLocationName;

    /**
     * 路线名称（公交路线）
     */
    private String routeName;

    /**
     * 最晚公交车发车计算时间（理想计算时间）
     */
    private String calculateTime;

    /**
     * 要求到达时间
     */
    private String arriveTime;

    /**
     * 计算出来的出发时间
     */
    private String leaveStartLocationTime;
}
