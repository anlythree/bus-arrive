package top.anlythree.bussiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
     * 要求到达时间
     */
    private String arriveTime;

    /**
     * 计算出来的出发时间
     */
    private List<String> leaveStartLocationTime;

    /**
     *  是不是赶不上最后一班公交了，差多少分钟
     */
    private String isTooLate;
}
