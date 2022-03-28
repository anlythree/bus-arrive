package top.anlythree.bussiness.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import top.anlythree.api.RouteService;
import top.anlythree.api.StationService;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.BusArriveResultDto;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.cache.ACache;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.utils.MD5Util;
import top.anlythree.utils.TimeUtil;
import top.anlythree.utils.exceptions.AException;

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
                               String startLocationName,
                               String endLocationName,
                               String prepareMinutes,
                               String arriveTime) {
        LocalDateTime arriveLocalTime = TimeUtil.onlyTimeStrToTime(arriveTime);
        LocationDTO startLocationByName = stationService.getLocationByName(cityName, startLocationName);
        LocationDTO endLocationByName = stationService.getLocationByName(cityName, endLocationName);
        // 生成本次结果对应的key
        String key = MD5Util.getMd5(startLocationName + "-" + endLocationName + "-" + TimeUtil.timeToString(arriveLocalTime));
        // 计算什么时候可以获取计算结果 && 延时在取结果之前计算何时出发
        LocalDateTime startTimeByArriveTime = busArriveService.getCalculateTimeAndCalculateDelay(
                cityName,
                routeName,
                startLocationByName,
                endLocationByName,
                prepareMinutes,
                arriveLocalTime,
                key
                );
        if(TimeUtil.timeInterval(startTimeByArriveTime).isNegative()){
            // 可能计算出来的时间比当前的时间还要早，那说明已经错过目标公交车的发车时间。
            // 那么这时后台的任务线程已经开始计算时间。所以可以直接按当前时间后的10秒后请求结果。
            startTimeByArriveTime = LocalDateTime.now();
        }
        // 给后台留10秒计算时间，防止请求结果时后台还未计算出结果
        return "getTime:"+TimeUtil.timeToString(startTimeByArriveTime.plusSeconds(10))+"key:"+key;
    }


    @GetMapping("/getResult")
    public String getResult(@RequestParam(required = true) String key) {
        BusArriveResultDto result = ACache.getResult(key);
        if(null == result){
            throw new AException("未查询到结果");
        }
        return result.getLeaveStartLocationTime();
    }
}
