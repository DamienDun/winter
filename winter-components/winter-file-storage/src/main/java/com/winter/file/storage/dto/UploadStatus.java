package com.winter.file.storage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 上传状态
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 14:17
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "上传状态")
public class UploadStatus extends UploadOptionsDto {

    private static final long serialVersionUID = 1315950565114065299L;

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
     * 总字节数
     */
    @ApiModelProperty(value = "总字节数")
    private long totalBytes;

    /**
     * 已完成字节数
     */
    @ApiModelProperty(value = "已完成字节数")
    private long completeBytes;

    /**
     * 分片字节数
     */
    @ApiModelProperty(value = "分片字节数")
    private long partBytes;

    /**
     * 分片数量
     */
    @ApiModelProperty(value = "分片数量")
    private int partCount;

    /**
     * 任务状态;0:取消上传;1:新建;2:上传中;3:上传完成
     */
    @ApiModelProperty(value = "任务状态;0:取消上传;1:新建;2:上传中;3:上传完成")
    private Integer status;

    /**
     * 已上传的分片编号
     */
    @ApiModelProperty(value = "已上传的分片编号")
    private Set<Integer> uploadedPartNumberList;

    /**
     * 已上传分片数量
     */
    @ApiModelProperty(value = "已上传分片数量")
    private int uploadedPartCount;

    public Set<Integer> getUploadedPartNumberList() {
        if (CollectionUtils.isEmpty(uploadedPartNumberList)) {
            uploadedPartNumberList = new HashSet<>(getPartCount());
        }
        return uploadedPartNumberList;
    }

    public int getUploadedPartCount() {
        if (CollectionUtils.isEmpty(uploadedPartNumberList)) {
            return 0;
        }
        return getUploadedPartNumberList().size();
    }

    /**
     * 是否完成
     */
    @ApiModelProperty(value = "是否完成")
    public boolean isComplete() {
        return this.getCompleteBytes() >= this.getTotalBytes();
    }

    /**
     * 进度
     *
     * @return
     */
    @ApiModelProperty(value = "进度")
    public int getProgress() {
        if (this.isComplete()) {
            return 100;
        }
        if (this.getTotalBytes() <= 0) {
            return 0;
        }
        double value = (double) this.getCompleteBytes() / (double) this.getTotalBytes() * 100.0;
        if (value > 100.0) {
            return 100;
        }
        return (int) Math.rint(value);
    }
}
