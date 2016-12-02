package qccr.performance.b;

import org.testng.annotations.Test;

import baseClass.AppUiBaseDevice;
import baseClass.ExecCmd;
import io.appium.java_client.android.AndroidKeyCode;
import qccr.pageObject.b.BottomBar;

public class MonkeyPerformance extends AppUiBaseDevice {
	
	//@Test(dataProvider = "oCsvDataProviderConfig")
	@Test
	public void b_monkey(){
		
		if(!(isFind(BottomBar.STORE, 0))){
			getDriver().pressKeyCode(AndroidKeyCode.BACK);
		}
		
		System.out.println("Monkey test~~~~");

		final String monkeyinfo = "adb shell monkey -p com.twl.qichechaoren_business "
				+ "-v --ignore-timeouts --ignore-security-exceptions --ignore-crashes "
				+ "--kill-process-after-error  --pct-appswitch 10 --pct-touch 30 "
				+ "--pct-trackball 10 --pct-motion 25 --pct-syskeys  10 --pct-anyevent 15 --throttle 300 200 >> /sdcard/qb_monkey.txt";
		
		ExecCmd.toExecCmd(monkeyinfo);

	}

}
