package top.anlythree.api.xiaoyuanimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/104:01 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturlInfoRes {

    private List<LineInfoRes> lineinfo;

    private List<StationInfoRes> stations;

    private List<BusInfoRes> buses;
}
