package top.anlythree.api.amapimpl;

import top.anlythree.api.RouteService;
import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.utils.exceptions.AException;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/1610:31 上午
 */
public class AMapRouteServiceImpl implements RouteService {

    @Override
    public AMapBusRouteTimeRes getBusRouteTimeByCityNameAndRouteNameAndEndStation(String cityName, String routeName, String endStation, String startLocation, String endLocation) {
        // todo-anlythree
        return null;
    }

    @Override
    public List<XiaoYuanRouteDTO> getRouteListByNameAndCityName(String routeName, String cityName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityIdAndStartStation(String routeName, String cityName, String startStation) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteFromCache(String cityId, String routeName, String startStation) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public void cacheRouteByNameAndCityName(String cityName, String routeName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityAndRideStartAndRideEnd(String routeName, String cityName, String rideStart, String rideEnd) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }
}
