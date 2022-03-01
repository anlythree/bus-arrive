package top.anlythree.task;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.cache.ACache;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;
import top.anlythree.utils.exceptions.AException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/12:11 下午
 */
@Slf4j
@Data
@Component
public class RememborCityAndRouteInfo implements ApplicationRunner {

    @Autowired
    @Qualifier(value = "xiaoYuanCityServiceImpl")
    private CityService cityService;

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeService;

    @Value("#{'${remembor.cityandroute}'.split(',')}")
    private String[] cityAndRouteList;

    @Override
    public void run(ApplicationArguments args) {
        // 缓存城市
        List<String> cityNameList = Arrays.stream(cityAndRouteList).map(f -> f.substring(0, f.indexOf(" "))).collect(Collectors.toList());
        List<City> cities = cityService.cityList();
        for (City city : cities) {
            if (cityNameList.contains(city.getName())) {
                ACache.addCity(city);
            }
        }
        // 缓存路线
        for (String cityAndRoute : cityAndRouteList) {
            String[] cityAndRouteCollection = cityAndRoute.split(" ");
            String cityName = cityAndRouteCollection[0];
            String routeName = cityAndRouteCollection[1];
            if (cityAndRouteCollection.length != 2) {
                throw new AException("配置文件中的路线格式有误，关键字位置：\'" + cityAndRoute + "\'");
            }
            routeService.cacheRouteByNameAndCityName(cityName, routeName);
        }
    }


}
