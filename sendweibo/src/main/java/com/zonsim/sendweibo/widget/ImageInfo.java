package com.zonsim.sendweibo.widget;

import java.io.Serializable;

/**
 * CopyRight
 * Created by tang-jw on 2016/8/8.
 */
public class ImageInfo implements Serializable {
	public String thumbnailUrl;
	public String bigImageUrl;
	public int imageViewHeight;
	public int imageViewWidth;
	public int imageViewX;
	public int imageViewY;
	
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	
	public String getBigImageUrl() {
		return bigImageUrl;
	}
	
	public void setBigImageUrl(String bigImageUrl) {
		this.bigImageUrl = bigImageUrl;
	}
	
	public int getImageViewHeight() {
		return imageViewHeight;
	}
	
	public void setImageViewHeight(int imageViewHeight) {
		this.imageViewHeight = imageViewHeight;
	}
	
	public int getImageViewWidth() {
		return imageViewWidth;
	}
	
	public void setImageViewWidth(int imageViewWidth) {
		this.imageViewWidth = imageViewWidth;
	}
	
	public int getImageViewX() {
		return imageViewX;
	}
	
	public void setImageViewX(int imageViewX) {
		this.imageViewX = imageViewX;
	}
	
	public int getImageViewY() {
		return imageViewY;
	}
	
	public void setImageViewY(int imageViewY) {
		this.imageViewY = imageViewY;
	}
	
	@Override
	public String toString() {
		return "ImageInfo{" +
				"imageViewY=" + imageViewY +
				", imageViewX=" + imageViewX +
				", imageViewWidth=" + imageViewWidth +
				", imageViewHeight=" + imageViewHeight +
				", bigImageUrl='" + bigImageUrl + '\'' +
				", thumbnailUrl='" + thumbnailUrl + '\'' +
				'}';
	}
}