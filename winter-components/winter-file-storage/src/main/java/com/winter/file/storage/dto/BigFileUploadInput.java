package com.winter.file.storage.dto;

import com.winter.common.annotation.valid.NotEmptyFile;
import com.winter.common.annotation.valid.NotNullOrBlank;
import com.winter.common.utils.validation.DefaultDataValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 19:43
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "大文件上传入参")
public class BigFileUploadInput extends DefaultDataValidation {

    private static final long serialVersionUID = -8198467400128538785L;

    @ApiModelProperty(value = "文件", required = true)
    @NotEmptyFile(message = "文件不能为空")
    private MultipartFile file;

    @ApiModelProperty(value = "上传id", required = true)
    @NotNullOrBlank(message = "上传id不能为空")
    private String uploadId;

    @ApiModelProperty(value = "分块编号", required = true)
    @NotNull(message = "分块编号不能为空")
    private Integer partNumber;

    @ApiModelProperty(value = "重复提交分片编号时是否覆盖")
    private boolean override;
}
