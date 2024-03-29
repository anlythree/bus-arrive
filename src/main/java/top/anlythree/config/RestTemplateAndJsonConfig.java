package top.anlythree.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.anlythree.utils.ApplicationContextUtil;

import java.util.Arrays;
import java.util.TimeZone;

import static org.springframework.http.MediaType.APPLICATION_XML;

/**
 * RestTempale解析配置
 * @author anlythree
 * @time 2022/3/111:12 上午
 */
@Configuration
public class RestTemplateAndJsonConfig {

    @Autowired
    private ApplicationContextUtil applicationContextUtil;


    @Bean
    public RestTemplate restTemplate() {
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
        httpMessageConverter.setSupportedMediaTypes(
                Arrays.asList(MediaType.TEXT_HTML,
                        MediaType.APPLICATION_XHTML_XML,
                        MediaType.APPLICATION_JSON_UTF8,
                        MediaType.APPLICATION_JSON,
                        APPLICATION_XML));
        ObjectMapper objectMapper = httpMessageConverter.getObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); //命名策略
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //未知属性不报错
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8")); //时区设置
        applicationContextUtil.addBean(objectMapper);
        return new RestTemplateBuilder()
                .messageConverters(httpMessageConverter)
                .build();
    }


}
