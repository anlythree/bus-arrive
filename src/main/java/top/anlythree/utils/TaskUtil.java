package top.anlythree.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import top.anlythree.utils.exceptions.AException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
            try {
                Thread.sleep(Duration.between(LocalDateTime.now(), doTime).getSeconds()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runnable.run();
        });
        log.info("task success.");
    }


}
