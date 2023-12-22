package com.winter.web.controller.file;

import com.winter.common.annotation.Anonymous;
import com.winter.common.annotation.Log;
import com.winter.common.constant.BusinessType;
import com.winter.common.core.domain.R;
import com.winter.common.utils.AutoMapUtils;
import com.winter.file.storage.dto.*;
import com.winter.file.storage.service.BigFileUploadMinioService;
import com.winter.web.annotation.IgnoreApiResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 15:57
 */
@RestController
@RequestMapping("/anonymous/bigFile/minio")
@Api(tags = "大文件分块上传minio")
@RequiredArgsConstructor
@Anonymous
public class BigFileMinioController {

    private final BigFileUploadMinioService bigFileUploadMinioService;

    /**
     * 上传申请
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "上传申请")
    @PostMapping(path = "/upload/apply")
    public R<UploadApplyOutput> apply(@RequestBody @Valid UploadApplyInput input) {
        return R.ok(bigFileUploadMinioService.multipartUploadApply(input));
    }

    /**
     * 上传状态
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "上传状态")
    @PostMapping(path = "/upload/status")
    public R<UploadStatus> queryStatus(@RequestBody @Valid UploadOptionsDto input) {
        return R.ok(bigFileUploadMinioService.queryMultipartStatus(input));
    }

    /**
     * 上传(支持并发)
     *
     * @return
     */
    @ApiOperation(value = "上传(支持并发)")
    @PostMapping(path = "/upload")
    @Log(title = "大文件上传", businessType = BusinessType.INSERT)
    public R<UploadOutput> upload(@Valid BigFileUploadInput bigFileUploadInput) throws Exception {
        UploadInput input = AutoMapUtils.map(bigFileUploadInput, UploadInput.class);
        input.setInputStream(bigFileUploadInput.getFile().getInputStream());
        return R.ok(bigFileUploadMinioService.multipartUpload(input));
    }

    /**
     * 取消上传
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "取消上传")
    @PostMapping(path = "/upload/cancel")
    public R<T> cancelUpload(@RequestBody @Valid UploadOptionsDto input) {
        bigFileUploadMinioService.deleteMultipartUpload(input);
        return R.ok();
    }

    /**
     * 下载(仅示例，即隐藏实际地址)
     *
     * @param path     实际路径（实际应用不能传该参数，由其他参数查询出该地址)
     * @param request  请求
     * @param response 响应
     * @throws Exception
     */
    @RequestMapping(path = "/download", method = {RequestMethod.GET})
    @ApiOperation(value = "下载(仅示例，即隐藏实际地址)", produces = "application/octet-stream")
    @IgnoreApiResponseBody
    public void download(@RequestParam("bucketName") String bucketName, @RequestParam("path") String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
        bigFileUploadMinioService.download(bucketName, path, request, response);
    }

    @ApiOperation(value = "手动合并")
    @PostMapping(path = "/merge")
    public R<UploadStatus> merge(@RequestBody @Valid UploadOptionsDto input) throws Exception {
        bigFileUploadMinioService.merge(input.getUploadId());
        return R.ok();
    }
}
