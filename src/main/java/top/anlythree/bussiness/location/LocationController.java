package top.anlythree.bussiness.location;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.cache.ACache;
import top.anlythree.utils.exceptions.AException;

import java.util.stream.Collectors;

/**
 * 地址相关接口
 * @author anlythree
 * @description:
 * @time 2022/3/2514:15
 */
@RequestMapping("/location")
@RestController
public class LocationController {

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation(value = "{\"stationName\":\"地点名称\",\"stationFullName\":\"中国浙江省杭州市余杭区地点名称\",\"longitudeAndLatitude\":\"经纬度\",\"country\":\"中国\",\"province\":\"浙江省\",\"city\":\"杭州市\",\"district\":\"余杭区\"}")
    @GetMapping("/addLocation")
    public String addLocation(String locationJson){
        try {
            LocationDTO locationDTO = objectMapper.readValue(locationJson, LocationDTO.class);
            ACache.addLocation(locationDTO);
        } catch (JsonProcessingException e) {
            throw new AException("json格式不正确,未添加成功");
        }
        // 返回所有已缓存的地点名称列表
        return ACache.getLocationCacheList().stream().map(LocationDTO::getStationFullName).collect(Collectors.toList()).toString();
    }
}
