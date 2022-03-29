package top.anlythree.api;

import top.anlythree.api.amapimpl.res.AMapBusRouteRes;
import top.anlythree.api.amapimpl.res.AMapWalkRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 路线接口
 *
 * @author anlythree
 */
public interface RouteService {

    /**
     * 根据路线名和城市名称获取路线列表（正常可以查到2条线路，因为有往返）
     *
     * @param routeName
     * @param cityName
     * @return
     */
    List<XiaoYuanRouteDTO> getRouteListByNameAndCityName(String routeName, String cityName);

    /**
     * 根据路线名称，城市名称，线路起始站查找公交线路
     *
     * @param routeName
     * @param cityName
     * @param endStationName
     * @return
     */
    XiaoYuanRouteDTO getRouteByNameAndCityAndEndStation(String cityName, String routeName, String endStationName);

    /**
     * 在缓存中查找路线
     *
     * @param cityId
     * @param routeName
     * @param endStationName
     * @return
     */
    XiaoYuanRouteDTO getRouteFromCache(String cityId, String routeName, String endStationName);

    /**
     * 根据城市名称和线路名称缓存公交线路
     *
     * @param cityName
     * @param routeName
     */
    void cacheRouteByNameAndCityName(String cityName, String routeName);

    /**
     * 根据路线名称，城市名称，乘坐开始站，乘坐结束站查找公交路线
     *
     * @param routeName
     * @param cityName
     * @param startStation 起点坐标
     * @param endStation 终点坐标
     * @return
     */
    XiaoYuanRouteDTO getRouteByNameAndCityAndRideStartAndRideEnd(String cityName, String routeName, String startStation, String endStation);

    /**
     * 高德公交路线规划
     *
     * @param startLocationLal
     * @param endLocationLal
     * @return
     */
    AMapBusRouteRes getBusRouteByLocation(String cityName, String startLocationLal, String endLocationLal, LocalDateTime time);

    /**
     * 根据城市名称，开始结束坐标点和公交车号和出发时间（默认当前）查询当前所需要的时间
     *
     * @param cityName
     * @param startLocationLal
     * @param endLocationLal
     * @param dateTime 出发时间
     * @return
     */
    AMapBusRouteRes.AMapBusRouteInfo.TransitsInfo getBusTransitsByLocation(String cityName, String routeName, String startLocationLal, String endLocationLal, LocalDateTime dateTime);

    /**
     * 根据开始结束坐标点查询步行所需要的时间
     *
     * @param startLocationLal
     * @param endLocationLal
     * @param dateTime 出发时间
     * @return
     */
    AMapWalkRouteTimeRes.Route.Path getWalkSecondsByLocation(String cityName, String startLocationLal, String endLocationLal, LocalDateTime dateTime);

    /**
     * 根据开始结束位置名查询步行所需要的时间
     *
     * @param startLocationName
     * @param endLocationName
     * @param dateTime 出发时间
     * @return
     */
    AMapWalkRouteTimeRes.Route.Path getWalkSecondsByLocationName(String cityName, String startLocationName, String endLocationName, LocalDateTime dateTime);

    /**
     * 根据城市名称获取城市代码(不查询缓存)
     * @param cityName
     * @return
     */
    String getCityCodeNoCacheByName(String cityName);

    /**
     * 根据城市名称获取城市代码
     * @param cityName
     * @return
     */
    String getCityCodeByName(String cityName);



}
