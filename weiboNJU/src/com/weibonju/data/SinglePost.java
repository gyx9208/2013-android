package com.weibonju.data;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class SinglePost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private Date created_at;
	private String text;
	private int reposts_count,comments_count,attitudes_count;
	private long pic_ids;
	private URL thumbnail_pic;
	private String source;
	
	private long uid;
	private String screen_name;
	private URL profile_image_url;
}
