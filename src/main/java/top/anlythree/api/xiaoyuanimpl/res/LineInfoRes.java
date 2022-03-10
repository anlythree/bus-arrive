package top.anlythree.api.xiaoyuanimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/103:58 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineInfoRes {
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
