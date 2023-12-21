package com.winter.file.storage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 上传申请输出
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 9:41
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "上传申请输出")
public class UploadApplyOutput extends UploadStatus {

    private static final long serialVersionUID = 6827136088328132703L;

    /**
     * 是否存在源，如果存在则不需要重新上传
     */
    @ApiModelProperty(value = "是否存在源，如果存在则不需要重新上传")
    private boolean existSource;

}
