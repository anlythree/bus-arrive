package top.anlythree.utils;

import lombok.extern.slf4j.Slf4j;
import top.anlythree.utils.exceptions.AException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Slf4j
public class TaskUtil {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    /**
     * 延时执行任务
     * @param callable
     */
    public static <T> T doSomeThingLater(Callable<T> callable, LocalDateTime doTime){
        T returnValue;
        try {
            returnValue = scheduledExecutorService.schedule(callable,
                    Duration.between(LocalDateTime.now(), doTime).getSeconds(), TimeUnit.SECONDS).get();
            log.info("task success.return:"+ returnValue.toString() +";");
        } catch (InterruptedException e) {
            throw new AException("执行定时任务失败,错误类型：InterruptedException："+e.getMessage());
        } catch (ExecutionException e) {
            throw new AException("执行定时任务失败,错误类型：ExecutionException："+e.getMessage());
        }
        return returnValue;
    }


}
