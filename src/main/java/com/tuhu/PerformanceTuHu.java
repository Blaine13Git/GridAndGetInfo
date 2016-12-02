package com.tuhu;

import org.testng.annotations.Test;

import baseClass.AppUiBaseDevice;

public class PerformanceTuHu extends AppUiBaseDevice{
	
	@Test
	public void toGetData(){
		toClick("换轮胎", 0);
		
		int x = getScreenWidth()/2;
		int y = getScreenHeight()/2;
		
		swipe_for_height(y/3);
		
		clickXY(x, y);
		
		toClick("立即购买", 0);
		//toClick("提交订单", 0);
		
		//toClick("查看订单", 0);
		//toClick("取消订单", 0);
		
		//toClick("确认取消", 0);
		
		String activity = getActivity();
		while(!activity.contains("TuHuTabActivity") & !activity.contains("Launcher")){
			back();
			activity = getActivity();
		}
		
		toClick("我的", 0);
		toClick("全部订单", 0);
		back();
		swipe_for_height(50);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
