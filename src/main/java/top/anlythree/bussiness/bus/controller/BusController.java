package top.anlythree.bussiness.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import top.anlythree.api.RouteService;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.AMapRouteServiceImpl;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.StationDTO;
import top.anlythree.cache.ACache;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.utils.TaskUtil;
import top.anlythree.utils.TimeUtil;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bus")
public class BusController {

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeService;

    @Autowired
    @Qualifier(value = "AMapStationServiceImpl")
    private StationService stationService;

    @Autowired
    private BusArriveService busArriveService;

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

    @GetMapping("/calculateTime")
    public String getRouteList(@RequestParam String routeName,
                               @RequestParam String cityName,
                               @RequestParam String district,
                               @RequestParam String startStation,
                               @RequestParam String endStation,
                               @RequestParam String directStation,
                               @RequestParam String prepareSeconds,
                               @RequestParam String arriveTime){
        LocalDateTime arriveLocalTime = TimeUtil.onlyTimeStrToTime(arriveTime);

        StationDTO startStationDto = stationService.getStation(cityName, district, startStation);
        StationDTO endStationDto = stationService.getStation(cityName, district, endStation);

        LocalDateTime startTimeByArriveTime = busArriveService.getStartTimeByArriveTime(cityName, routeName, null, arriveLocalTime,
                startStationDto.getLongitudeAndLatitude(), endStationDto.getLongitudeAndLatitude(), null);
            busArriveService.calculateTimeToGo(cityName, district, routeName,
                    startStation,directStation,
                    Long.parseLong(prepareSeconds),startTimeByArriveTime,arriveLocalTime);
        return TimeUtil.timeToString(startTimeByArriveTime);
    }
}
