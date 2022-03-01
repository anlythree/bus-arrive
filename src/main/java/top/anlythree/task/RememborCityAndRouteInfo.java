package top.anlythree.task;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.anlythree.api.CityService;
import top.anlythree.cache.ACache;
import top.anlythree.dto.City;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/12:11 下午
 */
@Component
public class RememborCityAndRouteInfo implements ApplicationRunner {

    @Autowired
    private CityService cityService;

    @Value("${remembor.cityandroute}")
    private List<String> cityAndRoute;

    @Override
    public void run(ApplicationArguments args) {
        // 缓存城市
        List<String> cityNameList = cityAndRoute.stream().map(f -> f.substring(f.indexOf(" "))).collect(Collectors.toList());
        List<City> cities = cityService.cityList();
        for (City city : cities) {
            if(cityNameList.contains(city.getName())){
                ACache.addCity(city);
            }
        }
        // 缓存路线

    }
}
