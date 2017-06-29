package com.fantasi.common.db.tools;

public class DBUtils {
	public static String getTypeFromDbType(String type){
		type = type.toLowerCase();
		String result = "";
		if (type.contains("int")) {
			result = "int";
		} else if (type.equals("varchar") || type.equals("longtext") || type.equals("text") || type.contains("time") || type.contains("date")) {
			result = "String";
		}
		return result;
	}
	
	public static String getCSharpTypeFromDbType(String type) {
		type = type.toLowerCase();
		String result = "";
		if (type.contains("int")) {
			result = "int";
		} else if (type.equals("varchar") || type.equals("longtext") || type.equals("text") || type.contains("time") || type.contains("date")) {
			result = "string";
		}
		return result;
	}
	
	
	/**
	 * 将首字母大写 例：sourceId to SourceId
	 * @param str
	 * @return
	 */
	public static String upperCaption(String str) {
		char[] chars = str.toCharArray();
		int i = (int)chars[0];
		if (i < 97) {
			return str;
		}
		chars[0] = (char)(chars[0] - 32);
		return new String(chars);
	}
	

	/**
	 * 从字段到属性的映射 例：source_id to sourceId
	 * @param columnName
	 * @param isProperty
	 * @return
	 */
	public static String getFieldNameFromColumnName(String columnName, boolean isProperty) {
		char[] chars = columnName.toCharArray();
        StringBuilder property = new StringBuilder();
        
        for (int index = 0; index < chars.length; index++){
        	char item = chars[index];
        	if (item == '_') {
        		index++;
        		//转换成大写
        		item = (char)(chars[index] - 32);
        	}
        	if (index == 0 && !isProperty) {
        		item = (char)(chars[index] - 32);
        	}
        	property.append(item);
        }
        return property.toString();
	}
	

	/**
	 * 类名到表名的映射 例子：AnaBbsInfo to ana_bbs_info
	 * @param className
	 * @return
	 */
	public static String getTableNameFromClassName(String className){
		char[] chars = className.toCharArray();
		StringBuilder property = new StringBuilder();
	        
		for (int index = 0; index < chars.length; index++){
			char item = chars[index];
			if (item >= 'A' && item <= 'Z') {
				if (index !=0 ) {
					property.append("_");
				}
				property.append((char)(item + 32));
			} else {
				property.append(item);
			}
		}
		return property.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getTableNameFromClassName("AnaBbsInfo"));
	}
	
	public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }
}
