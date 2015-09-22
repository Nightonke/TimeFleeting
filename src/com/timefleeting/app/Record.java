package com.timefleeting.app;

/*
 * notice that the type of the records:
 * one is PAST
 * one is FUTURE
 */

public class Record {

	private int id;
	private String title;
	private String text;
	private String remindTime;
	private String createTime;
	private String star;
	private String type;
	
	public Record() {
		
	}
	
	public Record(int id, String title, String text, String remindTime, 
			String createTime, String star, String type) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.remindTime = remindTime;
		this.createTime = createTime;
		this.star = star;
		this.type = type;
	}
	
	public void set(Record record) {
		this.id = record.id;
		this.title = record.title;
		this.text = record.text;
		this.remindTime = record.remindTime;
		this.createTime = record.createTime;
		this.star = record.star;
		this.type = record.type;
	}
	
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
	
	public String toString() {
		String s = "{";
		s += "id:" + id + ", ";
		s += "title:" + title + ", ";
		s += "text:" + text + ", ";
		s += "remind_time:" + remindTime + ", ";
		s += "create_time:" + createTime + ", ";
		s += "star:" + star + ", ";
		s += "type:" + type + "}";
		return s;
	}
	
}
