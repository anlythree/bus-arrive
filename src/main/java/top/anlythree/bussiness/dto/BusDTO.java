package top.anlythree.bussiness.dto;

import io.swagger.models.auth.In;
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

    private String cityName;

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
    private Integer disStat;

    /**
     * 返回经,纬
     * @return
     */
    public String getLocation(){
        return longing+","+lating;
    }

    /**
     * 获取当前公交即将到达站点
     * @return
     */
    public Integer getStationNum(){
        return Integer.valueOf(disStat);
    }

}
