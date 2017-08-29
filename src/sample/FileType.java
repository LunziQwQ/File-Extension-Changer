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
public class FileType {
	static Map<String, String> fileTypeMap = new HashMap<String, String>();
	
	void loadFileTypeMap() throws FileNotFoundException, IOException {
		File file = new File(Config.FILE_TYPE_DATA_PATH);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp;
		while ((temp = br.readLine()) != null) {
			fileTypeMap.put(temp.split("=")[0], temp.split("=")[1]);
		}
		br.close();
	}
	
	void saveFileTypeMap() throws IOException {
		File file = new File(Config.FILE_TYPE_DATA_PATH);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		Iterator iterator = sortFileTypeMap().iterator();
		while (iterator.hasNext()) {
			bw.write(iterator.next() + "\n");
		}
		bw.close();
	}
	
	private List sortFileTypeMap() {
		List<Map.Entry<String, String>> sortMap = new ArrayList<>(fileTypeMap.entrySet());
		sortMap.sort(Comparator.comparing(Map.Entry::getValue));
		return sortMap;
	}
	
	String printFileTypeMap() {
		StringBuilder sb = new StringBuilder();
		sortFileTypeMap().forEach(item -> sb.append(item).append("\n"));
		return sb.toString();
	}
	
	
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
	
	String getFileCode(String filePath) {
		String fileCode = null;
		try {
			FileInputStream is = new FileInputStream(filePath);
			byte[] b = new byte[10];
			is.read(b, 0, 10);
			fileCode = bytesToHexString(b);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileCode;
	}
	
	String getFileType(String filePath) {
		String res = null;
		String fileCode = getFileCode(filePath);
		Iterator<String> keyIter = fileTypeMap.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			if (key.toLowerCase().startsWith(fileCode.toLowerCase()) ||
					fileCode.toLowerCase().startsWith(key.toLowerCase())) {
				res = fileTypeMap.get(key);
				break;
			}
		}
		return res != null ? res.toLowerCase() : null;
	}
	
	int getTypeCount(String type) {
		int count = 0;
		Iterator<String> valueIter = fileTypeMap.values().iterator();
		while (valueIter.hasNext()) {
			String temp = valueIter.next();
			if(temp.equals(type)) count++;
		}
		return count;
	}
}
