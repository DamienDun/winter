package com.winter.file.storage.service;

import com.winter.common.core.service.IBaseService;
import com.winter.file.storage.dto.FsBigFileUploadTaskInput;

import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/14 8:51
 */
public interface FsBigFileUploadTaskService extends IBaseService<Long, FsBigFileUploadTaskInput, FsBigFileUploadTaskInput> {

    /**
     * 更新完成字节,状态
     * @param id
     * @param completeBytes
     * @param status
     * @param uploadedPartNumberList
     */
    void updateCompleteBytesAndStatus(Long id, Long completeBytes, Integer status, Set<Integer> uploadedPartNumberList);

    /**
     * 更新完成字节,状态
     * @param uploadId
     * @param completeBytes
     * @param status
     * @param uploadedPartNumberList
     */
    void updateCompleteBytesAndStatus(String uploadId, Long completeBytes, Integer status, Set<Integer> uploadedPartNumberList);

    /**
     * 根据文件md5查询
     *
     * @param fileMd5
     * @return
     */
    FsBigFileUploadTaskInput queryByFileMd5(String fileMd5);

    /**
     * 根据uploadId查询
     *
     * @param uploadId
     * @return
     */
    FsBigFileUploadTaskInput queryByUploadId(String uploadId);

    /**
     * 取消任务
     *
     * @param id
     */
    void cancel(Long id);
}
