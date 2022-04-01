package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author anlythree
 * @description: 高德api返回的城市返回值
 * @time 2022/3/2914:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapCityRes extends AMapResult{

    /**
     * 建议
     */
    private Object suggestion;

    /**
     * 行政区列表
     */
    private List<DistrictsInfo> districts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistrictsInfo{
        /**
         * 城市编码
         */
        private String citycode;

        /**
         * 区域编码
         */
        private String adcode;

        /**
         * 名称
         */
        private String name;

        /**
         * 区域中心点
         */
        private String center;

        /**
         * 级别
         */
        private String level;

        /**
         * 下级行政区列表，包含district元素
         */
        private List<DistrictsInfo> districts;
    }

    /**
     * 获取返回结果中的城市编码
     * @return
     */
    public String getCityCode(){
        Assert.isTrue(!CollectionUtils.isEmpty(getDistricts()),"未查询到城市信息");
        return getDistricts().get(0).getCitycode();
    }
}
