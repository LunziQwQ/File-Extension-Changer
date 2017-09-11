# File-Extension-Changer
智能识别文件类型并补全后缀名

> 为了完成千里码题目[缺失的后缀](http://www.qlcoder.com/task/7593)而完成简单小工具  
> 基于文件头数据特征而判断文件类型
> 可通过学习本地已知类型文件补充自身数据
> 欢迎大家批评指正

`创建于2017年8月`  

---

##说明
使用了JavaFx开发的窗口应用程序，由于使用了Lambda语法，您需要 **Java8或以上** 的Java环境来运行。  

**运行程序请直接下载解压Release.zip。 命令行使用：**  

```
java -jar FileExtensionChanger.jar
```
**运行时请保证zip中的类型特征库 *extNameData.txt* 与 jar文件同目录。**

##功能介绍
* **识别文件类型**：将要识别的文件拖拽进窗口或在下方路径栏中输入文件路径，点击识别文件类型。  
* **学习文件类型**：将确认类型正确的文件或文件夹拖拽进窗口或在下方路径栏中输入文件轮径，点击学习文件类型。将根据现有已知类型的文件学习补充类型特征库。
* **输出特征库**：在窗口中输出当前特征库的特征数据。
* **编辑特征库**：调用系统默认文本编辑器打开文件特征库。