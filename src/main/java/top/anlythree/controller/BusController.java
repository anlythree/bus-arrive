package top.anlythree.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anlythree.cache.ACache;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;

import java.util.List;

@RestController
@RequestMapping("/bus")
public class BusController {

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

}
