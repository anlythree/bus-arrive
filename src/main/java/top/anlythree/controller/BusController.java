package top.anlythree.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import top.anlythree.api.RouteService;
import top.anlythree.cache.ACache;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;

import java.util.List;

@RestController
@RequestMapping("/bus")
public class BusController {

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeService;

    @GetMapping("/getBusLocation")
    public String getBusLocation(){
        return null;
    }

    @GetMapping("/getCityList")
    public List<City> getCityList(){
        return ACache.getCityCacheList();
    }

    @GetMapping("/getRouteList")
    public List<Route> getRouteList(){
        return ACache.getRouteCacheList();
    }

    @GetMapping("/getRoute")
    public Route getRouteList(@RequestParam String routeName, @RequestParam String cityName, @RequestParam String startStation){
        return routeService.getRoutByNameAndCityIdAndStartStation(routeName,cityName,startStation);
    }
}
