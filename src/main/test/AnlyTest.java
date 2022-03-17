import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.anlythree.SpringApplicationMain;
import top.anlythree.api.BusService;
import top.anlythree.api.RouteService;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.api.amapimpl.res.AMapStationListRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.bussiness.dto.StationDTO;
import top.anlythree.utils.TimeUtils;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/82:44 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= SpringApplicationMain.class)
public class AnlyTest{

    @Autowired
    @Qualifier(value = "AMapStationServiceImpl")
    private StationService stationService;

    @Autowired
    @Qualifier(value = "xiaoYuanBusServiceImpl")
    private BusService busService;

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeService;

    @Autowired
    @Qualifier(value = "AMapRouteServiceImpl")
    private RouteService aMapRouteService;

    @Test
    public void test1(){
        StationDTO station = stationService.getStation("杭州", "余杭", "永福村");
        System.out.println(station);
    }

    @Test
    public void test2(){
        List<BusDTO> busLocation = busService.getBusLocationList("杭州", "353", "梦想小镇");
        System.out.println(busLocation);
    }

    @Test
    public void test3(){
        List<XiaoYuanRouteDTO> routeListByNameAndCityName = routeService.getRouteListByNameAndCityName("353", "杭州");
        System.out.println(routeListByNameAndCityName);
    }

    @Test
    public void test4(){
        AMapStationListRes aMapStationListRes = stationService.getLocationByName("杭州", "杭州市阿里巴巴A5门");
        System.out.println(aMapStationListRes.getOneLocationRes("杭州市阿里巴巴A5门").getLocation());
    }

    @Test
    public void test5(){
        AMapBusRouteTimeRes busRouteTimeByLocation = aMapRouteService.getBusRouteTimeByLocation("杭州","120.034084,30.242901", "120.026686,30.280905",null);
        System.out.println(busRouteTimeByLocation);
    }

    @Test
    public void test6(){
        String[] dateAndTimeByDateTimeStr = TimeUtils.getDateAndTimeByDateTimeStr("2022-03-17 11:19:01");
        System.out.println(dateAndTimeByDateTimeStr[0]);
        System.out.println(dateAndTimeByDateTimeStr[1]);
    }

    @Test
    public void test7(){
        AMapBusRouteTimeRes.AMapBusRouteInfo.TransitsInfo transitsInfo = aMapRouteService.getSecondsByBusAndLocation("杭州",
                "120.034084,30.242901", "120.026686,30.280905",
                "353", null);
        System.out.println(transitsInfo.getSeconds()/60);
    }
}
