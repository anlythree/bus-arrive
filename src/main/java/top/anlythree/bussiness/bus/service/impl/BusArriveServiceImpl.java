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
import top.anlythree.api.amapimpl.res.AMapBusRoute2Res;
import top.anlythree.api.amapimpl.res.AMapBusRouteRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanBusRes;
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
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeServiceXiaoYuanImpl;

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
        AMapBusRoute2Res.ImportInfo importInfo = routeServiceAMapImpl.getBusRoute2ByLocation(cityName, startLocationLal, endLocationLal, startTime).getImportInfo(routeName);
        //预计到达时间
        LocalDateTime expectArriveTime = startTime.plusSeconds(importInfo.getSeconds());
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
    public BusDTO getBestBusOnStartTime(List<BusDTO> busLocationList) {
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
        if(null == startBusLal){
            log.error("没有找到相关公交方案，高德api返回信息："+busRouteByLocation);
            throw new AException("没有找到相关公交方案，高德api返回信息："+busRouteByLocation);
        }
        // 获取出发公交站点坐标和名称
        String startBusStationLal = startBusLal.getStartBusStationLal();
        String startBusStationName = startBusLal.getStartBusStationName();
        // 获取该公交线路始发站Dto
        String[] startStationAndEndStation = startBusLal.getStartStationAndEndStation();
        LocationDTO firstStationByRouteDto = stationService.getLocationByName(cityName, startStationAndEndStation[0]);
        // 获取路线信息
        XiaoYuanRouteDTO routeDto = routeServiceXiaoYuanImpl.getRouteByNameAndCityAndEndStation(cityName, routeName, startStationAndEndStation[1]);
        // 获取目标公交车最晚发车时间（及返回值）
        LocalDateTime doCalculateTime = getStartTimeByArriveTime(cityName, routeName,
                null, arriveLocalTime,
                firstStationByRouteDto.getLongitudeAndLatitude(), endLocationDto.getLongitudeAndLatitude(),
                null);
        // 延时计算出发时间并持久化至缓存（ACache）
        TaskUtil.doSomeThingLater(() -> {
            LocalDateTime leaveStartLocationTime = calculateTimeToGo(cityName, routeDto,
                    startLocationDto, endLocationDto, startBusStationLal, startBusStationName,
                    Long.parseLong(prepareMinutes) * 60, doCalculateTime, arriveLocalTime);
            String isTooLate = null;
            Duration duration = TimeUtil.timeInterval(leaveStartLocationTime);
            if(duration.isNegative()){
                isTooLate = "已经赶不上最后一辆车，相差"+duration.getSeconds()+"秒";
            }
            // 持久化
            ACache.addResult(key,
                    new BusArriveResultDto(startLocationDto.getStationName(), endLocationDto.getStationName(), routeName,
                            TimeUtil.timeToString(arriveLocalTime), TimeUtil.timeToString(leaveStartLocationTime),isTooLate));
        }, doCalculateTime);
        return doCalculateTime;
    }

    @Override
    public LocalDateTime calculateTimeToGo(
            String cityName, XiaoYuanRouteDTO routeDTO,
            LocationDTO startLocationDto, LocationDTO endLocationDto, String startBusStationLal, String startBusStationName,
            Long prepareSeconds, LocalDateTime doCalculateTime, LocalDateTime arriveTime) {
        // 当前在线公交车列表
        XiaoYuanBusRes xiaoYuanBusRes = busService.getXiaoYuanBusRes(cityName, routeDTO);
        // 计算走到起点公交站所需时间
        Long walkSecondsByLocation = routeServiceAMapImpl.getWalkSecondsByLocation(cityName, startLocationDto.getLongitudeAndLatitude(), startBusStationLal, arriveTime).getSeconds();
        // 获取目标公交车
        BusDTO bestBus;
        if (Math.abs(Duration.between(LocalDateTime.now(), doCalculateTime).getSeconds()) > 2) {
            // 当前时间已不是最晚公交车发车时间，只能从起始站到出发站找倒数第二辆还能准时到达目的地的公交车,如果只剩一辆那么就选最后一辆
            bestBus = getBestBusAfterStartTime(cityName, routeDTO, xiaoYuanBusRes,
                    startBusStationName, arriveTime, endLocationDto, startBusStationLal, walkSecondsByLocation);
        } else {
            // 当前时间为最晚公交车出发时间
            bestBus = getBestBusOnStartTime(xiaoYuanBusRes.getBusList());
            if (bestBus == null) {
                throw new AException("没有找到合适的在线公交车信息。");
            }
        }
        AMapBusRoute2Res.ImportInfo importInfo = routeServiceAMapImpl.getBusRoute2ByLocation(cityName,
                bestBus.getLocation(), startBusStationLal,
                doCalculateTime).getImportInfo(routeDTO.getRouteName());
        if (null == importInfo || null == importInfo.getSeconds()) {
            log.error("计算失败，无法计算出发时间，要求到达时间" + arriveTime + ",计算时间：" + doCalculateTime + "。");
            throw new AException("计算失败，无法计算出发时间，要求到达时间" + arriveTime + ",计算时间：" + doCalculateTime + "。");
        }
        // 计算计算时间向后推秒数 = 车到达起始站点所需时间（秒）-准备时间（秒）-步行时间（秒）
        Long plusSeconds = importInfo.getSeconds() - prepareSeconds - walkSecondsByLocation;
        return doCalculateTime.plusSeconds(plusSeconds);
    }

    private BusDTO getBestBusAfterStartTime(String cityName, XiaoYuanRouteDTO routeDTO, XiaoYuanBusRes xiaoYuanBusRes, String startBusStationName, LocalDateTime arriveTime,
                                            LocationDTO endLocationDto, String startBusStationLal, Long walkSecondsByLocation) {
        // 起始站在第几站(从始发站开始计数)
        Integer startBusStationItemNum = getStartBusStationItemNum(xiaoYuanBusRes, startBusStationName);
        // 当前时间到到达时间还剩多久
        Long secondsHave = Duration.between(arriveTime, LocalDateTime.now()).getSeconds();
        Integer bestBusItem = null;
        for (int i = 0; i < xiaoYuanBusRes.getBusList().size(); i++) {
            String busLal = xiaoYuanBusRes.getBusList().get(i).getLocation();
            // 该公交到目的地时间
            Long busToEndStationSeconds = routeServiceAMapImpl.getBusRoute2ByLocation(cityName,
                    busLal, endLocationDto.getLongitudeAndLatitude(), null).getImportInfo(routeDTO.getRouteName()).getSeconds();
            // 该公交到站点时间
            Long busToStartStationSeconds = routeServiceAMapImpl.getBusRoute2ByLocation(cityName,
                    busLal, startBusStationLal, null).getImportInfo(routeDTO.getRouteName()).getSeconds();
            if (secondsHave.compareTo(busToEndStationSeconds) > 0 &&
                    busToStartStationSeconds.compareTo(walkSecondsByLocation) > 0) {
                bestBusItem = i;
            }
            if (xiaoYuanBusRes.getBusList().get(i).getStationNum() > startBusStationItemNum) {
                // 起始站之后的车是已经赶不上的了，跳出循环
                break;
            }
        }
        if (bestBusItem == null) {
            throw new AException("当前所有公交车均会晚点");
        }
        if (bestBusItem < xiaoYuanBusRes.getBusList().size() - 1) {
            // 如果前边还有合适的车就选前边的，防止最后一辆车坐不上
            bestBusItem++;
            String busLocationLal = xiaoYuanBusRes.getBusList().get(bestBusItem).getLocation();
            Long busToStartStationSeconds = routeServiceAMapImpl.getBusTransitsByLocation(cityName, routeDTO.getRouteName(),
                    busLocationLal, startBusStationLal, null).getSeconds();
            if (busToStartStationSeconds.compareTo(walkSecondsByLocation) < 0) {
                // 如果这辆车走过去已经赶不上上车点了就还是坐最后一辆车
                bestBusItem--;
            }
        }
        return xiaoYuanBusRes.getBusList().get(bestBusItem);
    }

    /**
     * 根据当前公交信息获取起始站在第几站
     * @param xiaoYuanBusRes
     * @param startBusStationName
     * @return
     */
    private Integer getStartBusStationItemNum(XiaoYuanBusRes xiaoYuanBusRes, String startBusStationName) {
        Integer startBusStationItemNum = null;
        for (int i = 0; i < xiaoYuanBusRes.getStationNameList().size(); i++) {
            if (xiaoYuanBusRes.getStationNameList().get(i).contains(startBusStationName)) {
                startBusStationItemNum = i;
                break;
            }
        }
        if (startBusStationItemNum == null) {
            log.error("根据站点名称未找到站点是第几个，站点名：" + startBusStationName + "所有站点：" + xiaoYuanBusRes.getStationNameList());
            throw new AException("计算出发时间失败");
        }
        return startBusStationItemNum;
    }
}
