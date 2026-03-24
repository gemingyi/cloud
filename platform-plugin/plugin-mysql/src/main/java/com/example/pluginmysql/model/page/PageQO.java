package com.example.pluginmysql.model.page;

/**
 * 分页参数接收类
 */
public class PageQO {

    private int currentPage = 1;

    private int pageSize = 10;

    public PageQO() {

    }

    public PageQO(int currentPage, int pageSize) {
        super();
        if (currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize > 50) {
            pageSize = 50;
        }
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (this.currentPage - 1) * this.pageSize;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
