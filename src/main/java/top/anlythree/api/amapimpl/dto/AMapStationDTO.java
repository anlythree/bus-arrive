package top.anlythree.api.amapimpl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地点DTO
 * @author anlythree
 * @description:
 * @time 2022/3/110:33 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapStationDTO {

    private String cityName;

    private String stationName;

    /**
     * （经，纬）
     */
    private String longitudeAndLatitude;
}
