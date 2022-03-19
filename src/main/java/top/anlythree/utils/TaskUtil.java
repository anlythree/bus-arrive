package top.anlythree.utils;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public class TaskUtil {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    /**
     * 延时执行任务
     * @param function
     */
    public static void doSomeThingLater(Function<Object,Void> function, LocalDateTime doTime){

        scheduledExecutorService.execute(function);
    }



}
