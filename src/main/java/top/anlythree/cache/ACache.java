package top.anlythree.cache;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    public static Map<String,String> aMapCityMap = new HashMap<>();

    static {
        List<LocationDTO> locationCacheList = new ArrayList<>(16);
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
        locationCacheList.add(new LocationDTO("中国","浙江省","杭州市","余杭区","星创新里程","119.955084,30.259144"));
        if(!redisUtil.hasKey("locationCacheList")){
            redisUtil.lSet("locationCacheList",locationCacheList);
        }else {
            // 缓存中有 locationCacheList,那就循环把上边的list放到缓存中
            locationCacheList.stream().forEach(ACache::addLocation);
        }

    }

    /**
     * 添加城市
     * @param city
     */
    public static void addCity(XiaoYuanCityDTO city) {
        List<XiaoYuanCityDTO> xiaoYuanCityList = Objects.requireNonNull(redisUtil.lGet("xiaoYuanCityList", XiaoYuanCityDTO.class));
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
        redisUtil.lSetAddList("xiaoYuanCityList",Lists.newArrayList(city));
    }

    /**
     * 添加路线
     * @param route
     */
    public static void addRoute(XiaoYuanRouteDTO route) {
        List<XiaoYuanRouteDTO> xiaoYuanRouteCacheList = Objects.requireNonNull(redisUtil.lGet("xiaoYuanRouteCacheList", XiaoYuanRouteDTO.class));
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
        // 没有就添加
        redisUtil.lSetAddList("xiaoYuanRouteCacheList",Lists.newArrayList(route));
    }

    /**
     * 添加地址
     * @param locationDTO
     */
    public static void addLocation(LocationDTO locationDTO){
        List<LocationDTO> locationCacheList = redisUtil.lGet("locationCacheList",LocationDTO.class);
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
        redisUtil.lSetAddList("locationCacheList", Lists.newArrayList(locationDTO));
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


    public static List<LocationDTO> getLocationCacheList() {
        return redisUtil.lGet("locationCacheList",LocationDTO.class);
    }

}
