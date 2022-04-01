package top.anlythree.api.amapimpl.res;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 高德公交路线规划api返回值
 * @author anlythree
 * @description:
 * @time 2022/3/156:45 下午
 */
@Deprecated
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class AMapBusRouteRes extends AMapResult{

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
<<<<<<< HEAD
         * 出租车价格
=======
         * 总距离
         */
        private String distance;

        /**
         * 打车费用参考
>>>>>>> cd8631ba7ae849dd8453f1d215c9cce17389ab93
         */
        private String taxiCost;

        /**
         * 公交方案列表
         */
        private List<TransitsInfo> transits;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TransitsInfo{
            /**
             * 此换乘方案价格 单位：元，有的时候是空列表
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
                private Walking walking;

                /**
                 * 此路段公交导航信息
                 */
                private BusInfo bus;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Walking{

                    private String origin;

                    private String destination;

                    /**
                     * 距离
                     */
                    private String distance;

                    /**
                     * 用时
                     */
                    private String duration;

                    private List<StepInfo> steps;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class StepInfo{
                        private String instruction;

                        private String road;

                        private String distance;

                        private String duration;

                        private String polyline;

                        private String action;

                        private String assistantAction;
                    }
                }

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

            /**
             * 获取方案所需毫秒数
             * @return
             */
            public Long getSeconds(){
                if(duration == null){
                    return null;
                }
                return Long.parseLong(duration);
            }

            /**
             * 获取路线起始站和结束站的名称
             * @return
             */
            public String[] getStartStationAndEndStation(){
                for (SegmentsInfo segment : this.getSegments()) {
                    for (SegmentsInfo.BusInfo.BusLinesInfo busline : segment.getBus().getBuslines()) {
                        String busName;
                        if(StringUtils.isNotEmpty(busName = busline.getName())){
                            return busName.substring(busName.indexOf("(")+1, busName.lastIndexOf(")")).split("--");
                        }
                    }
                }
                return null;
            }

            /**
             * 获取该路线起点公交站的坐标（步行的终点）
             */
            public String getStartBusStationLal(){
                for (SegmentsInfo segment : this.getSegments()) {
                    if(null != segment.getBus().getBuslines() && segment.getBus().getBuslines().size() == 1){
                        return segment.getWalking().getDestination();
                    }
                }
                return null;
            }

            /**
             * 获取该路线起点公交站的名字（步行的终点）
             */
            public String getStartBusStationName(){
                for (SegmentsInfo segment : this.getSegments()) {
                    if(null != segment.getBus().getBuslines() && segment.getBus().getBuslines().size() == 1){
                        List<SegmentsInfo.Walking.StepInfo> steps = segment.getWalking().getSteps();
                        if(!CollectionUtils.isEmpty(steps)){
                            String assistantAction = steps.get(steps.size() - 1).getAssistantAction().toString();
                            if(assistantAction.contains("到达")){
                                return StringUtils.substringAfter(assistantAction,"到达");
                            }
                        }
                    }
                }
                return null;
            }

        }
    }

    /**
     * 根据公交路线名从高德返回的路径规划中找到合适的行程方案  todo-anlythree
     * @return
     */
    public AMapBusRouteInfo.TransitsInfo getTransitsByRouteName(String routeName){
        for (AMapBusRouteInfo.TransitsInfo transit : this.getRoute().getTransits()) {
            for (AMapBusRouteInfo.TransitsInfo.SegmentsInfo segment : transit.getSegments()) {
                if(null != segment.getBus().getBuslines() && segment.getBus().getBuslines().size() == 1 &&
                        segment.getBus().getBuslines().get(0).getName().contains(routeName)){
                    return transit;
                }
            }
        }
        try {
            log.error("找不到直达公交，routeName:"+routeName+",this:"+new ObjectMapper().writeValueAsString(this));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
