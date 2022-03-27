import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import top.anlythree.api.amapimpl.res.AMapBusRouteRes;
import top.anlythree.api.amapimpl.res.AMapStationListRes;
import top.anlythree.api.amapimpl.res.AMapWalkRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.bus.controller.BusController;
import top.anlythree.bussiness.bus.service.BusArriveService;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.bussiness.dto.StationDTO;
import top.anlythree.utils.TaskUtil;
import top.anlythree.utils.TimeUtil;

import java.time.LocalDateTime;
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

    @Autowired
    private BusArriveService busArriveService;

    @Autowired
    private BusController busController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test1(){
        StationDTO station = stationService.getStation("杭州", "余杭", "永福村");
        System.out.println(station);
    }

    @Test
    public void test3(){
        List<XiaoYuanRouteDTO> routeListByNameAndCityName = routeService.getRouteListByNameAndCityName("353", "杭州");
        System.out.println(routeListByNameAndCityName);
    }

    @Test
    public void test4(){
        AMapStationListRes aMapStationListRes = stationService.getLocationByNameFromAMap("杭州", "杭州市阿里巴巴A5门");
        System.out.println(aMapStationListRes.getOneLocationRes("杭州市阿里巴巴A5门").getLocation());
    }

    @Test
    public void test5(){
        AMapBusRouteRes busRouteTimeByLocation = aMapRouteService.getBusRouteByLocation("杭州","120.034084,30.242901", "120.026686,30.280905",null);
        System.out.println(busRouteTimeByLocation);
    }

    @Test
    public void test6(){
        String[] dateAndTimeByDateTimeStr = TimeUtil.getDateAndTimeByTime(LocalDateTime.now());
        System.out.println(dateAndTimeByDateTimeStr[0]);
        System.out.println(dateAndTimeByDateTimeStr[1]);
    }

    @Test
    public void test7(){
        AMapBusRouteRes.AMapBusRouteInfo.TransitsInfo transitsInfo = aMapRouteService.getBusTransitsByLocation("杭州",
                "353", "120.026686,30.280905",
                "120.034084,30.242901", null);
        System.out.println(transitsInfo.getSeconds()/60);
        String[] startStationAndEndStation = transitsInfo.getStartStationAndEndStation();
        System.out.println("起始站："+startStationAndEndStation[0]+";结束站："+startStationAndEndStation[1]);
    }

    @Test
    public void test8(){
        StationDTO startStation = stationService.getStation("杭州", "余杭", "西湖体育馆");
        StationDTO arriveStation = stationService.getStation("杭州", "余杭", "阿里巴巴A5门");
        LocalDateTime startTimeByArriveTime = busArriveService.getStartTimeByArriveTime("杭州", "353",
                null, TimeUtil.stringToTime("2022-03-18 10:00:00"),
                startStation.getLongitudeAndLatitude(), arriveStation.getLongitudeAndLatitude(),null);
        System.out.println(TimeUtil.timeToString(startTimeByArriveTime));
    }

    @Test
    public void test10() {
        System.out.println("开始");
        new Thread(()->{
            TaskUtil.doSomeThingLater(()->{
                System.out.println("延时测试"+LocalDateTime.now());
            },LocalDateTime.now().plusSeconds(60));
        }).start();
        System.out.println("结束");
    }

    @Test
    public void test11(){
        StationDTO startStation = stationService.getStation("杭州", "余杭", "西湖体育馆");
        StationDTO arriveStation = stationService.getStation("杭州", "余杭", "阿里巴巴A5门");
        LocalDateTime startTimeByArriveTime = busArriveService.getStartTimeByArriveTime("杭州", "353",
                null, TimeUtil.stringToTime("2022-03-18 10:00:00"),
                startStation.getLongitudeAndLatitude(), arriveStation.getLongitudeAndLatitude(),null);
        System.out.println(TimeUtil.timeToString(startTimeByArriveTime));
    }

    @Test
    public void test12(){
        AMapWalkRouteTimeRes.Route.Path walkSecondsByLocationName = aMapRouteService.getWalkSecondsByLocationName("杭州", "爱橙街", "阿里巴巴A5门", null);
        System.out.println(walkSecondsByLocationName);
    }

    @Test
    public void test13(){
        LocalDateTime now = LocalDateTime.now();
        String[] dateAndTimeByTime = TimeUtil.getDateAndTimeByTime(now);
        System.out.println(dateAndTimeByTime[0]+dateAndTimeByTime[1]);
    }

    @Test
    public void test14(){
        busController.calculateTime("杭州",
                "353",
                "阿里巴巴访客中心",
                "五常大道丰铃路追梦家公寓",
                "5",
                "20:40"
                );
    }

    @Test
    public void test15(){
        String disStat = "距离第2个站还有distance米";
        int i = Integer.parseInt(disStat.substring(disStat.indexOf("距离第") + 3, disStat.indexOf("个站还有")));
        System.out.println(i);
    }

    @Test
    public void test16() throws JsonProcessingException {
        LocationDTO locationDTO = new LocationDTO();
        String s = objectMapper.writeValueAsString(locationDTO);
        System.out.println(s);
    }
}
