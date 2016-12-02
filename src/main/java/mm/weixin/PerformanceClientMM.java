package mm.weixin;

import org.testng.annotations.Test;

import baseClass.AppUiBaseDevice;

public class PerformanceClientMM extends AppUiBaseDevice {
	
	//@Test(dataProvider = "oCsvDataProviderConfig")
	@Test
	public void b_performance(){
		
		//简单的操作步骤，页面切换
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getEle("微信", 0).click();
		getEle("通讯录", 0).click();
		getEle("发现", 0).click();
		getEle("我", 0).click();
		
		getEle("通讯录", 0).click();
		rollTo("For you").click();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
