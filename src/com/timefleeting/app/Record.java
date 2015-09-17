package com.timefleeting.app;

public class Record {

	private int id;
	private String title;
	private String text;
	private String remindTime;
	private String createTime;
	private String star;
	private String type;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getRemindTime() {
		return remindTime;
	}
	
	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}

	public String getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getStar() {
		return star;
	}
	
	public void setStar(String star) {
		this.star = star;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
