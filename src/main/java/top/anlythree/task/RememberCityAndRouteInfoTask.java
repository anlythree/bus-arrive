package top.anlythree.task;


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
import top.anlythree.cache.ACache;
import top.anlythree.utils.exceptions.AException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Value("#{'${remembor.cityandroute}'.split(',')}")
    private String[] cityAndRouteList;

    @Value("#{spring.profiles.active}")
    private String active;

    @Override
    public void run(ApplicationArguments args) {
        List<String> cityNameList = Arrays.stream(cityAndRouteList).map(f -> f.substring(0, f.indexOf(" "))).collect(Collectors.toList());
        // 缓存笑园城市信息
        cityService.cityList().forEach(ACache::addCity);
        // 缓存高德城市信息
        cityNameList.stream().forEach(cityName->{
            ACache.aMapCityList.put(cityName,aMapRouteService.getCityCodeNoCacheByName(cityName));
        });
        // 缓存笑园公交路线信息
        if("dev".equals(active)){
            return;
        }
        // 只有线上才进行缓存
        for (String cityAndRoute : cityAndRouteList) {
            String[] cityAndRouteCollection = cityAndRoute.split(" ");
            String cityName = cityAndRouteCollection[0];
            String routeName = cityAndRouteCollection[1];
            if (cityAndRouteCollection.length != 2) {
                throw new AException("配置文件中的路线格式有误，关键字位置：\'" + cityAndRoute + "\'");
            }
            xiaoYuanRouteService.cacheRouteByNameAndCityName(cityName, routeName);
        }
    }


}
