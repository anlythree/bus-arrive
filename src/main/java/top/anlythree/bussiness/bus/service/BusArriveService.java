package top.anlythree.bussiness.bus.service;

import top.anlythree.api.amapimpl.res.AMapBusRoute2Res;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.bussiness.dto.LocationDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author anlythree
 * @description: 公交到达接口（主接口）
 * @time 2022/3/1810:48 上午
 */
public interface BusArriveService {

   /**
    * 根据公交路线，到达目的地时间来推算  合适的公交什么时候从起始站出发
    * (高德api只提供了根据出发时间查询到达时间的接口，这个接口是根据到达时间倒推出发时间的，
    * 其中就是循环递归不断去试出发时间，直到到达时间在规定的时间误差之内)
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
     * @param busLocationList
     * @return
     */
    BusDTO getBestBusOnStartTime(List<BusDTO> busLocationList);

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
                                                   LocalDateTime arriveLocalTime,
                                                   String key);


    /**
     * 计算需要什么时候出发
     *
     * @param cityName 城市名称
     * @param routeDTO 公交路线
     * @param prepareSeconds 准备时间，从准备出发到出发站点的时间（单位：秒）
     * @param doCalculateTime 开始计算的时间（获取在线公交位置的时间）
     * @param arriveTime 到达时间
     * @return
     */
    List<String> calculateTimeToGo(
            String cityName, XiaoYuanRouteDTO routeDTO,
            AMapBusRoute2Res.ImportInfo importInfo,
            Long prepareSeconds, LocalDateTime arriveTime);

}
