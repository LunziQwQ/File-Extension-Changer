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
	
	//软件欢迎文本
	static String WELCOME_TEXT = "Welcome to File Extension Changer\n" +
			"version: 1.1\n" +
			"Made by Lunzi 2017.8\n\n" +
			"拖拽文件进入窗口即可填充路径\n" +
			"点击右下角的Run it 按钮选择功能\n\n";
}
