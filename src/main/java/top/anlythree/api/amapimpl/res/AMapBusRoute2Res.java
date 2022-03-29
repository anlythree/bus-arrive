package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author anlythree
 * @description: 高德api路径规划2.0返回值
 * @time 2022/3/2915:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapBusRoute2Res extends AMapResult{

    private RouteRes routeRes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteRes{
        private String origin;
        private String destination;
        private List<TransitsRes> transits;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TransitsRes {
            private String distance;
            private String nightflag;

            private CostInfo costInfo;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class CostInfo{
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
     * @return
     */
    public ImportInfo getImportInfo(String routeName){
        for (RouteRes.TransitsRes transit : this.getRouteRes().getTransits()) {
            if(!CollectionUtils.isEmpty(transit.getSegments())){
                for (RouteRes.TransitsRes.SegmentsRes segment : transit.getSegments()) {
                    if(!CollectionUtils.isEmpty(segment.getBus().getBuslines())){
                        for (RouteRes.TransitsRes.SegmentsRes.BusRes.BusLinesRes busline : segment.getBus().getBuslines()) {
                            if(busline.getName().contains(routeName)){
                                String busName;
                                String[] stationName = {null,null};
                                if(StringUtils.isNotEmpty(busName = busline.getName())){
                                    stationName = busName.substring(busName.indexOf("(")+1, busName.lastIndexOf(")")).split("--");
                                }
                                return new ImportInfo(routeName,
                                        Long.parseLong(transit.getCostInfo().getDuration()),
                                        Long.parseLong(transit.getCostInfo().getTransitFee()),
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
        }
        return null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportInfo{
        /**
         * 路线名称
         */
        private String routeName;

        /**
         * 用时（秒）
         */
        private Long seconds;

        /**
         * 费用
         */
        private Long cost;

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

        public ImportInfo(String routeName, Long seconds, Long cost,String startBusStationName, String startBusStationLal, String endBusStationName, String endBusStationLal, String firstBusStationName,String lastBusStationName) {
            this.routeName = routeName;
            this.seconds = seconds;
            this.cost = cost;
            this.startBusStationName = startBusStationName;
            this.startBusStationLal = startBusStationLal;
            this.endBusStationName = endBusStationName;
            this.endBusStationLal = endBusStationLal;
            this.firstBusStationName = firstBusStationName;
            this.lastBusStationName = lastBusStationName;
        }
    }
}
