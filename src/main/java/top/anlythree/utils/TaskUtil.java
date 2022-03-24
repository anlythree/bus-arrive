package top.anlythree.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
public class TaskUtil {
    private static ThreadPoolTaskExecutor threadPool;

    @Autowired
    public void setThreadPool(ThreadPoolTaskExecutor threadPool) {
        TaskUtil.threadPool = threadPool;
    }

    /**
     * 延时执行任务
     *
     * @param runnable
     */
    public static void doSomeThingLater(Runnable runnable, LocalDateTime doTime) {
        threadPool.execute(()->{
            long seconds = Duration.between(LocalDateTime.now(), doTime).getSeconds();
            if(seconds > 0){
                try {
                    Thread.sleep(seconds*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runnable.run();
        });
        log.info("task success.");
    }
}
