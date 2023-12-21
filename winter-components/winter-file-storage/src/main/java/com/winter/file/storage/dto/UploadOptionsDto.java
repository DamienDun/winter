package com.winter.file.storage.dto;

import com.winter.common.annotation.valid.NotNullOrBlank;
import com.winter.common.utils.validation.DefaultDataValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 上传选项Dto
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 9:42
 */
@Getter
@Setter
@NoArgsConstructor
public class UploadOptionsDto extends DefaultDataValidation {

    private static final long serialVersionUID = 1496862942068166564L;

    /**
     * 上传id
     */
    @ApiModelProperty(value = "上传id", required = true)
    @NotNullOrBlank(message = "上传id不能为空")
    private String uploadId;
}
