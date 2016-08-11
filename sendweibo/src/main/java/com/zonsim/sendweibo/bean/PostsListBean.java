package com.zonsim.sendweibo.bean;

import java.util.List;

/**
 * ^-^
 * Created by tang-jw on 8/9.
 */
public class PostsListBean {
	
	/**
	 * data : [{"id":1,"name":"张三","content":"人生最精彩的不是成功的那一瞬间，而是回头看，那段漆黑看似没有尽头、苦苦摸索的过程。其实，我只是很在意，在意在我所在意的人的心里，我，在哪个位置。","photo":"http://pic105.nipic.com/file/20160725/13948737_152610217000_2.jpg"},{"id":2,"name":"李四","content":"生活再不如人意，都要学会自我温暖和慰藉，给自己多一点欣赏和鼓励。生活就是童话，只要心存美好，结局就会是美好。","photo":"http://pic105.nipic.com/file/20160725/13948737_152610217000_2.jpg"}]
	 * status : 0
	 * errorMsg : 
	 */
	
	private int status;
	private String errorMsg;
	/**
	 * id : 1
	 * name : 张三
	 * content : 人生最精彩的不是成功的那一瞬间，而是回头看，那段漆黑看似没有尽头、苦苦摸索的过程。其实，我只是很在意，在意在我所在意的人的心里，我，在哪个位置。
	 * photo : http://pic105.nipic.com/file/20160725/13948737_152610217000_2.jpg
	 */
	
	private List<DataBean> data;
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public List<DataBean> getData() {
		return data;
	}
	
	public void setData(List<DataBean> data) {
		this.data = data;
	}
	
	public static class DataBean {
		private int id;
		private String name;
		private String content;
		private String photo;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getContent() {
			return content;
		}
		
		public void setContent(String content) {
			this.content = content;
		}
		
		public String getPhoto() {
			return photo;
		}
		
		public void setPhoto(String photo) {
			this.photo = photo;
		}
	}
}
