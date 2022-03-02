package top.anlythree.api.xiaoyuanimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anlythree.api.StationService;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanStationDTO;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/211:12 上午
 */
@Service
public class XiaoYuanStationServiceImpl implements StationService {

    @Value("${xiaoyuan.username}")
    private String uname;

    @Value("${xiaoyuan.key}")
    private String key;

    @Override
    public XiaoYuanStationDTO getStationByStationName(String stationName) {

        return null;
    }
}
