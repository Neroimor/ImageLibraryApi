package com.neroimor.ImageLibrary.Models.PageData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagesDataInfo {
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;

}
