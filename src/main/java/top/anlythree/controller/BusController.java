package top.anlythree.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/bus")
public class BusController {

    @GetMapping("/getBusLocation")
    public String getBusLocation(){
        return null;
    }

}
