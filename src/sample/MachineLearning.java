package sample;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ***********************************************
 * Created by Lunzi on 8/29/2017.
 * Just presonal practice.
 * Not allowed to copy without permission.
 * ***********************************************
 */
class MachineLearning {
	private FileType fileType = new FileType();
	int learnCount = 0;
	
	/**
	 * 通过检查本地已知后缀文件进行特征库补充
	 * @param file 检查的文件实例
	 * @return 该文件的特征字符串+学习结果
	 */
	String learnFile(File file) {
		String res = "\n";
		String[] temp = file.getName().split("\\.");
		String extName = temp[temp.length - 1].toLowerCase();
		if (extName.length() > 20)
			return "\n特殊后缀文件，不予学习";
		for (String s : Config.MLIgnoreThpe) {
			if (s.equals(extName)) {
				return "\n无特征文本文件，不予学习";
			}
		}
		String fileCode = fileType.getFileCode(file.getAbsolutePath());
		res += fileCode + " = " + extName;
		String getType = fileType.getFileType(file.getAbsolutePath());
		if (getType == null || !getType.contains(extName)) {
			boolean getNewItem = addItemToMap(fileCode, extName);
			res += getNewItem ? "  --> 学习成功" : "";
			learnCount += getNewItem ? 1 : 0;
			System.out.print(res);
		}
		return res;
	}
	
	
	private boolean addItemToMap(String src, String extName) {
		return fileType.getTypeCount(extName) < 30 && FileType.fileTypeMap.put(src, extName) != null;
	}
	
	/**
	 * 通过相同前缀合并已知的特征数据，其相同前缀长度必须大于等于6位
	 * @return 被合并的特征数量
	 */
	int mergeTypeMap(){
		List<Map.Entry<String, String>> sortMap = fileType.sortFileTypeMap(false);
		int mergeItemCount = 0;
		int mapSize = sortMap.size();
		for (int i = 0; i < mapSize - 1; i++) {
			String prefix = getLCP(sortMap.get(i).getKey(), sortMap.get(i + 1).getKey());
			if (prefix.length() >= 6) {
				//merge items
				String mergeValue = mergeValues(sortMap.get(i).getValue(), sortMap.get(i + 1).getValue());
				sortMap.set(i, new AbstractMap.SimpleEntry<String, String>(prefix, mergeValue));
				
				//delete old items
				sortMap.remove(i + 1);
				
				//change length to make loop right
				mapSize--;
				i--;
				
				mergeItemCount++;
			}
		}
		
		//update map
		FileType.fileTypeMap.clear();
		sortMap.forEach(item -> FileType.fileTypeMap.put(item.getKey(), item.getValue()));
		return mergeItemCount;
	}
	
	/**
	 * Get lonest common prefix with two strings.
	 * @param str1 one string.
	 * @param str2 one string.
	 * @return The lonest common prefix string.
	 */
	private String getLCP(String str1, String str2) {
		int minLength = str1.length() < str2.length() ? str1.length() : str2.length();
		for (int i = 0; i < minLength; i++) {
			if(str1.charAt(i)!= str2.charAt(i)) return str1.substring(0, i);
		}
		return str1.length() < str2.length() ? str1 : str2;
	}
	
	/**
	 * Merge two extnames strings splite with ','.
	 * Example:"doc,docx" and "xls,docx" merge to "doc,docx,xls"
	 * @param value1 one string.
	 * @param value2 one string.
	 * @return The merged string.
	 */
	private String mergeValues(String value1, String value2) {
		List<String> values = new ArrayList<>();
		for (String value : (value1.concat(",").concat(value2)).split(",")){
			if (values.indexOf(value) == -1) values.add(value);
		}
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < values.size(); i++) {
			sb.append(values.get(i));
			if(i != values.size()-1) sb.append(",");
		}
		return sb.toString();
	}
}
