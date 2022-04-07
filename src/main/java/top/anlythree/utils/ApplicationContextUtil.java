package top.anlythree.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
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

    private static DefaultListableBeanFactory beanFactory;

    @Autowired
    @Qualifier(value = "defaultListableBeanFactory")
    public void setDefaultListableBeanFactory(DefaultListableBeanFactory defaultListableBeanFactory) {
        this.beanFactory = defaultListableBeanFactory;
    }

    /**
     * 获取bean
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> tClass){
        return (T) beanFactory.getBean(tClass.getName());
    }

    /**
     * 把对象添加为bean
     * @param object
     * @param <T>
     */
    public static <T> void addBean(T object){
        log.info("log----------------------beanFactory:"+beanFactory+"object："+object);
        beanFactory.registerSingleton(object.getClass().getName(),object);
        beanFactory.autowireBean(object);
    }
}
