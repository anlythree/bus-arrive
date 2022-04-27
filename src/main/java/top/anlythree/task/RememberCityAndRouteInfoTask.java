package top.anlythree.task;


import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.cache.ACache;
import top.anlythree.utils.RedisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/12:11 下午
 */
@Slf4j
@Data
@Component
public class RememberCityAndRouteInfoTask implements ApplicationRunner {

    @Autowired
    @Qualifier(value = "xiaoYuanCityServiceImpl")
    private CityService cityService;

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService xiaoYuanRouteService;

    @Autowired
    @Qualifier(value = "AMapRouteServiceImpl")
    private RouteService aMapRouteService;

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args) {
        // 缓存地址信息
        cacheLocationInfo();
        // 缓存笑园城市信息
        cacheXiaoYuanCityInfo();
        // 缓存高德城市信息
        cacheAMapCityInfo();
        // 缓存笑园公交路线信息
        cacheXiaoYuanRouteInfo();
    }

    /**
     * 缓存笑园路线信息
     */
    private void cacheXiaoYuanRouteInfo() {
        Lists.newArrayList(new ACache.CityAndRoute("杭州", "353"),
                        new ACache.CityAndRoute("杭州", "415"))
                .forEach(xiaoYuanRouteService::cacheRouteByNameAndCityName);
    }

    /**
     * 缓存高德城市信息
     */
    private void cacheAMapCityInfo() {
        Lists.newArrayList("杭州")
                .stream().forEach(cityName ->
                        ACache.addAMapCityCodeByName(cityName, aMapRouteService.getCityCodeNoCacheByName(cityName))
                );
    }

    /**
     * 缓存笑园城市信息
     */
    private void cacheXiaoYuanCityInfo() {
        cityService.cityList().forEach(ACache::addXiaoYuanCity);
    }

    /**
     * 缓存地址信息
     */
    public void cacheLocationInfo() {
        if (!redisUtil.hasKey("locationCacheList")) {
            List<LocationDTO> locationCacheList = new ArrayList<>(16);
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "阿里巴巴A5门", "120.026525,30.281248"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "阿里巴巴西溪园区(蔡家阁)公交站", "120.024556,30.280789"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "五常大道联胜路口公交站", "120.031884,30.243976"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "西湖体育馆公交站", "120.13072,30.267691"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "爱橙街贺翠路口公交站", "120.022539,30.274492"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "永兴村公交站", "120.020833,30.278499"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "何母桥公交站", "120.022676,30.273249"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "梦想小镇公交站", "120.004066,30.294739"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "海创园5号楼", "120.018439,30.283251"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "丰岭路追梦家公寓", "120.033852,30.242918"));
            locationCacheList.add(new LocationDTO("中国", "浙江省", "杭州市", "余杭区", "星创新里程", "119.955084,30.259144"));
            redisUtil.lSet("locationCacheList", locationCacheList);
        }

    }

}
