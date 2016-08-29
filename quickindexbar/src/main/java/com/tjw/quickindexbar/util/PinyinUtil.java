package com.tjw.quickindexbar.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * ^-^
 * Created by tang-jw on 8/28.
 */
public class PinyinUtil {
	
	/**
	 * 根据传入的汉字获取其首字母大写的拼音串
	 *
	 * @param string 传入的汉字字符串
	 * @return 返回首字母大写的拼音字符串
	 */
	public static String getPinyin(String string) {
		
		//汉语拼音输出格式
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		
		//不需要声调
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		//设置转换成大写字母
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		
		char[] charArray = string.toCharArray();
		
		StringBuilder sb = new StringBuilder();
		
		for (char c : charArray) {
			if (Character.isWhitespace(c)) {
				continue;
			}
			
			if (c < 127) {
				//在此范围内不是汉字, 直接拼接不用转换成拼音
				sb.append(c);
			} else {
				try {
					//获取第一个汉字的大写首字母
					String s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
					badHanyuPinyinOutputFormatCombination.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	
}
