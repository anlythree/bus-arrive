package top.anlythree.bussiness.bus.service;

import org.springframework.stereotype.Service;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.bussiness.dto.LocationDTO;

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
    * @param startLocationLal 公交起始站经纬度
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
     * @param routeName
     * @param directionStationName 该线路最后一站
     * @return
     */
    BusDTO getBestBusFromStartTime(String cityName, String routeName, String directionStationName);

   /**
    *
    * 计算什么时候可以获取计算结果 && 延时在取结果之前计算何时出发
    * @param cityName
    * @param routeName
    * @param startLocation
    * @param endLocation
    * @param prepareMinutes
    * @param arriveLocalTime
    * @return
    */
   LocalDateTime getCalculateTimeAndCalculateDelay(String cityName,
                                                   String routeName,
                                                   LocationDTO startLocation,
                                                   LocationDTO endLocation,
                                                   String prepareMinutes,
                                                   LocalDateTime arriveLocalTime);


    /**
     * 计算需要什么时候出发
     *
     * @param doCalculateTime 开始计算的时间（获取在线公交位置的时间）
     * @param arriveTime 到达时间
     * @param cityName
     * @param district
     * @param routeName
     * @param startStationName 出发站点名称
     * @param directionStationName 方向站名称（该公交路线的最后一站）
     * @param prepareSeconds 准备时间，从准备出发到出发站点的时间（单位：秒）
     * @return
     */
    void calculateTimeToGo(
                                    String cityName,
                                    String district,
                                    String routeName,
                                    String startStationName,
                                    String directionStationName,
                                    Long prepareSeconds,
                                    LocalDateTime doCalculateTime,
                                    LocalDateTime arriveTime,
                                    String key);

}
