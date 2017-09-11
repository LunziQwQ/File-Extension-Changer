package sample;

/**
 * ***********************************************
 * Created by Lunzi on 8/27/2017.
 * Just presonal practice.
 * Not allowed to copy without permission.
 * ***********************************************
 */
class Config {
	//特征数据文件路径
	static String FILE_TYPE_DATA_PATH = "extNameData.txt";
	
	//忽略的纯文本文件类型（无统一特征
	static final String[] MLIgnoreThpe =
			{"txt", "bat", "cfg", "css", "conf", "data", "license", "info", "ini", "log", "png", "sys"};
}
