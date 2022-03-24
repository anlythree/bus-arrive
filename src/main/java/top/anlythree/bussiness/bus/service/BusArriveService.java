package top.anlythree.bussiness.bus.service;

import org.springframework.stereotype.Service;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.bussiness.dto.RouteDTO;

import java.time.LocalDateTime;

/**
 * @author anlythree
 * @description: 公交到达接口（主接口）
 * @time 2022/3/1810:48 上午
 */
public interface BusArriveService {

   /**
    * 根据公交路线，到达目的地时间来推算  合适的公交什么时候从起始站出发
    * @param cityName
    * @param routeName
    * @param startTime 非必填，默认当前时间，该参数为预估出发时间
    * @param arriveTime 到达时间
    * @param startLocationLal 该线路公交起始站经纬度
    * @param endLocationLal 我们的出发站点经纬度
    * @return 这个时间将是播报的时间，在 这个时间 会提示还可以准备多久
    */
    LocalDateTime getStartTimeByArriveTime(String cityName, String routeName,
                                           LocalDateTime startTime, LocalDateTime arriveTime,
                                           String startLocationLal, String endLocationLal,
                                           Long allowDifferenceSeconds);

    /**
     * 根据出发时间选择最适合的一辆公交车作为要乘坐的公交车
     *
     * @param cityName
     * @param xiaoYuanRouteDTO
     * @return
     */
    BusDTO getBestBusFromStartTime(String cityName, XiaoYuanRouteDTO xiaoYuanRouteDTO);

   /**
    *
    * 计算什么时候可以获取计算结果 && 延时在取结果之前计算何时出发
    * @param cityName
    * @param routeName
    * @param startLocationDto
    * @param endLocationDto
    * @param prepareMinutes
    * @param arriveLocalTime
    * @return
    */
   LocalDateTime getCalculateTimeAndCalculateDelay(String cityName,
                                                   String routeName,
                                                   LocationDTO startLocationDto,
                                                   LocationDTO endLocationDto,
                                                   String prepareMinutes,
                                                   LocalDateTime arriveLocalTime);


    /**
     * 计算需要什么时候出发
     *
     * @param cityName 城市名称
     * @param startBusStationLal 出发站点经纬度
     * @param routeDTO 公交路线
     * @param prepareSeconds 准备时间，从准备出发到出发站点的时间（单位：秒）
     * @param doCalculateTime 开始计算的时间（获取在线公交位置的时间）
     * @param arriveTime 到达时间
     * @return
     */
    void calculateTimeToGo(
            String cityName, XiaoYuanRouteDTO routeDTO,
            LocationDTO startLocationDto, String startBusStationLal,
            Long prepareSeconds,
            LocalDateTime doCalculateTime, LocalDateTime arriveTime)

}
