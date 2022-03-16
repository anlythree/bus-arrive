package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
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
        private List<TransitsInfo> transits;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TransitsInfo{
            /**
             * 此换乘方案价格 单位：元
             */
            private String cost;

            /**
             * 此换乘方案预期时间 单位：秒
             */
            private String duration;

            /**
             * 总距离
             */
            private String distance;

            /**
             * 是否夜班车 0：非夜班车；1：夜班车
             */
            private String nightflag;

            /**
             * 此方案总步行距离 单位：米
             */
            private String walkingDistance;

            private List<SegmentsInfo> segments;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class SegmentsInfo{
                /**
                 * 此路段步行导航信息
                 */
                private Object walking;

                /**
                 * 此路段公交导航信息
                 */
                private BusInfo bus;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class BusInfo{

                    private List<BusLinesInfo> buslines;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class BusLinesInfo{
                        /**
                         * 公交名称
                         */
                        private String name;

                        /**
                         * 公交类型
                         */
                        private String type;

                        /**
                         * 距离
                         */
                        private String distance;

                        /**
                         * 用时
                         */
                        private String duration;
                    }
                }

            }

        }
    }
}
