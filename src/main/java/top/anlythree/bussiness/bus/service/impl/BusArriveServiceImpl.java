package top.anlythree.bussiness.bus.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.BusService;
import top.anlythree.api.RouteService;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.res.AMapBusRouteRes;
import top.anlythree.api.amapimpl.res.AMapWalkRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.BusArriveResultDto;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.cache.ACache;
import top.anlythree.utils.TaskUtil;
import top.anlythree.utils.TimeUtil;
import top.anlythree.utils.exceptions.AException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
    @Qualifier(value = "AMapStationServiceImpl")
    private StationService stationService;

    @Value("${bus-arrive.default-difference-seconds}")
    private Long defaultDifferenceSeconds;

    @Override
    public LocalDateTime getStartTimeByArriveTime(String cityName, String routeName,
                                                  LocalDateTime startTime, LocalDateTime arriveTime,
                                                  String startLocationLal, String endLocationLal,
                                                  Long allowDifferenceSeconds) {
        if (null == allowDifferenceSeconds) {
            allowDifferenceSeconds = defaultDifferenceSeconds;
        }
        if (null == startTime) {
            // 如果没填预计出发时间，则选择到达时间前的一个小时作为预计出发时间
            startTime = arriveTime.minusHours(1);
        }
        // 路径规划
        AMapBusRouteRes.AMapBusRouteInfo.TransitsInfo secondsByBusAndLocation =
                routeServiceAMapImpl.getBusTransitsByLocation(cityName, routeName, startLocationLal, endLocationLal, startTime);
        //预计到达时间
        LocalDateTime expectArriveTime = startTime.plusSeconds(secondsByBusAndLocation.getSeconds());
        long secondsDifferenceLong = Duration.between(expectArriveTime, arriveTime).getSeconds();
        if (Math.abs(secondsDifferenceLong) < allowDifferenceSeconds) {
            log.info("预计" + routeName + "路公交车如果在" + startTime + "出发，将会在" + expectArriveTime + "到达目的地");
            return startTime;
        } else {
            return getStartTimeByArriveTime(cityName, routeName,
                    startTime.plusSeconds(secondsDifferenceLong / 2), arriveTime,
                    startLocationLal, endLocationLal, allowDifferenceSeconds);
        }
    }

    @Override
    public BusDTO getBestBusFromStartTime(String cityName, XiaoYuanRouteDTO xiaoYuanRouteDTO) {
        List<BusDTO> busLocationList = busService.getBusLocationList(cityName, xiaoYuanRouteDTO);
        if (CollectionUtils.isEmpty(busLocationList)) {
            return null;
        }
        busLocationList.sort(Comparator.comparing(BusDTO::getDisStat).reversed());
        int size = busLocationList.size();
        // 如果有两辆车在同两个站点之间且是在倒数第三辆或之后，那么优先考虑，否则取倒数第2辆公交
        if (size > 1) {
            if (size > 2 && Objects.equals(busLocationList.get(2).getDisStat(), busLocationList.get(1).getDisStat())) {
                // 返回倒数第3辆
                return busLocationList.get(2);
            } else {
                // 返回倒数第2辆
                return busLocationList.get(1);
            }
        } else {
            // 只有一辆那么返回这辆
            return busLocationList.get(0);
        }
    }

    @Override
    public LocalDateTime getCalculateTimeAndCalculateDelay(String cityName,
                                                           String routeName,
                                                           LocationDTO startLocationDto,
                                                           LocationDTO endLocationDto,
                                                           String prepareMinutes,
                                                           LocalDateTime arriveLocalTime,
                                                           String key) {
        // 获取高德路线信息
        AMapBusRouteRes busRouteByLocation = routeServiceAMapImpl.getBusRouteByLocation(cityName,
                startLocationDto.getLongitudeAndLatitude(),
                endLocationDto.getLongitudeAndLatitude(),
                arriveLocalTime
        );
        AMapBusRouteRes.AMapBusRouteInfo.TransitsInfo startBusLal = busRouteByLocation.getTransitsByRouteName(routeName);
        // 获取出发公交站点坐标
        String startBusStationLal = startBusLal.getWalkingDistance();
        // 获取该公交线路始发站Dto
        String[] startStationAndEndStation = startBusLal.getStartStationAndEndStation();
        LocationDTO firstStationByRouteDto = stationService.getLocationByName(cityName, startStationAndEndStation[0]);
        // 获取路线信息
        XiaoYuanRouteDTO routeDto = routeServiceAMapImpl.getRouteByNameAndCityAndEndStation(cityName, routeName, startStationAndEndStation[1]);
        // 获取目标公交车最晚发车时间（及返回值）
        LocalDateTime doCalculateTime = getStartTimeByArriveTime(cityName, routeName,
                null, arriveLocalTime,
                firstStationByRouteDto.getLongitudeAndLatitude(), endLocationDto.getLongitudeAndLatitude(),
                null);
        // 延时计算出发时间并持久化至缓存（ACache）
        TaskUtil.doSomeThingLater(() -> {
            LocalDateTime leaveStartLocationTime = calculateTimeToGo(cityName, routeDto, startLocationDto, startBusStationLal,
                    Long.parseLong(prepareMinutes) * 60, doCalculateTime, arriveLocalTime);
            // 持久化
            ACache.addResult(key,
                    new BusArriveResultDto(startLocationDto.getStationName(), endLocationDto.getStationName(), routeName,
                            TimeUtil.timeToString(arriveLocalTime), TimeUtil.timeToString(leaveStartLocationTime)));
        }, doCalculateTime);
        // todo-anlythree 已经过了最晚出发时间，需要完善获取目标公交方法
        if (LocalDateTime.now().isAfter(doCalculateTime)) {
            // 已经过了最佳计算时间,则把需要计算的时间置为当前,及不需要延时
            return LocalDateTime.now().plusSeconds(10);
        }
        // 给后台留10秒计算时间，防止请求结果时后台还未计算出结果
        return doCalculateTime.plusSeconds(10);
    }

    @Override
    public LocalDateTime calculateTimeToGo(
            String cityName, XiaoYuanRouteDTO routeDTO,
            LocationDTO startLocationDto, String startBusStationLal,
            Long prepareSeconds, LocalDateTime doCalculateTime, LocalDateTime arriveTime) {
        // 获取目标公交车
        BusDTO bestBus = getBestBusFromStartTime(cityName, routeDTO);
        AMapBusRouteRes.AMapBusRouteInfo.TransitsInfo transitsInfo = routeServiceAMapImpl.getBusTransitsByLocation(cityName, routeDTO.getRouteName(),
                bestBus.getLocation(), startBusStationLal,
                doCalculateTime);
        if (null == transitsInfo || null == transitsInfo.getSeconds()) {
            log.error("计算失败，无法计算出发时间，要求到达时间" + arriveTime + ",计算时间：" + doCalculateTime + "。");
            throw new AException("计算失败，无法计算出发时间，要求到达时间" + arriveTime + ",计算时间：" + doCalculateTime + "。");
        }
        // 计算走到起点公交站所需时间
        AMapWalkRouteTimeRes.Route.Path walkSecondsByLocation = routeServiceAMapImpl.getWalkSecondsByLocation(cityName, startLocationDto.getLongitudeAndLatitude(), startBusStationLal, arriveTime);
        // 计算计算时间向后推秒数 = 车到达起始站点所需时间（秒）-准备时间（秒）-步行时间（秒）
        Long plusSeconds = transitsInfo.getSeconds() - prepareSeconds - Long.parseLong(walkSecondsByLocation.getDuration());
        return doCalculateTime.plusSeconds(plusSeconds);
    }
}
