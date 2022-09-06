package com.winter.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.winter.common.core.page.PageDomain;
import com.winter.common.core.page.TableSupport;
import com.winter.common.utils.sql.SqlUtil;

import java.util.List;

/**
 * 分页工具类
 *
 * @author winter
 */
public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }

    /**
     * 转换为 Page 对象
     *
     * @param page
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> Page<T> load(IPage<E> page, List<E> sourceList, Class<T> targetClass) {
        Page<T> pageResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setRecords(AutoMapUtils.mapForList(sourceList, targetClass));
        return pageResult;
    }

    /**
     * 转换为 Page 对象
     *
     * @param page
     * @param targetClass
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> Page<T> load(IPage<E> page, Class<T> targetClass) {
        return load(page, page.getRecords(), targetClass);
    }
}
