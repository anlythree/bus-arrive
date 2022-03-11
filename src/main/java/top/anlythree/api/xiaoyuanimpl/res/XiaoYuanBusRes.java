package top.anlythree.api.xiaoyuanimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/95:13 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoYuanBusRes extends XiaoYuanResult{

    private ReturlInfoRes returlList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ReturlInfoRes {

        private LineInfoRes lineinfo;

        private List<StationInfoRes> stations;

        private List<BusInfoRes> buses;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LineInfoRes {
        /**
         *  路线名称
         */
        private String busStaname;
        /**
         * 起始站
         */
        private String staSta;
        /**
         * 终点站
         */
        private String endSta;
        /**
         * 首班车时间
         */
        private String firTime;
        /**
         * 末班车时间
         */
        private String endTime;
        /**
         * 票价
         */
        private String busMoney;
        /**
         * 反向路线ID
         */
        private String otherLineid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class StationInfoRes {

        private String busStaname;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class BusInfoRes {
        /**
         * 纬度
         */
        private String lating;
        /**
         * 经度
         */
        private String longing;
        /**
         * 距离（米）
         */
        private String distance;
        /**
         * 距第几个站点（如：2，距离第2个站还有distance米，distance是0为已到站！）
         */
        private String disStat;
    }


}



