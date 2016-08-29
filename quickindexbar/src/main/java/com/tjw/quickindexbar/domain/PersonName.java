package com.tjw.quickindexbar.domain;

import android.support.annotation.NonNull;

import com.tjw.quickindexbar.util.PinyinUtil;

/**
 * ^-^
 * Created by tang-jw on 8/28.
 */
public class PersonName implements Comparable<PersonName> {
	
	private String name;
	private String pinyin;
	
	public PersonName(String name) {
		this.name = name;
		this.pinyin = PinyinUtil.getPinyin(name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPinyin() {
		return pinyin;
	}
	
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	
	@Override
	public int compareTo(@NonNull PersonName another) {
		return this.pinyin.compareTo(another.pinyin);
	}
	
}
