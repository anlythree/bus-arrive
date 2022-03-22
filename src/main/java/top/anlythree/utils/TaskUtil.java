package top.anlythree.utils;

import lombok.extern.slf4j.Slf4j;
import top.anlythree.utils.exceptions.AException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskUtil {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(15);

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * 延时执行任务
     *
     * @param runnable
     */
    public static void doSomeThingLater(Runnable runnable, LocalDateTime doTime) {
        executorService.execute(() -> {
            try {
                scheduledExecutorService.schedule(runnable,
                        Duration.between(LocalDateTime.now(), doTime).getSeconds(), TimeUnit.SECONDS).get();
            } catch (InterruptedException e) {
                throw new AException("执行定时任务失败,错误类型：InterruptedException：" + e.getMessage());
            } catch (ExecutionException e) {
                throw new AException("执行定时任务失败,错误类型：ExecutionException：" + e.getMessage());
            }
            log.info("task success.");
        });
    }


}
