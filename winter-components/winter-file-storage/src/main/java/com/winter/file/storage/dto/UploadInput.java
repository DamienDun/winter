package com.winter.file.storage.dto;

import com.winter.common.exception.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 9:52
 */
@Getter
@Setter
@NoArgsConstructor
public class UploadInput extends UploadOptionsDto {

    private static final long serialVersionUID = 8996851282584599059L;

    /**
     * 输入流
     */
    @ApiModelProperty(value = "输入流")
    private InputStream inputStream;

    /**
     * 是否Zip压缩
     */
    @ApiModelProperty(value = "是否Zip压缩")
    private boolean zipCompress;

    /**
     * 输入字节
     */
    @ApiModelProperty(value = "输入字节")
    private byte[] bytes;

    @ApiModelProperty(value = "分片编号")
    private Integer partNumber;

    @ApiModelProperty(value = "重复提交分片编号时是否覆盖")
    private boolean override;

    @Override
    public void valid() {
        super.valid();
        if (this.getBytes() != null) {
            if (this.getBytes().length == 0) {
                throw new ServiceException("至少二进制字节不能空。");
            }
            this.setInputStream(new ByteArrayInputStream(this.getBytes()));
        } else {
            if (this.getInputStream() == null) {
                throw new ServiceException("输入流不能为空。");
            }
        }
    }
}
