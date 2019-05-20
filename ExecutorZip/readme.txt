双击Start.bat文件运行
初次使用时，ip选项和包选项数据为空，需要手动添加。添加一次后，下次启动会读取以前添加过的数据。
若要批量添加候选数据，自行编辑config目录下的文件：
ip.txt ----- ip选择的数据(支持多条数据)
packages.txt ----- 包名选择的数据(支持多条数据)
startPage.txt ----- 需要启动的SplashActivity数据(只支持一条数据)

Start按钮右侧的文本框：用于确定启动的Activity  填写完整的Activity文件路径(不需要包名) 如：com.aaa.bbb.SplashActivity
若未设置，则显示所有Launcher应用待选择。
