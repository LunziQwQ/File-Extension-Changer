package sample;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Controller {
	private FileType fileType = new FileType();
	private MachineLearning ml = new MachineLearning();
	
	@FXML
	private TextArea console;
	
	@FXML
	private TextField filePath;
	
	
	@FXML
	private void getFileType() {
		String path = filePath.getText();
		File temp = new File(path);
		if (temp.isDirectory()) console.setText("该路径为文件夹，不存在后缀名");
		else if (!temp.exists()) console.setText("该路径不存在，请检查是否输入正确");
		else {
			String res = fileType.getFileType(path);
			console.setText(res != null ? "该文件的类型可能为： " + res : "未能成功获取文件类型");
		}
	}
	
	private int fileCount = 0;
	
	@FXML
	void learnThePath() {
		ml.learnCount = 0;
		fileCount = 0;
		console.setText("");
		String path = filePath.getText();
		File temp = new File(path);
		if (!temp.exists()) {
			console.setText("该路径不存在，请检查是否输入正确");
		} else if (!temp.isDirectory()) {
			console.appendText(ml.learnFile(temp));
		} else {
			dfsLearn(temp);
			console.appendText("\n========================\n" +
					"扫描共计" + fileCount + "个文件\n" +
					"成功学习共计" + ml.learnCount + "种文件特征");
		}
		try {
			fileType.saveFileTypeMap();
			fileType.loadFileTypeMap();
		} catch (IOException ioe) {
			console.appendText("\n文件类型数据存储失败");
			ioe.printStackTrace();
		}
	}
	
	private List<File> dfsLearn(File file) {
		List<File> fileList = new ArrayList<>();
		
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				fileList.addAll(dfsLearn(item));
			}
		} else {
			fileCount++;
			console.appendText(ml.learnFile(file));
		}
		return fileList;
	}
	
	@FXML
	private void printMap() {
		console.setText(fileType.printFileTypeMap());
		console.appendText(" ");
	}
	
	@FXML
	private void openTheMapFile() {
		try {
			Desktop.getDesktop().open(new File(Config.FILE_TYPE_DATA_PATH));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			console.setText("文件类型库丢失或损坏");
		}
	}
	
	@FXML
	private void getFilePath(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasFiles()) {
			try {
				File file = db.getFiles().get(0);
				if (file != null) {
					filePath.setText(file.getAbsolutePath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void initialize(){
		console.textProperty().addListener(
				(ChangeListener<Object>) (observableValue, oldValue, newValue)
						-> console.setScrollTop(Double.MAX_VALUE));
		
		console.appendText("Welcome to FileTypeGetter\n" +
				"Made by Lunzi 2017.8\n\n" +
				"拖拽文件进入窗口即可填充路径\n" +
				"点击右下角的Run it 按钮选择功能\n\n");
		try {
			fileType.loadFileTypeMap();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			console.appendText("文件类型库缺失或出错");
		}
	}
}
