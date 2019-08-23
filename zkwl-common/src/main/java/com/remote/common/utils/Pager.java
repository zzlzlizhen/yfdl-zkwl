package com.remote.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author zhangwenping
 * @Date 2019/8/8 16:50
 * @Version 1.0
 **/
public class Pager<T>  implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4542617637761955078L;

    /**
     * currentPage 当前页
     */
    private int pageNum = 1;
    /**
     * pageSize 每页大小
     */
    private int pageSize = 10;
    /**
     * pageTotal 总页数
     */
    private int pages;
    /**
     * recordTotal 总条数
     */
    private int recordTotal = 0;
    /**
     * previousPage 前一页
     */
    private int previousPage;
    /**
     * nextPage 下一页
     */
    private int nextPage;
    /**
     * firstPage 第一页
     */
    private int firstPage = 1;
    /**
     * lastPage 最后一页
     */
    private int lastPage;
    /**
     * list 每页的内容
     */
    private List<T> list;



    // 以下set方式是需要赋值的
    /**
     * 设置当前页 <br>
     *
     * @author kangxu
     *
     * @param pageNum
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }


    /**
     * 设置每页大小,也可以不用赋值,默认大小为10条 <br>
     *
     * @author kangxu
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    /**
     * 设置总条数,默认为0 <br>
     *
     * @author kangxu
     *
     * @param recordTotal
     */
    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
        otherAttr();
    }


    /**
     * 设置分页内容 <br>
     *
     * @author kangxu
     *
     * @param list
     */
    public void setList(List<T> list) {
        this.list = list;
    }


    /**
     * 设置其他参数
     *
     * @author kangxu
     *
     */
    public void otherAttr() {
        // 总页数
        this.pages = this.recordTotal % this.pageSize > 0 ? this.recordTotal / this.pageSize + 1 : this.recordTotal / this.pageSize;
        // 第一页
        this.firstPage = 1;
        // 最后一页
        this.lastPage = this.pages;
        // 前一页
        if (this.pageNum > 1) {
            this.previousPage = this.pageNum - 1;
        } else {
            this.previousPage = this.firstPage;
        }
        // 下一页
        if (this.pageNum < this.lastPage) {
            this.nextPage = this.pageNum + 1;
        } else {
            this.nextPage = this.lastPage;
        }
    }


    // 放开私有属性
    public int getPageNum() {
        return pageNum;
    }


    public int getPageSize() {
        return pageSize;
    }


    public int getPages() {
        return pages;
    }


    public int getRecordTotal() {
        return recordTotal;
    }


    public int getPreviousPage() {
        return previousPage;
    }


    public int getNextPage() {
        return nextPage;
    }


    public int getFirstPage() {
        return firstPage;
    }


    public int getLastPage() {
        return lastPage;
    }


    public List<T> getList() {
        return list;
    }


    @Override
    public String toString() {
        return "Pager [pageNum=" + pageNum + ", pageSize=" + pageSize
                + ", pages=" + pages + ", recordTotal=" + recordTotal
                + ", previousPage=" + previousPage + ", nextPage=" + nextPage
                + ", firstPage=" + firstPage + ", lastPage=" + lastPage
                + ", list=" + list + "]";
    }


}
