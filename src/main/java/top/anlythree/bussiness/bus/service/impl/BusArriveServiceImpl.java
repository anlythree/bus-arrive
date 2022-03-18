package top.anlythree.bussiness.bus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import top.anlythree.api.BusService;
import top.anlythree.api.RouteService;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.BusDTO;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/1811:19 上午
 */
public class BusArriveServiceImpl implements BusArriveService {

    @Autowired
    private BusService busService;

    @Autowired
    @Qualifier(value = "AMapRouteServiceImpl")
    private RouteService routeServiceAMapImpl;

    @Autowired
    @Qualifier(value = "AMapRouteServiceImpl")
    private RouteService routeServiceAMapImpl;

    @Override
    public BusDTO getBusByArriveTimeAndLocation(String dateTime, String startLocation, String endLocation) {
        // 根据
        return null;
    }
}
