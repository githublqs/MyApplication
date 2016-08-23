package com.example.administrator.myapplication.fileupload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PageInfo implements Serializable{
	private static final long serialVersionUID = -7622967957690712426L;
	private int pageNo;
	private int pageSize;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public PageInfo() {
		super();
	}
	public PageInfo(int pageNo, int pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	public String toJsonString(){
		JSONObject jo=new JSONObject();
		try {
			jo.put("pageNo",pageNo);
			jo.put("pageSize",pageSize);
			jo.put("retListDirect",retListDirect);
			return  jo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	private boolean retListDirect;
	public boolean isRetListDirect() {
		return retListDirect;
	}
	public void setRetListDirect(boolean retListDirect) {
		this.retListDirect = retListDirect;
	}
	public PageInfo(int pageNo, int pageSize, boolean retListDirect) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.retListDirect = retListDirect;
	}


}
