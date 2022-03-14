package top.anlythree.bussiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/95:12 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusDTO {

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
