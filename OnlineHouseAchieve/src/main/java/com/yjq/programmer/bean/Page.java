package com.yjq.programmer.bean;

import org.springframework.stereotype.Component;

/**
 * 分页基本信息
 * @author llq
 *
 */
@Component
public class Page<T> {

	private Integer page; //当前页码
	
	private Integer rows; //每页显示数量
	
	private Integer totalPage;  //总页数
	
	private Integer offset;//对应数据库中的偏移量
	
	private Integer totalCount;  //总记录数

	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getOffset() {
		this.offset = (page - 1) * rows;
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	public Integer getTotalPage() {
		return (totalCount-1)/rows+1;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
