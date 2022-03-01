package top.anlythree.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;
import top.anlythree.utils.exceptions.AException;

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

    private static String xiaoyuanUrl = "http://api.dwmm136.cn/z_busapi/BusApi.php";

    private static List<City> cityCacheList = new ArrayList<>();

    private static List<Route> routeCacheList = new ArrayList<>();

    public static void addCity(City city) {
        for (City cityItem : cityCacheList) {
            // 判断有重复的就覆盖掉原信息
            if (Objects.equals(cityItem.getId(), city.getId())) {
                // 更新城市名称
                return;
            }
            if (Objects.equals(cityItem.getName(), city.getName())) {
                // 更新城市id
                cityItem.setId(city.getId());
            }
        }
        cityCacheList.add(city);
    }

    public static void addRoute(Route route) {
        routeCacheList.add(route);
    }

    public static List<City> getCityCacheList() {
        return cityCacheList;
    }

    public static void setCityCacheList(List<City> cityCacheList) {
        ACache.cityCacheList = cityCacheList;
    }

    public static List<Route> getRouteCacheList() {
        return routeCacheList;
    }

    public static void setRouteCacheList(List<Route> routeCacheList) {
        ACache.routeCacheList = routeCacheList;
    }

    public static String getXiaoyuanUrl() {
        return xiaoyuanUrl;
    }
}
