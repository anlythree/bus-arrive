package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 高德公交路线规划api返回值
 * @author anlythree
 * @description:
 * @time 2022/3/156:45 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapBusRouteTimeRes extends AMapResult{

    private AMapBusRouteInfo route;

    public static class AMapBusRouteInfo{

        /**
         * 起点经纬度
         */
        private String origin;

        /**
         * 终点经纬度
         */
        private String destination;

        /**
         * 公交方案列表
         */
        private TransitsInfo transits;

        public static class TransitsInfo{

        }

    }

}
