package top.anlythree.api.amapimpl.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用到的url记录
 *
 * @author anlythree
 * @description:
 * @time 2022/3/2315:33
 */
@NoArgsConstructor
@AllArgsConstructor
public enum UrlTypeEnum {

    // 经纬度查询
    LOCATION("https://restapi.amap.com/v3/geocode/geo", ServiceEnum.AMAP),
    // 公交路径规划
    BUS_ROUTE("https://restapi.amap.com/v3/direction/transit/integrated", ServiceEnum.AMAP),
    // 步行路径规划
    WALK_ROUTE("https://restapi.amap.com/v3/direction/walking", ServiceEnum.AMAP),
    // 乘车路径规划
    CAR_ROUTE("https://restapi.amap.com/v3/direction/walking", ServiceEnum.AMAP),

    // 公交实时位置
    BUS_LOCATION("http://api.dwmm136.cn/z_busapi/BusApi.php", ServiceEnum.XIAO_YUAN),
    ;

    /**
     * url
     */
    private String url;

    /**
     * 服务提供者
     */
    private ServiceEnum serviceType;

    public String getUrl() {
        return url;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public enum ServiceEnum {

        /**
         * 笑园科技
         */
        XIAO_YUAN("笑园实时公交api（网址：http://dwmm136.cn/app/index.php）"),
        /**
         * 高德地图
         */
        AMAP("高德api");

        /**
         * 备注
         */
        private String desc;

    }

}
