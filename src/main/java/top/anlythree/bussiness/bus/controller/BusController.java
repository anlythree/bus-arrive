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
    public String getRouteList(@RequestParam(required = true) String cityName,
                               @RequestParam(required = true) String district,
                               @RequestParam(required = true) String routeName,
                               @RequestParam(required = true) String startStation,
                               @RequestParam(required = true) String endStation,
                               @RequestParam(required = true) String prepareMinutes,
                               @RequestParam(required = true) String arriveTime){
        LocalDateTime arriveLocalTime = TimeUtil.onlyTimeStrToTime(arriveTime);

        StationDTO startStationDto = stationService.getStation(cityName, district, startStation);
        StationDTO endStationDto = stationService.getStation(cityName, district, endStation);
        XiaoYuanRouteDTO routeByNameAndCityAndRideStartAndRideEnd = routeService.getRouteByNameAndCityAndRideStartAndRideEnd(cityName, routeName, startStationDto.getLongitudeAndLatitude(), endStationDto.getLongitudeAndLatitude());
        LocalDateTime startTimeByArriveTime = busArriveService.getStartTimeByArriveTime(cityName, routeName, null, arriveLocalTime,
                startStationDto.getLongitudeAndLatitude(), endStationDto.getLongitudeAndLatitude(), null);
            busArriveService.calculateTimeToGo(cityName, district, routeName,
                    startStation,routeByNameAndCityAndRideStartAndRideEnd.getEndStation(),
                    Long.parseLong(prepareMinutes)*60,startTimeByArriveTime,arriveLocalTime);
        return TimeUtil.timeToString(startTimeByArriveTime);
    }

    @GetMapping("/whenLeave")
    public String whenLeave(@RequestParam(required = true) String cityName,
                               @RequestParam(required = true) String district,
                               @RequestParam(required = true) String routeName,
                               @RequestParam(required = true) String startStation,
                               @RequestParam(required = true) String endStation,
                               @RequestParam(required = true) String arriveTime){
        LocalDateTime arriveLocalTime = TimeUtil.onlyTimeStrToTime(arriveTime);
        StationDTO startStationDto = stationService.getStation(cityName, district, startStation);
        StationDTO arriveStationDto = stationService.getStation(cityName, district, endStation);
        LocalDateTime startTimeByArriveTime = busArriveService.getStartTimeByArriveTime(cityName, routeName,
                null, arriveLocalTime,
                startStationDto.getLongitudeAndLatitude(), arriveStationDto.getLongitudeAndLatitude(),null);
        return TimeUtil.timeToString(startTimeByArriveTime);
    }
}
