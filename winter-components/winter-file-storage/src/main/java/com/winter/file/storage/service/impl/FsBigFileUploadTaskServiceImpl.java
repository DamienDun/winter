package com.winter.file.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.winter.common.constant.Constants;
import com.winter.common.core.service.BaseService;
import com.winter.common.utils.AutoMapUtils;
import com.winter.file.storage.domain.FsBigFileUploadTask;
import com.winter.file.storage.dto.FsBigFileUploadTaskInput;
import com.winter.file.storage.enums.FsBigFileUploadTaskStatusEnum;
import com.winter.file.storage.mapper.FsBigFileUploadTaskMapper;
import com.winter.file.storage.service.FsBigFileUploadTaskService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/14 8:52
 */
@Service
public class FsBigFileUploadTaskServiceImpl extends BaseService<Long, FsBigFileUploadTask, FsBigFileUploadTaskMapper,
        FsBigFileUploadTaskInput, FsBigFileUploadTaskInput> implements FsBigFileUploadTaskService {

    @Override
    public String getModuleName() {
        return "大文件上传任务模块";
    }

    @Override
    public void updateCompleteBytesAndStatus(Long id, Long completeBytes, Integer status, Set<Integer> uploadedPartNumberList) {
        FsBigFileUploadTask fsBigFileUploadTask = getMapper().selectById(id);
        if (Objects.isNull(fsBigFileUploadTask)) {
            return;
        }
        fsBigFileUploadTask.setCompleteBytes(completeBytes);
        fsBigFileUploadTask.setStatus(status);
        if (!CollectionUtils.isEmpty(uploadedPartNumberList)) {
            fsBigFileUploadTask.setUploadedPartNumbers(uploadedPartNumberList.stream().sorted().map(String::valueOf).collect(Collectors.joining(Constants.ENGLISH_COMMA)));
        }
        getMapper().updateById(fsBigFileUploadTask);
    }

    @Override
    public void updateCompleteBytesAndStatus(String uploadId, Long completeBytes, Integer status, Set<Integer> uploadedPartNumberList) {
        LambdaQueryWrapper<FsBigFileUploadTask> wrapper = Wrappers.lambdaQuery(FsBigFileUploadTask.class);
        wrapper.eq(FsBigFileUploadTask::getUploadId, uploadId);
        FsBigFileUploadTask fsBigFileUploadTask = getMapper().selectForFirst(wrapper);
        if (Objects.isNull(fsBigFileUploadTask)) {
            return;
        }
        fsBigFileUploadTask.setCompleteBytes(completeBytes);
        fsBigFileUploadTask.setStatus(status);
        if (!CollectionUtils.isEmpty(uploadedPartNumberList)) {
            fsBigFileUploadTask.setUploadedPartNumbers(uploadedPartNumberList.stream().sorted().map(String::valueOf).collect(Collectors.joining(Constants.ENGLISH_COMMA)));
        }
        getMapper().updateById(fsBigFileUploadTask);
    }

    @Override
    public FsBigFileUploadTaskInput queryByFileMd5(String fileMd5) {
        LambdaQueryWrapper<FsBigFileUploadTask> wrapper = Wrappers.lambdaQuery(FsBigFileUploadTask.class);
        wrapper.eq(FsBigFileUploadTask::getFileMd5, fileMd5);
        return AutoMapUtils.map(getMapper().selectForFirst(wrapper), FsBigFileUploadTaskInput.class);
    }

    @Override
    public FsBigFileUploadTaskInput queryByUploadId(String uploadId) {
        LambdaQueryWrapper<FsBigFileUploadTask> wrapper = Wrappers.lambdaQuery(FsBigFileUploadTask.class);
        wrapper.eq(FsBigFileUploadTask::getUploadId, uploadId);
        return AutoMapUtils.map(getMapper().selectForFirst(wrapper), FsBigFileUploadTaskInput.class);
    }

    @Override
    public void cancel(Long id) {
        FsBigFileUploadTask fsBigFileUploadTask = getMapper().selectById(id);
        if (Objects.isNull(fsBigFileUploadTask)) {
            return;
        }
        fsBigFileUploadTask.setStatus(FsBigFileUploadTaskStatusEnum.CANCEL.getCode());
        getMapper().updateById(fsBigFileUploadTask);
        this.deleteById(id);
    }
}
