package top.anlythree.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author anlythree
 * @description: 全局线程池配置（目前只用于TaskUtil类中）
 * @time 2022/3/2311:22
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPool(){
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setMaxPoolSize(3);
        threadPool.setCorePoolSize(2);
        threadPool.setQueueCapacity(10);
        threadPool.setKeepAliveSeconds(300);
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPool;
    }

}
