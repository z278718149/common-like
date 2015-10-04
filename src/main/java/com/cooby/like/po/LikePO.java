package com.cooby.like.po;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LikePO {

	@Id
	private String id;
	@Indexed
	private String status;
	private String content;
	@Indexed
	private Date date;
	/**
	 * from
	 */
	@Indexed
	private String fromType;
	private String fromId;
	private String fromName;
	private String fromLogo;
	/**
	 * to
	 */
	private String toType;
	private String toId;
	private String toName;
	private String toLogo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromLogo() {
		return fromLogo;
	}

	public void setFromLogo(String fromLogo) {
		this.fromLogo = fromLogo;
	}

	public String getToType() {
		return toType;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToLogo() {
		return toLogo;
	}

	public void setToLogo(String toLogo) {
		this.toLogo = toLogo;
	}

}
