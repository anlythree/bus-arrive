package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import top.anlythree.utils.AStrUtil;

import java.util.List;

/**
 * @author anlythree
 * @description: 高德api路径规划2.0返回值
 * @time 2022/3/2915:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapBusRoute2Res extends AMapResult {

    private RouteRes route;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteRes {
        private String origin;
        private String destination;
        private List<TransitsRes> transits;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TransitsRes {
            private String distance;
            private String nightflag;

            private CostInfo cost;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class CostInfo {
                /**
                 * 用时
                 */
                private String duration;

                /**
                 * 价格
                 */
                private String transitFee;
            }

            private List<SegmentsRes> segments;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class SegmentsRes {
                private WalkingRes walking;
                private BusRes bus;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class WalkingRes {
                    private String destination;
                    private String distance;
                    private String origin;
                    private List<StepsRes> steps;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class StepsRes {
                        private String distance;
                        private String instruction;
                        private String road;
                    }
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class BusRes {
                    private List<BusLinesRes> buslines;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class BusLinesRes {
                        private DepartureStopRes departureStop;
                        private ArrivalStopRes arrivalStop;
                        private String name;
                        private String id;
                        private String type;
                        private String distance;
                        private String bustimetag;
                        private String startTime;
                        private String endTime;
                        private String viaNum;
                        private List<ViaStopsRes> viaStops;

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        public static class DepartureStopRes {
                            private String name;
                            private String id;
                            private String location;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        public static class ArrivalStopRes {
                            private String name;
                            private String id;
                            private String location;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        public static class ViaStopsRes {
                            private String name;
                            private String id;
                            private String location;
                        }
                    }
                }
            }

        }
    }

    /**
     * 根据公交路线名从高德返回的路径规划中找到需要的信息
     *
     * @return
     */
    public ImportInfo getImportInfo(String routeName) {
        for (RouteRes.TransitsRes transit : this.getRoute().getTransits()) {
            for (RouteRes.TransitsRes.SegmentsRes segment : transit.getSegments()) {
                if (null != segment.getBus() && !CollectionUtils.isEmpty(segment.getBus().getBuslines())) {
                    for (RouteRes.TransitsRes.SegmentsRes.BusRes.BusLinesRes busline : segment.getBus().getBuslines()) {
                        if (busline.getName().contains(routeName)) {
                            String busName;
                            String[] stationName = {null, null};
                            if (StringUtils.isNotEmpty(busName = busline.getName())) {
                                stationName = busName.substring(busName.indexOf("(") + 1, busName.lastIndexOf(")")).split("--");
                            }
                            return new ImportInfo(routeName,busName,
                                    AStrUtil.castLong(transit.getCost().getDuration()),
                                    AStrUtil.castLong(transit.getCost().getTransitFee()),
                                    getRoute().getOrigin(),
                                    getRoute().getDestination(),
                                    busline.getDepartureStop().getName(),
                                    busline.getDepartureStop().getLocation(),
                                    busline.getArrivalStop().getName(),
                                    busline.getArrivalStop().getLocation(),
                                    stationName[0],
                                    stationName[1]
                            );
                        }
                    }
                }
            }

        }
        return null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportInfo {
        /**
         * 路线名称
         */
        private String routeName;

        /**
         * 带方向的路线名称
         */
        private String routeFullName;

        /**
         * 用时（秒）
         */
        private Long seconds;

        /**
         * 费用
         */
        private Long cost;

        /**
         * 起始位置名称(无法直接推算，需要后期赋值)
         */
        private String startLocationName;

        /**
         * 起始位置坐标
         */
        private String startLocationLal;

        /**
         * 结束位置坐标
         */
        private String endLocationLal;

        /**
         * 上车公交站名称
         */
        private String startBusStationName;

        /**
         * 上车公交站坐标
         */
        private String startBusStationLal;

        /**
         * 下车公交站名称
         */
        private String endBusStationName;

        /**
         * 下车公交站坐标
         */
        private String endBusStationLal;

        /**
         * 起始站名称
         */
        private String firstBusStationName;

        /**
         * 起始站坐标（无法直接推算，需要后期赋值）
         */
        private String firstBusStationLal;

        /**
         * 最后一站名称
         */
        private String lastBusStationName;

        public ImportInfo(String routeName,
                          String routeFullName,
                          Long seconds,
                          Long cost,
                          String startLocationLal,
                          String endLocationLal,
                          String startBusStationName,
                          String startBusStationLal,
                          String endBusStationName,
                          String endBusStationLal,
                          String firstBusStationName,
                          String lastBusStationName) {
            this.routeName = routeName;
            this.routeFullName = routeFullName;
            this.seconds = seconds;
            this.cost = cost;
            this.startLocationLal = startLocationLal;
            this.endLocationLal = endLocationLal;
            this.startBusStationName = startBusStationName;
            this.startBusStationLal = startBusStationLal;
            this.endBusStationName = endBusStationName;
            this.endBusStationLal = endBusStationLal;
            this.firstBusStationName = firstBusStationName;
            this.lastBusStationName = lastBusStationName;
        }

        /**
         * 获取没有等待的用时
         * @return
         */
        public Long getNoWaitTime(){
            // 减去15分钟
            return getSeconds()-15*60;
        }
    }
}
