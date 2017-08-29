package sample;

import java.io.File;

/**
 * ***********************************************
 * Created by Lunzi on 8/29/2017.
 * Just presonal practice.
 * Not allowed to copy without permission.
 * ***********************************************
 */
public class MachineLearning {
	private FileType fileType = new FileType();
	int learnCount = 0;
	
	
	String learnFile(File file) {
		String res = "\n";
		String[] temp = file.getName().split("\\.");
		String extName = temp[temp.length - 1].toLowerCase();
		String fileCode = fileType.getFileCode(file.getAbsolutePath());
		res += fileCode + " = " + extName;
		if (!extName.equals(fileType.getFileType(file.getAbsolutePath()))) {
			boolean getNewItem = addItemToMap(fileCode, extName);
			res += getNewItem ? "  --> 学习成功" : "";
			learnCount += getNewItem ? 1 : 0;
		}
		return res;
	}
	
	
	private boolean addItemToMap(String src, String extName) {
		if (fileType.getTypeCount(extName) < 5) {
			FileType.fileTypeMap.put(src, extName);
			return true;
		}
		return false;
	}
}
