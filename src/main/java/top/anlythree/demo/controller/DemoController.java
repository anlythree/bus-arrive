package top.anlythree.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Api(tags = "swagger的getDemoStr测试")
@RestController
public class DemoController {

    @GetMapping("/get")
    @ApiOperation("getDemoStr")
    public String getDemoStr() {
        String demoStr = "demoStr:controller链接成功。当前时间：" + LocalDateTime.now();
        System.out.println(demoStr);
        return demoStr;
    }
}
