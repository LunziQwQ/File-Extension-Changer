package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Controller {
	private FileType fileType;
	public static String path = "000";
	public Controller() {
		fileType = new FileType();
	}
	
	@FXML
	private TextArea console;
	
	@FXML
	private Button getExtName;
	
	@FXML
	private TextField filePath;
	
	@FXML
	private Button printMap;
	
	@FXML
	private void getFileType() {
	
	}
	
	@FXML void learnThePath() throws Exception{
		//TODO:
		fileType.saveFileTypeMap();
		console.setText("保存成功");
	}
	
	@FXML
	private void printMap() {
		try {
			fileType.loadFileTypeMap();
			console.setText(fileType.printFileTypeMap());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			console.setText("文件类型库缺失或出错");
		}
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
}
