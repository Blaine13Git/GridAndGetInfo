::修改端口号-p，-bp
::修改设备名称--udid
::修改初始化参数--nodeconfig
::修改日志存放位置-g
::可是使用set对以上内容进行参数化

cd d:
cd D:\Work\programs\Appium\node_modules\appium\bin

node appium.js -a localhost -p 4568 --udid d2372d55 -bp 5558 --nodeconfig D:\Work\scripts\bats\GridHub_Node\Grid_Jsons\nodeconfig_01.json -g D:\Work\logs\log_02.log --session-override