package top.anlythree.bussiness.bus.service.impl;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/1811:19 上午
 */
@Slf4j
@Service
public class BusArriveServiceImpl implements BusArriveService {

    @Autowired
    private BusService busService;

    @Autowired
    @Qualifier(value = "AMapRouteServiceImpl")
    private RouteService routeServiceAMapImpl;

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeServiceXiaoYuanImpl;

    @Value("${bus-arrive.default-difference-seconds}")
    private Long defaultDifferenceSeconds;

    @Override
    public LocalDateTime getStartTimeByArriveTime(String cityName, String routeName,
                                                  LocalDateTime startTime, LocalDateTime arriveTime,
                                                  String startLocation, String endLocation,
                                                  Long allowDifferenceSeconds) {
        if (null == allowDifferenceSeconds) {
            allowDifferenceSeconds = defaultDifferenceSeconds;
        }
        if (null == startTime) {
            // 如果没填预计出发时间，则选择到达时间前的一个小时作为预计出发时间
            startTime = arriveTime.minusHours(1);
        }
        // 路径规划
        AMapBusRouteTimeRes.AMapBusRouteInfo.TransitsInfo secondsByBusAndLocation =
                routeServiceAMapImpl.getSecondsByBusAndLocation(cityName, routeName, startLocation, endLocation, TimeUtils.timeToString(startTime));
        //预计到达时间
        LocalDateTime expectArriveTime = startTime.plusSeconds(secondsByBusAndLocation.getSeconds());
        long secondsDifferenceLong = Duration.between(expectArriveTime, arriveTime).getSeconds();
        if (Math.abs(secondsDifferenceLong) < allowDifferenceSeconds) {
            log.info("预计" + routeName + "路公交车如果在" + startTime + "出发，将会在" + expectArriveTime + "到达目的地");
            return startTime;
        } else {
            return getStartTimeByArriveTime(cityName, routeName,
                    startTime.plusSeconds(secondsDifferenceLong / 2), arriveTime,
                    startLocation, endLocation, allowDifferenceSeconds);
        }
    }

    @Override
    public BusDTO getBestBusFromStartTime(String cityName, String routeName, String endStationName) {
        List<BusDTO> busLocationList = busService.getBusLocationList(cityName, routeName, endStationName);
        if(CollectionUtils.isEmpty(busLocationList)){
            return null;
        }
        busLocationList.sort(Comparator.comparing(BusDTO::getDisStat).reversed());
        int size = busLocationList.size();
        // 如果有两辆车在同两个站点之间且是在倒数第三辆或之后，那么优先考虑，否则取倒数第2辆公交
        if(size >1){
            if(size >2 && Objects.equals(busLocationList.get(2).getDisStat(),busLocationList.get(1).getDisStat())){
                // 返回倒数第3辆
                return busLocationList.get(2);
            }else {
                // 返回倒数第2辆
                return busLocationList.get(1);
            }
        }else {
            // 只有一辆那么返回这辆
            return busLocationList.get(0);
        }
    }
}
