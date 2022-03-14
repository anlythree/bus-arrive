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
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanBusRes;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.bussiness.dto.StationDTO;

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

    @Test
    public void test1(){
        StationDTO station = stationService.getStation("杭州", "余杭", "永福村");
        System.out.println(station);
    }

    @Test
    public void test2(){
        List<BusDTO> busLocation = busService.getBusLocation("杭州", "353", "梦想小镇");
        System.out.println(busLocation);
    }

    @Test
    public void test3(){
        List<XiaoYuanRouteDTO> routeListByNameAndCityName = routeService.getRouteListByNameAndCityName("353", "杭州");
        System.out.println(routeListByNameAndCityName);
    }

}
