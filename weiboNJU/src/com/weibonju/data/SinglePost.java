package com.weibonju.data;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class SinglePost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long pid;
	private Date created_at;
	private String text;
	private int reposts_count,comments_count,attitudes_count;
	private String pic_ids;
	private URL thumbnail_pic;
	private String source;
	
	private long uid;
	private String screen_name;
	private URL profile_image_url;
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getReposts_count() {
		return reposts_count;
	}
	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}
	public int getComments_count() {
		return comments_count;
	}
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}
	public int getAttitudes_count() {
		return attitudes_count;
	}
	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}
	public String getPic_ids() {
		return pic_ids;
	}
	public void setPic_ids(String pic_ids) {
		this.pic_ids = pic_ids;
	}
	public URL getThumbnail_pic() {
		return thumbnail_pic;
	}
	public void setThumbnail_pic(URL thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public URL getProfile_image_url() {
		return profile_image_url;
	}
	public void setProfile_image_url(URL profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
}
