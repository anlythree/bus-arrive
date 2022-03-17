package top.anlythree.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.StationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/13:04 下午
 */
@Data
@AllArgsConstructor
public class ACache {

    @Value("${xiaoyuan.url}")
    private static String xiaoyuanUrl;

    private static List<XiaoYuanCityDTO> cityCacheList = new ArrayList<>();

    private static List<XiaoYuanRouteDTO> routeCacheList = new ArrayList<>();

    private static List<StationDTO> stationCacheList = new ArrayList<>();

    static {
        // 添加阿里巴巴A5门坐标位置
        stationCacheList.add(new StationDTO("中国","浙江省","杭州市","余杭区","阿里巴巴A5门","120.026525,30.281248"));
        stationCacheList.add(new StationDTO("中国","浙江省","杭州市","余杭区","阿里巴巴西溪园区(蔡家阁)公交站","120.024556,30.280789"));
        stationCacheList.add(new StationDTO("中国","浙江省","杭州市","余杭区","五常大道联胜路口公交站","120.031884,30.243976"));
    }

    public static void addCity(XiaoYuanCityDTO city) {
        for (XiaoYuanCityDTO cityItem : cityCacheList) {
            // 缓存中有重复的就覆盖掉原信息
            if (Objects.equals(cityItem.getId(), city.getId())) {
                // 更新城市名称
                cityItem.setName(city.getName());
                return;
            }
            if (Objects.equals(cityItem.getName(), city.getName())) {
                // 更新城市id
                cityItem.setId(city.getId());
                return;
            }
        }
        cityCacheList.add(city);
    }

    public static void addRoute(XiaoYuanRouteDTO route) {
        for (XiaoYuanRouteDTO routeItem : routeCacheList) {
            // 缓存中有重复的就覆盖掉原信息
            if (Objects.equals(routeItem.getRouteName(), route.getRouteName()) &&
            Objects.equals(routeItem.getStartStation(),route.getStartStation()) &&
            Objects.equals(routeItem.getCityId(),route.getCityId())) {
                // 更新路线信息
                BeanUtils.copyProperties(route,routeItem);
                return;
            }
        }
        routeCacheList.add(route);
    }

    public static void addStation(StationDTO stationDTO){
        for (StationDTO stationDtoItem : stationCacheList) {
            if(stationDtoItem.eqauls(stationDTO)){
                // 更新站点信息
                BeanUtils.copyProperties(stationDTO,stationDtoItem);
                return;
            }
        }
        stationCacheList.add(stationDTO);
    }

    public static List<XiaoYuanCityDTO> getCityCacheList() {
        return cityCacheList;
    }

    public static void setCityCacheList(List<XiaoYuanCityDTO> cityCacheList) {
        ACache.cityCacheList = cityCacheList;
    }

    public static List<XiaoYuanRouteDTO> getRouteCacheList() {
        return routeCacheList;
    }

    public static void setRouteCacheList(List<XiaoYuanRouteDTO> routeCacheList) {
        ACache.routeCacheList = routeCacheList;
    }

    public static String getXiaoyuanUrl() {
        return xiaoyuanUrl;
    }
}
