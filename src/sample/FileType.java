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
	public Map<String, String> fileTypeMap = new HashMap<String, String>();
	
	public void loadFileTypeMap() throws FileNotFoundException, IOException {
		File file = new File(Config.FILE_TYPE_DATA_PATH);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp;
		while ((temp = br.readLine()) != null) {
			fileTypeMap.put(temp.split("=")[0], temp.split("=")[1]);
		}
		br.close();
	}
	
	public void saveFileTypeMap() throws IOException {
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
		Collections.sort(sortMap, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
		return sortMap;
	}
	
	public String printFileTypeMap() {
		StringBuilder sb = new StringBuilder();
		sortFileTypeMap().forEach(item -> sb.append(item).append("\n"));
		return sb.toString();
	}
	
	
	public String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}//如果hv长度不够2了就不添加进stringBuilder
			stringBuilder.append(hv);
		}//遍历src字节信息转换橙16进制
		return stringBuilder.toString();//加入美
	}//获取16进制的头信息
	
	
	public String getFileType(String filePath) {
		String res = null;
		try {
			FileInputStream is = new FileInputStream(filePath);
			byte[] b = new byte[10];
			is.read(b, 0, 10);
			String fileCode = bytesToHexString(b);//获取的是16进制的b数组的信息
			System.out.println(fileCode);//输出的是16进制的信息 filecode是转换完的16进制信息
			//这种方法在字典的头代码不够位数的时候可以用但是速度相对慢一点
			Iterator<String> keyIter = fileTypeMap.keySet().iterator();//建立File_TYPE_MAP的迭代器
			is.close();
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				if (key.toLowerCase().startsWith(fileCode.toLowerCase())) {
					//toLowerCase是转换成小写字母，判断相互迭代？？？两个判断删掉另一个都能正确运行程序
					res = fileTypeMap.get(key);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
		
	}
}
