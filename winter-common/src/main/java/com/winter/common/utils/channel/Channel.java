package com.winter.common.utils.channel;

/**
 * 通道
 * <p>
 * 提供各种通道管理
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/15 17:23
 */
public interface Channel {

    /**
     * 获取通道id
     *
     * @return
     */
    String getChannelId();

    /**
     * 获取通道名称
     *
     * @return
     */
    String getChannelName();
}
