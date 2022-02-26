package top.anlythree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApplicationMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationMain.class);
        System.out.println("启动成功");
    }
}
