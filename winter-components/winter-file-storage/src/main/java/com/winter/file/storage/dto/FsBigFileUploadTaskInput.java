package com.winter.file.storage.dto;

import com.winter.common.constant.Constants;
import com.winter.common.core.domain.entity.audit.PrimaryKey;
import com.winter.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/14 8:50
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("大文件上传任务入参")
public class FsBigFileUploadTaskInput implements PrimaryKey<Long> {

    private static final long serialVersionUID = -6906400844497727410L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 分片上传的uploadId
     */
    @ApiModelProperty(value = "分片上传的uploadId")
    private String uploadId;

    /**
     * 文件的Md5值
     */
    @ApiModelProperty(value = "文件的Md5值")
    private String fileMd5;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /**
     * 桶名称
     */
    @ApiModelProperty(value = "桶名称")
    private String bucketName;

    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    /**
     * 访问路径
     */
    @ApiModelProperty(value = "访问路径")
    private String accessPath;

    /**
     * 文件总字节数
     */
    @ApiModelProperty(value = "文件总字节数")
    private Long totalBytes;

    /**
     * 已完成字节数
     */
    @ApiModelProperty(value = "已完成字节数")
    private Long completeBytes;

    /**
     * 分片字节数
     */
    @ApiModelProperty(value = "分片字节数")
    private Long partBytes;

    /**
     * 分片数量
     */
    @ApiModelProperty(value = "分片数量")
    private Integer partCount;

    /**
     * 已上传的分片编号
     */
    @ApiModelProperty(value = "已上传的分片编号")
    private String uploadedPartNumbers;

    /**
     * 任务状态;0:取消上传;1:新建;2:上传中;3:上传完成
     */
    @ApiModelProperty(value = "任务状态;0:取消上传;1:新建;2:上传中;3:上传完成")
    private Integer status;

    @ApiModelProperty(value = "已上传的分块编号")
    private Set<Integer> uploadedPartNumberList = new HashSet<>();

    public Set<Integer> getUploadedPartNumberList() {
        if (StringUtils.isNotEmpty(uploadedPartNumbers)) {
            return Arrays.stream(uploadedPartNumbers.trim().split(Constants.ENGLISH_COMMA))
                    .map(s -> Integer.valueOf(s.trim())).collect(Collectors.toSet());
        }
        return uploadedPartNumberList;
    }

    public String getUploadedPartNumbers() {
        if (!CollectionUtils.isEmpty(uploadedPartNumberList)) {
            return uploadedPartNumberList.stream().map(String::valueOf).collect(Collectors.joining(Constants.ENGLISH_COMMA));
        }
        return uploadedPartNumbers;
    }
}
