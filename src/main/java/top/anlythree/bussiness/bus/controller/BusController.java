package top.anlythree.bussiness.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import top.anlythree.api.RouteService;
import top.anlythree.cache.ACache;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;

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
    public List<XiaoYuanCityDTO> getCityList(){
        return ACache.getCityCacheList();
    }

    @GetMapping("/getRouteList")
    public List<XiaoYuanRouteDTO> getRouteList(){
        return ACache.getRouteCacheList();
    }

    @GetMapping("/getRoute")
    public XiaoYuanRouteDTO getRouteList(@RequestParam String routeName, @RequestParam String cityName, @RequestParam String startStation){
        return routeService.getRouteByNameAndCityIdAndStartStation(routeName,cityName,startStation);
    }
}
