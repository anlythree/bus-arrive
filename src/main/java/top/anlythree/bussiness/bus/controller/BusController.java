package top.anlythree.bussiness.bus.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import top.anlythree.api.RouteService;
import top.anlythree.api.StationService;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.BusArriveResultDto;
import top.anlythree.bussiness.dto.LocationDTO;
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
    @Qualifier(value = "AMapRouteServiceImpl")
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
    public String calculateTime(String cityName,
                               String routeName,
                               String startStationName,
                               String endStationName,
                               String prepareMinutes,
                               String arriveTime) {
        LocalDateTime arriveLocalTime = TimeUtil.onlyTimeStrToTime(arriveTime);
        LocationDTO startLocationByName = stationService.getLocationByName(cityName, startStationName);
        LocationDTO endLocationByName = stationService.getLocationByName(cityName, endStationName);
        // 计算什么时候可以获取计算结果 && 延时在取结果之前计算何时出发
        LocalDateTime startTimeByArriveTime = busArriveService.getCalculateTimeAndCalculateDelay(
                cityName,
                routeName,
                startLocationByName,
                endLocationByName,
                prepareMinutes,
                arriveLocalTime
                );
        // 生成查询结果key
        return TimeUtil.timeToString(startTimeByArriveTime);
    }

    @GetMapping("/getResult")
    public BusArriveResultDto getResult(@RequestParam(required = true) String key) {
        return ACache.getResult(key);
    }
}
