package top.anlythree.api;

import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;

import java.util.List;

/**
 * 路线接口
 * @author anlythree
 */
public interface RouteService {

    /**
     * 根据路线名和城市id获取路线列表（正常可以查到2条线路，因为有往返）
     * @param routeName
     * @param cityName
     * @return
     */
    List<XiaoYuanRouteDTO> getRouteListByNameAndCityName(String routeName, String cityName);

    /**
     * 根据路线名称，城市名称，线路起始站查找公交线路
     * @param routeName
     * @param cityName
     * @param startStation
     * @return
     */
    XiaoYuanRouteDTO getRouteByNameAndCityIdAndStartStation(String routeName, String cityName, String startStation);

    /**
     * 在缓存中查找路线
     * @param cityId
     * @param routeName
     * @param startStation
     * @return
     */
    XiaoYuanRouteDTO getRouteFromCache(String cityId,String routeName,String startStation);

    /**
     * 根据城市名称和线路名称缓存公交线路
     * @param cityName
     * @param routeName
     */
    void cacheRouteByNameAndCityName(String cityName, String routeName);

    /**
     * 根据路线名称，城市名称，乘坐开始站，乘坐结束站查找公交路线
     * @param routeName
     * @param cityName
     * @param rideStart
     * @param rideEnd
     * @return
     */
    XiaoYuanRouteDTO getRouteByNameAndCityAndRideStartAndRideEnd(String routeName, String cityName, String rideStart, String rideEnd);

    /**
     *  高德公交路线规划
     * @param startLocation
     * @param endLocation
     * @return
     */
    AMapBusRouteTimeRes getBusRouteTimeByLocation(String cityName,String startLocation, String endLocation);

}
