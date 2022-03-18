package top.anlythree.bussiness.bus.service.impl;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import top.anlythree.api.BusService;
import top.anlythree.api.RouteService;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.BusDTO;

import java.time.LocalDateTime;

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
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeServiceXiaoYuanImpl;

    @Override
    public LocalDateTime getStartTimeByArriveTime(String cityName, String busName, String arriveTime, String startLocation, String endLocation) {
        // 根据当前时间路况来计算 开始到结束需要多久
        Integer minuteDifference = 0;
        LocalDateTime
        do{

        }while (minuteDifference < 5)
        return null;
    }
}
