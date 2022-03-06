package top.anlythree.api.abs;

import lombok.extern.slf4j.Slf4j;

/**
 * @author anlythree
 * @description: 返回结果abstract
 * @time 2022/3/111:35 上午
 */
@Slf4j
public abstract class RootResult {

    public void isApiError() {
        log.info("class:" + this.getClass() + "不支持检查api错误。");
    }
}
