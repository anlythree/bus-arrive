package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 步行路线信息
 * @author anlythree
 * @description:
 * @time 2022/3/2315:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapWalkRouteTimeRes extends AMapResult{

    private Route route;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Route{
        /**
         * 起点坐标
         */
        private String origin;

        /**
         * 终点坐标
         */
        private String destination;

        private List<Path> paths;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Path{

            /**
             * 距离
             */
            private String distance;

            /**
             * 用时
             */
            private String duration;

            /**
             * 具体怎么走的信息
             */
            private Object steps;

            /**
             * 获取方案所需秒数
             * @return
             */
            public Long getSeconds(){
                if(duration == null){
                    return null;
                }
                // 预留5分钟
                return Long.parseLong(duration)+60*5;
            }
        }
    }
}
