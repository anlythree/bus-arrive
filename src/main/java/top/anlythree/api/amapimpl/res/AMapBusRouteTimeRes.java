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

    /**
     * 返回的规划方案列表
     */
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
            /**
             * 本条路线的总距离，单位：米
             */
            private String distance;

            /**
             * 0：非夜班车；1：夜班车
             */
            private Integer nightflag;

            /**
             * 路线分段
             */
            private SegmentsInfo segments;

            public static class SegmentsInfo{

                /**
                 * 此分段中需要步行导航的信息
                 */
                private String walking;

                /**
                 * 此分段中需要公交导航的信息
                 */
                private String bus;

                /**
                 * 此分段中需要火车的信息
                 */
                private String railway;

                /**
                 * 打车信息
                 */
                private TexiInfo taxi;

                public static class TexiInfo{
                    /**
                     * 打车预计花费金额
                     */
                    private String price;

                    /**
                     * 打车预计花费时间
                     */
                    private String drivetime;

                    /**
                     * 打车距离
                     */
                    private String distance;

                    /**
                     * 线路点集合，通过show_fields控制返回与否
                     */
                    private String polyline;

                    /**
                     * 打车起点经纬度
                     */
                    private String startpoint;

                    /**
                     * 打车起点名称
                     */
                    private String startname;

                    /**
                     * 打车终点经纬度
                     */
                    private String endpoint;

                    /**
                     * 打车终点名称
                     */
                    private String endname;
                }
            }
        }
    }
}
