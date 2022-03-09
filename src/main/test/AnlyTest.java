import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.anlythree.SpringApplicationMain;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.dto.StationDTO;

import java.util.Map;

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

    @Test
    public void test1(){
        StationDTO station = stationService.getStation("杭州", "余杭", "永福村");
        System.out.println(station);
    }

}
