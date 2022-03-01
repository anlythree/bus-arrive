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

    List<Route> getRouteListByNameAndCityId(String routeName, String cityId);

    Route getRoutByNameAndCityIdAndStartStation(String routeName, String cityName, String startStation);

    void cacheRouteByNameAndCityName(String cityName, String routeName);

}
