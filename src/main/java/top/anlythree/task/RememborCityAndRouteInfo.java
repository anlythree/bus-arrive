package top.anlythree.task;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.anlythree.api.CityService;
import top.anlythree.cache.ACache;
import top.anlythree.dto.City;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/12:11 下午
 */
@Data
@Component
public class RememborCityAndRouteInfo implements ApplicationRunner {

    @Autowired
    @Qualifier(value = "xiaoYuanCityServiceImpl")
    private CityService cityService;

    @Value("#{'${remembor.cityandroute}'.split(',')}")
    private String[] cityAndRoute;

    @Override
    public void run(ApplicationArguments args) {
        // 缓存城市
        List<String> cityNameList = Arrays.stream(cityAndRoute).map(f -> f.substring(0,f.indexOf(" "))).collect(Collectors.toList());
        List<City> cities = cityService.cityList();
        for (City city : cities) {
            if(cityNameList.contains(city.getName())){
                ACache.addCity(city);
            }
        }
        // 缓存路线

    }
}
