package top.anlythree.bussiness.dto;

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
public class StationDTO {

    private String bizIdStation;

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 站点完整名称，用于地图api搜索站点坐标
     */
    private String stationFullName;

    /**
     * （经，纬）
     */
    private String longitudeAndLatitude;

    /**
     * 国家。国内地址默认返回中国
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;
}
