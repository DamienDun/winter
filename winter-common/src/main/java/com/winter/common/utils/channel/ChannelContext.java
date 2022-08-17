package com.winter.common.utils.channel;

import java.util.Collection;
import java.util.Set;

/**
 * 通道上下文
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/15 18:13
 */
public interface ChannelContext<TChannel extends Channel>  {

    /**
     * 注册通道
     *
     * @param channel 通道
     */
    void register(TChannel channel);

    /**
     * 获取通道
     *
     * @param channelId 通道id
     * @return
     */
    TChannel getChannel(String channelId);

    /**
     * 是否存在通道
     *
     * @param channelId 通道id
     * @return
     */
    boolean exist(String channelId);

    /**
     * 移除通道
     *
     * @param channelId 通道id
     * @return
     */
    boolean removeChannel(String channelId);

    /**
     * 清除所有通道
     */
    void clearAll();

    /**
     * 获取通道id集
     *
     * @return
     */
    Set<String> channelIdSet();

    /**
     * 获取通道集合
     *
     * @return
     */
    Collection<TChannel> channels();
}
