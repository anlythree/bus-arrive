package top.anlythree.bussiness.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import top.anlythree.api.RouteService;
import top.anlythree.api.StationService;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.StationDTO;
import top.anlythree.cache.ACache;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.utils.MD5Util;
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
    public String getBusLocation() {
        return null;
    }

    @GetMapping("/getCityList")
    public List<XiaoYuanCityDTO> getCityList() {
        return ACache.getCityCacheList();
    }

    @GetMapping("/getRouteList")
    public List<XiaoYuanRouteDTO> getRouteList() {
        return ACache.getRouteCacheList();
    }

    @GetMapping("/calculateTime")
    public String getRouteList(@RequestParam(required = true) String cityName,
                               @RequestParam(required = true) String district,
                               @RequestParam(required = true) String routeName,
                               @RequestParam(required = true) String startStationName,
                               @RequestParam(required = true) String endStationName,
                               @RequestParam(required = true) String prepareMinutes,
                               @RequestParam(required = true) String arriveTime) {
        LocalDateTime arriveLocalTime = TimeUtil.onlyTimeStrToTime(arriveTime);

        StationDTO startStationDto = stationService.getStation(cityName, district, startStationName);
        StationDTO endStationDto = stationService.getStation(cityName, district, endStationName);
        XiaoYuanRouteDTO routeByNameAndCityAndRideStartAndRideEnd = routeService.getRouteByNameAndCityAndRideStartAndRideEnd(cityName, routeName, startStationDto.getLongitudeAndLatitude(), endStationDto.getLongitudeAndLatitude());
        // 公交起始站
        StationDTO routeFirstStationDto = stationService.getStation(cityName, district, routeByNameAndCityAndRideStartAndRideEnd.getStartStation());
        LocalDateTime startTimeByArriveTime = busArriveService.getStartTimeByArriveTime(cityName, routeName,
                null, arriveLocalTime,
                routeFirstStationDto.getLongitudeAndLatitude(), endStationDto.getLongitudeAndLatitude(),
                null);
        // 生成查询结果key
        String key = MD5Util.getMd5(cityName + "-" + routeName + "-" + startStationName + "-" + TimeUtil.timeToString(arriveLocalTime));
        busArriveService.calculateTimeToGo(cityName, district, routeName,
                startStationName, routeByNameAndCityAndRideStartAndRideEnd.getEndStation(),
                Long.parseLong(prepareMinutes) * 60, startTimeByArriveTime, arriveLocalTime,key);
        return "获取结果时间:{"+TimeUtil.timeToString(startTimeByArriveTime)+"},key="+key;
    }

    @GetMapping("/getResult")
    public String getResult(@RequestParam(required = true) String cityName,
                            @RequestParam(required = true) String district,
                            @RequestParam(required = true) String routeName,
                            @RequestParam(required = true) String startStation,
                            @RequestParam(required = true) String endStation,
                            @RequestParam(required = true) String arriveTime) {
        LocalDateTime arriveLocalTime = TimeUtil.onlyTimeStrToTime(arriveTime);
        StationDTO startStationDto = stationService.getStation(cityName, district, startStation);
        StationDTO arriveStationDto = stationService.getStation(cityName, district, endStation);
        LocalDateTime startTimeByArriveTime = busArriveService.getStartTimeByArriveTime(cityName, routeName,
                null, arriveLocalTime,
                startStationDto.getLongitudeAndLatitude(), arriveStationDto.getLongitudeAndLatitude(), null);
        return TimeUtil.timeToString(startTimeByArriveTime);
    }
}
