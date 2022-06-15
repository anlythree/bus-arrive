package top.anlythree.cache;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.BusArriveResultDto;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.utils.RedisUtil;

import java.util.*;

/**
 * 缓存内容如下：
 * locationCacheList
 * xiaoYuanCityList
 * aMapCityList
 * xiaoYuanRouteCacheList
 * aMapCityMap(key:城市名称，value:城市代码)
 *
 * @author anlythree
 * @description:
 * @time 2022/3/13:04 下午
 */
@Slf4j
@AllArgsConstructor
@Component
public class ACache {

    private static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        ACache.redisUtil = redisUtil;
    }

    /**
     * 根据城市名称获取缓存高德城市code
     * @param cityName
     * @return
     */
    public static String getMapCityCodeByName(String cityName){
        return redisUtil.hget("aMapCityMap",cityName,String.class);
    }

    /**
     * 添加高德城市缓存信息
     * @param cityName
     * @param cityCode
     * @return
     */
    public static void addAMapCityCodeByName(String cityName,String cityCode){
        redisUtil.hset("aMapCityMap",cityName,cityCode);
    }


    /**
     * 获取所有缓存的地址信息
     * @return
     */
    public static List<LocationDTO> getLocationCacheList() {
        return redisUtil.lGet("locationCacheList",LocationDTO.class);
    }

    /**
     * 通过关键字获取地址
     * @param keyWord
     * @return
     */
    public static LocationDTO getLocationByKeyWord(String keyWord){
        for (LocationDTO locationDTO : getLocationCacheList()) {
            if(StringUtils.isNotEmpty(locationDTO.getStationFullName()) &&
                    locationDTO.getStationFullName().contains(keyWord)){
                return locationDTO;
            }
        }
        return null;
    }

    /**
     * 添加城市
     * @param city
     */
    public static void addXiaoYuanCity(XiaoYuanCityDTO city) {
        List<XiaoYuanCityDTO> xiaoYuanCityList = redisUtil.lGet("xiaoYuanCityList", XiaoYuanCityDTO.class);
        if(CollectionUtils.isEmpty(xiaoYuanCityList)){
            return;
        }
        for (int i = 0; i < xiaoYuanCityList.size(); i++) {
            XiaoYuanCityDTO cityItem = xiaoYuanCityList.get(i);
            // 缓存中有重复的就覆盖掉原信息
            if (Objects.equals(cityItem.getId(), city.getId())) {
                // 更新城市名称
                cityItem.setName(city.getName());

            }else if (Objects.equals(cityItem.getName(), city.getName())) {
                // 更新城市id
                cityItem.setId(city.getId());
            }
            redisUtil.lSetIndex("xiaoYuanCityList",i,cityItem);
        }
        // 没有就添加
        redisUtil.lSetList("xiaoYuanCityList", Lists.newArrayList(city));
    }

    /**
     * 获取
     * @return
     */
    public static List<XiaoYuanCityDTO> getXiaoYuanCityList() {
        return redisUtil.lGet("xiaoYuanCityList",XiaoYuanCityDTO.class);
    }


    /**
     * 添加路线
     * @param route
     */
    public static void addXiaoYuanRoute(XiaoYuanRouteDTO route) {
        List<XiaoYuanRouteDTO> xiaoYuanRouteCacheList = redisUtil.lGet("xiaoYuanRouteCacheList", XiaoYuanRouteDTO.class);
        if(!CollectionUtils.isEmpty(xiaoYuanRouteCacheList)){
            for (int i = 0; i < xiaoYuanRouteCacheList.size(); i++) {
                XiaoYuanRouteDTO routeItem = xiaoYuanRouteCacheList.get(i);
                // 缓存中有重复的就覆盖掉原信息
                if (Objects.equals(routeItem.getRouteName(), route.getRouteName()) &&
                        Objects.equals(routeItem.getStartStation(),route.getStartStation()) &&
                        Objects.equals(routeItem.getCityId(),route.getCityId())) {
                    // 更新路线信息
                    BeanUtils.copyProperties(route,routeItem);
                    redisUtil.lSetIndex("xiaoYuanRouteCacheList",i,routeItem);
                    return;
                }
            }
        }
        // 没有就添加
        redisUtil.lSetList("xiaoYuanRouteCacheList",Lists.newArrayList(route));
    }

    /**
     * 获取缓存中的路线信息
     * @return
     */
    public static List<XiaoYuanRouteDTO> getXiaoYuanRouteCacheList(){
        return redisUtil.lGet("xiaoYuanRouteCacheList",XiaoYuanRouteDTO.class);
    }

    /**
     * 添加地址
     * @param locationDTO
     */
    public static void addLocation(LocationDTO locationDTO){
        List<LocationDTO> locationCacheList = Objects.requireNonNull(redisUtil.lGet("locationCacheList",LocationDTO.class));
        for (int i = 0; i < locationCacheList.size(); i++) {
            LocationDTO locationDtoItem = locationCacheList.get(i);
            // 缓存中有重复的就覆盖掉原信息
            if(locationDtoItem.eqauls(locationDTO)){
                // 更新站点信息
                BeanUtils.copyProperties(locationDTO,locationDtoItem);
                redisUtil.lSetIndex("locationCacheList",i,locationDtoItem);
                return;
            }
        }
        // 没有就添加
        redisUtil.lSetList("locationCacheList", Lists.newArrayList(locationDTO));
    }


    /**
     * 添加结果
     * @param key
     * @param result
     */
    public static void addResult(String key, BusArriveResultDto result){
        log.info("缓存计算结果:{key:"+key+",result:"+result+"}");
        // 过期时间设置为一天
        // key: 出发地点-目的地-到达时间 value: 出发时间
        redisUtil.set(key,result,86400);
    }

    /**
     * 获取缓存中的计算结果
     * @param key 出发地点-目的地-到达时间 value: 出发时间
     * @return
     */
    public static BusArriveResultDto getResult(String key){
        return redisUtil.get(key,BusArriveResultDto.class);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CityAndRoute{

        private String cityName;

        private String routeName;
    }

}
