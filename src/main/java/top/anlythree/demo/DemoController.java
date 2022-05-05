package top.anlythree.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anlythree.api.BusService;
import top.anlythree.utils.TaskUtil;

import java.time.LocalDateTime;

@Api(tags = "swagger的getDemoStr测试")
@RestController("demo")
public class DemoController {

    @GetMapping("/get")
    @ApiOperation("getDemoStr")
    public String getDemoStr() {
        String demoStr = "demoStr:controller链接成功。当前时间：" + LocalDateTime.now();
        System.out.println(demoStr);
        System.out.println("开始");
        TaskUtil.doSomeThingLater(() -> {
            System.out.println("延时测试"+LocalDateTime.now());
        }, LocalDateTime.now().plusSeconds(10));
        System.out.println("结束");
        return demoStr;
    }

    @GetMapping("/getStream")
    public String getStream() {
        String demoStr = "getStream:controller链接成功。当前时间：" + LocalDateTime.now();
        System.out.println(demoStr);
        System.out.println("开始");
        System.out.println("结束");
        return demoStr;
    }


}
