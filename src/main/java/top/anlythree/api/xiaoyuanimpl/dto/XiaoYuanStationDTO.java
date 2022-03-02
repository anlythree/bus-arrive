package top.anlythree.api.xiaoyuanimpl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/110:33 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoYuanStationDTO {

    private String stationCode;

    private String cityName;

    private String stationName;
}
