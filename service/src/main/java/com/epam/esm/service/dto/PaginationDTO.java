package com.epam.esm.service.dto;

public class PaginationDTO {
    public static final Integer DEFAULT_RECORDS_PER_PAGE = 10;
    public static final Integer FIRST_PAGE = 1;

    private Integer page;
    private Integer size;
    private Long totalCount;

    public PaginationDTO() {
    }

    public PaginationDTO(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
