package top.anlythree.api;

import org.springframework.util.CollectionUtils;
import top.anlythree.cache.ACache;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;
import top.anlythree.utils.exceptions.AException;

import java.util.List;
import java.util.Objects;

/**
 * 路线接口
 * @author anlythree
 */
public interface RouteService {

    /**
     * 根据路线名和城市id获取路线列表（正常可以查到2条线路，因为有往返）
     * @param routeName
     * @param cityId
     * @return
     */
    List<Route> getRouteListByNameAndCityId(String routeName, String cityId);

    /**
     * 根据路线名称，城市名称，线路起始站查找公交线路
     * @param routeName
     * @param cityName
     * @param startStation
     * @return
     */
    Route getRoutByNameAndCityIdAndStartStation(String routeName, String cityName, String startStation);

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
    Route getRouteByNameAndCityAndRideStartAndRideEnd(String routeName, String cityName, String rideStart, String rideEnd);

}
