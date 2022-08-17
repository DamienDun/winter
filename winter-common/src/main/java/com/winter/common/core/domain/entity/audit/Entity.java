package com.winter.common.core.domain.entity.audit;

import java.io.Serializable;

/**
 * @author Damien
 * @description 实体
 * @create 2022/7/21 13:27
 */
public interface Entity<TKey extends Serializable> extends PrimaryKey<TKey>, DeleteAuditing {

}