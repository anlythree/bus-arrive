package top.anlythree.api.amapimpl.res.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStationRes {

    /**
     * 省份＋城市＋区县＋城镇＋乡村＋街道＋门牌号码
     */
    private String formatted_address;

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
     * 城市编码
     */
    private String citycode;

    /**
     * 区
     */
    private String district;

    /**
     * 街道
     */
    private String street;

    /**
     * 门牌
     */
    private String number;

    /**
     * 区域编码
     */
    private String adcode;

    /**
     * 坐标点（经度，纬度）
     */
    private String location;

    /**
     * 匹配级别
     */
    private String level;
}
