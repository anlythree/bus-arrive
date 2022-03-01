package top.anlythree.task;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/12:11 下午
 */
@Component
public class PrintApplicationNameRunner implements ApplicationRunner {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(applicationName + "项目启动成功!!!");
    }
}
