::执行文件的路径，或者添加到path中
::修改端口号-p，-bp
::修改设备名称--udid
::修改初始化参数--nodeconfig
::修改日志存放位置-g
::可是使用set对以上内容进行参数化

set appium_dir=D:\Work\programs\Appium\node_modules\appium\bin
set node_ip=localhost
set node_port=4567
set device=NX505J
set bp_port=5557
set node_config_file=D:\Work\scripts\bats\GridHub_Node\Grid_Jsons\nodeconfig_01.json
set log_file=D:\Work\scripts\bats\GridHub_Node\Logs\log_01.log

cd %appium_dir%

node appium.js -a %node_ip% -p %node_port% --udid %device% -bp %bp_port% --nodeconfig %node_config_file% -g %log_file% --session-override