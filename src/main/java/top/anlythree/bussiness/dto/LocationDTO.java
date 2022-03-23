package top.anlythree.bussiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * 地点DTO
 *
 * @author anlythree
 * @description:
 * @time 2022/3/110:33 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    /**
     * 地点名称
     */
    private String stationName;

    /**
     * 地点完整名称，用于地图api搜索站点坐标
     */
    private String stationFullName;

    /**
     * 经,纬
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


    public boolean eqauls(LocationDTO stationDTO) {
        if (Objects.equals(StringUtils.substringBeforeLast(stationDTO.getCountry(), "国"),
                StringUtils.substringBeforeLast(this.getCountry(), "国")) &&
                Objects.equals(StringUtils.substringBeforeLast(stationDTO.getCity(), "市"),
                        StringUtils.substringBeforeLast(this.getCity(), "市")) &&
                Objects.equals(StringUtils.substringBeforeLast(stationDTO.getProvince(), "省"),
                        StringUtils.substringBeforeLast(this.getProvince(), "省")) &&
                Objects.equals(StringUtils.substringBeforeLast(this.getDistrict(), "区"),
                        StringUtils.substringBeforeLast(stationDTO.getDistrict(), "区")) &&
                Objects.equals(this.getStationName(), stationDTO.getStationName())) {
            return true;
        }
        return false;
    }

    public LocationDTO(String country, String province, String city, String district, String stationName, String longitudeAndLatitude) {
        this.stationName = stationName;
        if(StringUtils.isEmpty(country)){
            this.country = "中国";
        }else{
            this.country = country;
        }
        this.province = province;
        this.city = city;
        this.district = district;
        this.stationFullName = this.country+this.province+this.city+this.district+this.stationName;
        this.longitudeAndLatitude = longitudeAndLatitude;
    }

    public LocationDTO(StationDTO stationDTO) {
        BeanUtils.copyProperties(stationDTO,this);
    }
}
