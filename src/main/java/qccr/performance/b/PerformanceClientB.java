package qccr.performance.b;

import org.testng.annotations.Test;

import baseClass.AppUiBaseDevice;
import qccr.pageObject.b.BottomBar;
import qccr.pageObject.b.MainActivity_Purchase;
import qccr.pageObject.b.MainActivity_User;

public class PerformanceClientB extends AppUiBaseDevice {
	
	//@Test(dataProvider = "oCsvDataProviderConfig")
	@Test
	public void b_performance(){
		toClick(BottomBar.PURCHASE, 0); // 点击底部导航栏的--采购
		toClick(MainActivity_Purchase.TIRE, 0);//点击轮胎，进入轮胎列表页
		toClick(MainActivity_Purchase.GOODS_LIST,0);//选择第一个轮胎，进入详情页
		toClick(MainActivity_Purchase.BUY,0);//点击购买
		toClick(MainActivity_Purchase.COMMIT,0);//确定购买数量等信息
		toClick(MainActivity_Purchase.CONFIRM,0);//确认生成订单
		swipe_for_height(50);
		back();//返回
		toClick("确认",0);//确认返回
		
		String activity = getActivity();
		while(!activity.contains("MainActivity") & !activity.contains("Launcher")){
			back();
			activity = getActivity();
		}
		
		getEle(BottomBar.MY, 0).click(); // 点击底部导航栏的--我的
		getEle(MainActivity_User.ORDER_ALL, 0).click(); // 点击--全部
		back();//返回
		swipe_for_height(50);
		//String pageSource = getDriver().getPageSource();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
