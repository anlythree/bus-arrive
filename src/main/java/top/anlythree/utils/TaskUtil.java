package top.anlythree.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 创建任务线程来执行任务（即时或延时）
 */
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
     * 如果执行时间晚于当前时间2秒以上则延时，2秒之内不延时；
     * 如果执行时间早于当前时间直接执行
     *
     * @param runnable
     */
    public static void doSomeThingLater(Runnable runnable, LocalDateTime doTime) {
        threadPool.execute(()->{
            if(doTime != null){
                long seconds = Duration.between(LocalDateTime.now(), doTime).getSeconds();
                if(doTime.isAfter(LocalDateTime.now()) && seconds > 2){
                    // 需要运行时间在当前时间之后且时间差大于2秒就延时执行，否则就直接执行
                    log.info("定时任务将于"+doTime+"执行");
                    try {
                        Thread.sleep(seconds * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            runnable.run();
        });
        log.info("task success.");
    }
}
