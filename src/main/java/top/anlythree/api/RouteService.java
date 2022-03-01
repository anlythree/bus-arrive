package top.anlythree.api;

import top.anlythree.dto.Route;

/**
 * 路线接口
 * @author anlythree
 */
public interface RouteService {

    Route getRouteByNameAndCityId(String routeName, Integer cityId);

}
