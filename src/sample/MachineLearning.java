package sample;

import java.io.File;

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
	
	
	String learnFile(File file) {
		String res = "\n";
		String[] temp = file.getName().split("\\.");
		String extName = temp[temp.length - 1].toLowerCase();
		if (extName.length() > 20)
			return "\n无特征文本文件";
		for (String s : Config.MLIgnoreThpe) {
			if (s.equals(extName)) {
				return "\n无特征文本文件";
			}
		}
		String fileCode = fileType.getFileCode(file.getAbsolutePath());
		res += fileCode + " = " + extName;
		String getType = fileType.getFileType(file.getAbsolutePath());
		if (getType == null || !getType.contains(extName)) {
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
