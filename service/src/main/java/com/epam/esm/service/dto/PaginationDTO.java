package com.epam.esm.service.dto;

public class PaginationDTO {
    public static final Integer DEFAULT_RECORDS_PER_PAGE = 10;
    public static final Integer MAX_RECORDS_PER_PAGE = 500;
    public static final Long FIRST_PAGE = 1L;

    private Long page;
    private Integer size;
    private Long totalCount;
    private Long totalPages;

    public PaginationDTO() {
    }

    public PaginationDTO(Long page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
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

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }
}
