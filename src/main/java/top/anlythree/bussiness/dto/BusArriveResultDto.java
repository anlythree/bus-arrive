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

    private String startLocationName;

    private String endLocationName;

    private String arriveTime;

    private String routeName;
}
