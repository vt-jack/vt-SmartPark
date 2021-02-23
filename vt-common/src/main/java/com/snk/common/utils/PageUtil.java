package com.snk.common.utils;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
public class PageUtil<T>{

    private int total; // 总条数
    private int pageSize;// 每页显示数
    private List<T> list;// 要进行分页的list

    public PageUtil(int pageSize, List<T> list) {
        super();
        this.pageSize = ObjectUtils.isEmpty(pageSize) ? 5000 : pageSize;
        this.total = list.size();
        this.list = list;
    }

    public int getTotalPage() {
        return this.total % this.pageSize == 0 ? this.total / this.pageSize : this.total / this.pageSize + 1;
    }


    public List<T> getPageList(int page) {
        if (ObjectUtils.isEmpty(page)) {
            page = 1;
        }
        List<T> pageList = this.list.subList(this.getPageSize() * (page - 1),
                (this.getPageSize() * page) > this.getTotal() ? this.getTotal() : (this.getPageSize() * page));
        return pageList;
    }

}
