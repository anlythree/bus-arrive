package top.anlythree.bus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/bus")
public class BusController {

    @GetMapping("/getBusArriveTime")
    public String getBusArriveTime(){
        return  null;
    }
}
