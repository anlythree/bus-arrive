package top.anlythree.api.xiaoyuanimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/95:13 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoYuanBusRes extends XiaoYuanResult{

    private List<ReturlInfoRes> returlList;

}


