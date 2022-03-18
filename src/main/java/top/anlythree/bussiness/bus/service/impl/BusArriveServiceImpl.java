package top.anlythree.bussiness.bus.service.impl;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import top.anlythree.api.BusService;
import top.anlythree.api.RouteService;
import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.utils.TimeUtils;
import top.anlythree.utils.exceptions.AException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/1811:19 上午
 */
public class BusArriveServiceImpl implements BusArriveService {

    @Autowired
    private BusService busService;

    @Autowired
    @Qualifier(value = "AMapRouteServiceImpl")
    private RouteService routeServiceAMapImpl;

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeServiceXiaoYuanImpl;

    @Override
    public LocalDateTime getStartTimeByArriveTime(String cityName, String busName,
                                                  LocalDateTime startTime, LocalDateTime arriveTime,
                                                  String startLocation, String endLocation) {
        if(null == startTime){
            startTime = LocalDateTime.now();
        }
        // 路径规划
        AMapBusRouteTimeRes.AMapBusRouteInfo.TransitsInfo secondsByBusAndLocation =
                routeServiceAMapImpl.getSecondsByBusAndLocation(cityName, busName, startLocation, endLocation, TimeUtils.timeToString(startTime));
        long secondsDifferenceLong = Duration.between(startTime.plusSeconds(secondsByBusAndLocation.getSeconds()), arriveTime).getSeconds();
        if(Math.abs(secondsDifferenceLong) < 300){
            return startTime;
        }else {
            return getStartTimeByArriveTime(cityName,busName,
                    startTime.plusSeconds(secondsDifferenceLong/2),arriveTime,
                    startLocation,endLocation);
        }
    }
}
