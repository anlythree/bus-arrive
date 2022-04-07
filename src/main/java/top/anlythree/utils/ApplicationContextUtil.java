package top.anlythree.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author anlythree
 * @description: Spring的bean管理类
 * @time 2022/3/2515:09
 */
@Slf4j
@Component
public class ApplicationContextUtil {

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;


    /**
     * 获取bean
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> tClass){
        return (T) defaultListableBeanFactory.getBean(tClass.getName());
    }

    /**
     * 把对象添加为bean
     * @param object
     * @param <T>
     */
    public <T> void addBean(T object){
        log.info("log----------------------beanFactory:"+ defaultListableBeanFactory +"object："+object);
        defaultListableBeanFactory.registerSingleton(object.getClass().getName(),object);
        defaultListableBeanFactory.autowireBean(object);
    }
}
