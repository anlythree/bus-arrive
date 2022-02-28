package top.anlythree.bus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anlythree.bus.service.BusService;

import javax.annotation.Resource;
import java.time.Duration;

@RestController("/bus")
public class BusController {

    @Resource
    private BusService busService;

    @GetMapping("/getBusArriveTime")
    public Integer getBusArriveTime(String busCodeName, String busStationName){
        Duration busArriveTime = busService.getBusArriveTime(busCodeName, busStationName);
        long seconds = busArriveTime.getSeconds()/6;
        return (int) seconds;
    }
}
