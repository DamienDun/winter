package com.winter.common.utils.poi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * excel导出多个sheet
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/6/14 13:22
 */
@Getter
@Setter
@NoArgsConstructor
public class ExcelExp {

    /**
     * sheet的名称
     */
    private String sheetName;
    /**
     * sheet里的标题
     */
    private String title;
    /**
     * sheet里的数据集
     */
    private List data;
    private Class clazz;

    public ExcelExp(String sheetName, List data, Class clazz) {
        this.sheetName = sheetName;
        this.data = data;
        this.clazz = clazz;
    }
}
