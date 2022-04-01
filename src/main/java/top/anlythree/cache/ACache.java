package top.anlythree.cache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.BusArriveResultDto;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.bussiness.dto.StationDTO;

import java.util.*;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/13:04 下午
 */
@Slf4j
@AllArgsConstructor
public class ACache {

    @Value("${xiaoyuan.url}")
    private static String xiaoyuanUrl;

    private static List<XiaoYuanCityDTO> cityCacheList = new ArrayList<>();

    /**
     * 开放变量
     */
    public static Map<String,String> aMapCityList = new HashMap<>();

    private static List<XiaoYuanRouteDTO> routeCacheList = new ArrayList<>();

    private static List<LocationDTO> locationCacheList = new ArrayList<>(8);

    /**
     * key: 出发地点-目的地-到达时间 value: 出发时间
     */
    private static Map<String, BusArriveResultDto> keyToResultMap = new HashMap<>();

    static {
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","阿里巴巴A5门","120.026525,30.281248"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","阿里巴巴西溪园区(蔡家阁)公交站","120.024556,30.280789"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","五常大道联胜路口公交站","120.031884,30.243976"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","西湖体育馆公交站","120.13072,30.267691"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","爱橙街贺翠路口公交站","120.022539,30.274492"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","永兴村公交站","120.020833,30.278499"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","何母桥公交站","120.022676,30.273249"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","梦想小镇公交站","120.004066,30.294739"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","海创园5号楼","120.018439,30.283251"));
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","丰岭路追梦家公寓","120.033852,30.242918"));
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

    public static void addLocation(LocationDTO locationDTO){
        for (LocationDTO locationDtoItem : locationCacheList) {
            if(locationDtoItem.eqauls(locationDTO)){
                // 更新站点信息
                BeanUtils.copyProperties(locationDTO,locationDtoItem);
                return;
            }
        }
        locationCacheList.add(locationDTO);
    }
    public static LocationDTO getLocationByKeyWord(String keyWord){
        for (LocationDTO locationDTO : getLocationCacheList()) {
            if(StringUtils.isNotEmpty(locationDTO.getStationFullName()) &&
                    locationDTO.getStationFullName().contains(keyWord)){
                return locationDTO;
            }
        }
        return null;
    }

    public static void addResult(String key, BusArriveResultDto result){
        log.info("缓存计算结果:{key:"+key+",result:"+result+"}");
        keyToResultMap.put(key,result);
    }

    /**
     * 获取缓存中的计算结果
     * @param key
     * @return
     */
    public static BusArriveResultDto getResult(String key){
        return keyToResultMap.get(key);
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

    public static List<LocationDTO> getLocationCacheList() {
        return locationCacheList;
    }

    public static void setLocationCacheList(List<LocationDTO> locationCacheList) {
        ACache.locationCacheList = locationCacheList;
    }
}
