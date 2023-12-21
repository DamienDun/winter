package com.winter.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * 下载文件信息
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 9:17
 */
@Getter
@Setter
public class DownloadFileInput {

    /**
     * 内容类型
     */
    @ApiModelProperty(value = "内容类型")
    private String contentType;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private long fileSize;

    /**
     * 文件名称(含扩展名)
     */
    @ApiModelProperty(value = "文件名称(含扩展名)")
    private String fileName;

    /**
     * 文件流
     */
    @ApiModelProperty(value = "文件流")
    private InputStream fileStream;
}
