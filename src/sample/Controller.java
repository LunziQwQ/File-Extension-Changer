package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class Controller {
	private FileType fileType = new FileType();
	
	@FXML
	private TextArea console;
	
	@FXML
	private TextField filePath;
	
	@FXML
	private Label UICount;
	
	@FXML
	private MenuButton menuButton;
	
	
	@FXML
	private void getFileType() {
		UICount.setVisible(false);
		String path = filePath.getText();
		File temp = new File(path);
		if (temp.isDirectory()) console.setText("该路径为文件夹，不存在后缀名");
		else if (!temp.exists()) console.setText("该路径不存在，请检查是否输入正确");
		else {
			String res = fileType.getFileType(path);
			console.setText(res != null ? "该文件的类型可能为： " + res.replace(","," (or) ") : "未能成功获取文件类型");
		}
	}
	
	private StringProperty CountProp = new SimpleStringProperty("Count: 0");
	private int count;
	
	@FXML
	void learnThePath() {
		UICount.setVisible(true);
		new Thread() {
			private MachineLearning ml = new MachineLearning();
			@Override
			public void run() {
				menuButton.setDisable(true);
				ml.learnCount = 0;
				count = 0;
				CountProp.setValue("Count: " + count);
				Platform.runLater(() -> console.setText(""));
				String path = filePath.getText();
				File temp = new File(path);
				if (!temp.exists()) {
					Platform.runLater(() -> console.setText("该路径不存在，请检查是否输入正确"));
				} else if (!temp.isDirectory()) {
					Platform.runLater(() -> console.appendText(ml.learnFile(temp)));
				} else {
					dfsLearn(temp);
					int mergeCount = ml.mergeTypeMap();
					Platform.runLater(() -> console.appendText("\n========================\n" +
							"扫描共计" + CountProp.getValue() + "个文件\n" +
							"成功学习共计" + ml.learnCount + "条文件特征\n" +
							"智能合并共计" + mergeCount + "条文件特征"));
				}
				try {
					fileType.saveFileTypeMap();
					fileType.loadFileTypeMap();
				} catch (IOException ioe) {
					console.appendText("\n文件类型数据存储失败");
					ioe.printStackTrace();
				}
				menuButton.setDisable(false);
			}
			
			private List<File> dfsLearn(File file) {
				List<File> fileList = new ArrayList<>();
				
				if (file.isDirectory()) {
					Arrays.stream(file.listFiles()).map(this::dfsLearn).forEach(fileList::addAll);
				} else {
					if(count % 500 == 0) Platform.runLater(() -> console.setText("")); //数据量大时清空
					Platform.runLater(() -> {
						console.appendText(ml.learnFile(file));
						CountProp.setValue("Count: " + ++count);
					});
					try{
						Thread.sleep(5);
					}catch (Exception e){}
				}
				return fileList;
			}
		}.start();
	}
	
	
	
	@FXML
	private void printMap() {
		UICount.setVisible(true);
		CountProp.setValue("Count: " + FileType.fileTypeMap.size());
		console.setText(fileType.printFileTypeMap());
		console.appendText("========================\n" +
				"文件特征库共计 " + FileType.fileTypeMap.size() + " 条特征\n"
		);
	}
	
	@FXML
	private void openTheMapFile() {
		try {
			Desktop.getDesktop().open(new File(Config.FILE_TYPE_DATA_PATH));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			console.appendText("文件类型库缺失，请点击Run it菜单，然后下载最新类型库。\n");
			
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
	boolean downloadFileTypeMap(){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("下载特征库");
		alert.setHeaderText("正在试图在线下载最新的文件特征库");
		alert.setContentText("您是否要在线下载？（文件大小<10kB）\n");
		Optional result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {
				URL url = new URL("http://file.lunzi.pw/FileExtensionChanger/extNameData.txt");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				
				InputStream inputStream = connection.getInputStream();
				
				byte[] getData = readInputStream(inputStream);
				
				//文件保存位置
				File file = new File("." + File.separator + "extNameData.txt");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(getData);
				fos.close();
				inputStream.close();
				
				console.setText(Config.WELCOME_TEXT + "文件特征库下载/更新成功\n");
				fileType.loadFileTypeMap();
				console.appendText("文件特征库加载成功\n");
			} catch (IOException ioe) {
				ioe.printStackTrace();
				Alert warning = new Alert(Alert.AlertType.ERROR);
				warning.setTitle("下载特征库");
				warning.setHeaderText("文件类型库下载失败！");
				warning.setContentText("请检查您的网络连接，若您的网络正常，可能是服务器资源出现问题，请反馈至邮箱me@lunzi.space。");
				console.appendText("文件类型库下载失败，无法正常工作\n");
			}
			return true;
		} else {
			alert.close();
			console.appendText("下载已取消\n");
			return false;
		}
	}
	
	private byte[] readInputStream(InputStream is) throws IOException {
		byte[] buffer = new byte[1024];
		int size = -1;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((size = is.read(buffer)) != -1) {
			bos.write(buffer, 0, size);
		}
		bos.close();
		is.close();
		return bos.toByteArray();
	}
	
	@FXML
	public void initialize(){
		console.textProperty().addListener(
				(ChangeListener<Object>) (observableValue, oldValue, newValue)
						-> console.setScrollTop(Double.MAX_VALUE));
		UICount.textProperty().bind(CountProp);
		console.appendText(Config.WELCOME_TEXT);
		
		try {
			fileType.loadFileTypeMap();
			console.appendText("文件类型库加载成功\n");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			if(!downloadFileTypeMap()){
				console.appendText("文件类型库缺失，无法正常工作，请点击Run it菜单，然后下载最新类型库。\n");
			}
		}
	}
}
