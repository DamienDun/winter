package com.winter.file.storage.impl;

import com.winter.common.utils.channel.AbstractChannelContext;
import com.winter.file.storage.StorageClient;
import com.winter.file.storage.StorageClientContext;

/**
 * 存储客户实现
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/17 13:18
 */
public class StorageClientContextImpl extends AbstractChannelContext<StorageClient> implements StorageClientContext {

    /**
     *
     */
    public StorageClientContextImpl() {
        super(16);
    }
}
