package sample;

import java.io.*;
import java.util.*;

/**
 * ***********************************************
 * Created by Lunzi on 8/27/2017.
 * Just presonal practice.
 * Not allowed to copy without permission.
 * ***********************************************
 */
class FileType {
	//文件类型特征库
	static Map<String, String> fileTypeMap = new HashMap<>();
	
	/**
	 * 从附带的数据文件加载文件特征库
	 * @throws IOException
	 */
	void loadFileTypeMap() throws IOException {
		fileTypeMap.clear();
		File file = new File(Config.FILE_TYPE_DATA_PATH);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp;
		while ((temp = br.readLine()) != null) {
			fileTypeMap.put(temp.split("=")[0], temp.split("=")[1]);
		}
		br.close();
	}
	
	/**
	 * 将当前的文件特征库保存至文件
	 * @throws IOException
	 */
	void saveFileTypeMap() throws IOException {
		File file = new File(Config.FILE_TYPE_DATA_PATH);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (Object o : sortFileTypeMap(true)) {
			bw.write(o + "\n");
		}
		bw.close();         //关闭流防止后续文件操作占用
	}
	
	/**
	 * 将Map转为List并排序
	 *
	 * @param byValue 排序依据，true->value false->key
	 * @return 返回以后缀名字典序排序的特征库List
	 */
	List<Map.Entry<String, String>> sortFileTypeMap(boolean byValue) {
		List<Map.Entry<String, String>> sortMap = new ArrayList<>(fileTypeMap.entrySet());
		sortMap.sort(Comparator.comparing(byValue ? Map.Entry::getValue : Map.Entry::getKey));
		return sortMap;
	}
	
	
	/**
	 * 输出当前文件特征库
	 * @return 一个包含换行的全部特征库String
	 */
	String printFileTypeMap() {
		StringBuilder sb = new StringBuilder();
		sortFileTypeMap(true).forEach(item -> sb.append(item).append("\n"));
		return sb.toString();
	}
	
	/**
	 * 将文件字节转化为16进制文本的字符串
	 * @param src 字节组
	 * @return 该字节组16进制表示形式的字符串
	 */
	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (byte aSrc : src) {
			int v = aSrc & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 获取该文件前十个字节的16进制表示形式字符串
	 * @param filePath 文件路径
	 * @return 该文件前十个字节的数据的16进制表示的字符串
	 */
	String getFileCode(String filePath) {
		String fileCode;
		try {
			FileInputStream is = new FileInputStream(filePath);
			byte[] b = new byte[10];
			is.read(b, 0, 10);
			fileCode = bytesToHexString(b);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fileCode;
	}
	
	/**
	 * 在Map中匹配文件特征，并返回文件类型
	 * @param filePath 该文件的路径
	 * @return 若匹配成功，返回Map中的文件类型，若匹配失败，返回 null
	 */
	String getFileType(String filePath) {
		String res = null;
		String fileCode = getFileCode(filePath);
		for (String key : fileTypeMap.keySet()) {
			if (key.toLowerCase().startsWith(fileCode.toLowerCase()) ||
					fileCode.toLowerCase().startsWith(key.toLowerCase())) {
				res = fileTypeMap.get(key);
				break;
			}
		}
		return res != null ? res.toLowerCase() : null;
	}
	
	/**
	 * 统计文件类型在特征库中的特征数量
	 * @param type 文件类型
	 * @return 特征库中该文件类型特征的数量
	 */
	int getTypeCount(String type) {
		int count = 0;
		for (String temp : fileTypeMap.values()) {
			if (temp.equals(type)) count++;
		}
		return count;
	}
}
