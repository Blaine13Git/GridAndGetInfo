package diandian.storeInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import baseClass.AppUiBaseDevice;
import baseClass.ExecCmd;
import diandian.pageObject.BottomBar;
import diandian.pageObject.MainActivity_Home;
import diandian.pageObject.ServiceActivity_Clean;

public class ChediandianStore extends AppUiBaseDevice {

	@Test
	public void getStoreInfo() {
		boolean flag = isFind(BottomBar.HOME, 0);
		while (!flag) {
			flag = isFind(BottomBar.HOME, 0);
		}
		getEle(BottomBar.HOME, 0).click();
		getEle(MainActivity_Home.CLEAN, 0).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Set<String> store_name_set = new HashSet<String>(); // 保存门店的名字
		int name_times = 0; // 名字出现次数
		String last_name = ""; // 上一个门店的名字
		String the_last_name = ""; // 当前页面最后一个名字

		WebElement tips_ele = getEle(ServiceActivity_Clean.TIPS, 0);
		Point tipsXY = tips_ele.getLocation();// 获取头图元素左上角的位置
		int topX = tipsXY.getX();
		int topY = tipsXY.getY();

		// price 元素
		WebElement price_ele = getEle(ServiceActivity_Clean.PRICE, 0);
		Point priceXY = price_ele.getLocation();// 获取距离元素左上角的位置
		int priceY = priceXY.getY();

		Dimension price_size = price_ele.getSize(); // 获取元素的尺寸
		int price_higth = price_size.getHeight();
		int startY = priceY + price_higth;

		/*
		 * 需要加滑动次数统计 打印店铺名称和滑动次数 修改滑动距离
		 */
		int swipe_time = 0; // 滑动次数

		while (name_times != 2) {
			// 获取日历的实例
			Calendar cl = Calendar.getInstance();
			Date d = cl.getTime();

			// 格式化时间的输出
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
			String data_date = sdf.format(d);

			String pageSource = getDriver().getPageSource();
			ExecCmd.writeToFile("d:\\testData\\" + data_date + "_" + swipe_time + ".txt", pageSource);

			List<WebElement> store_name_list = getEles(ServiceActivity_Clean.BIZ_NAME);
			String store_name = "";
			for (int i = 0; i < store_name_list.size(); i++) {
				store_name = store_name_list.get(i).getText().trim();
				store_name_set.add(store_name);
				String temp_data = "store_name》" + store_name + "\t门店数量:" + store_name_set.size() + "\t滑动第 "
						+ swipe_time + " 次";
				System.out.println(temp_data);
				ExecCmd.writeToFile("d:\\testData\\tempData.txt", temp_data);
			}

			the_last_name = store_name_list.get(store_name_list.size() - 1).getText();
			if (last_name.equals(the_last_name)) {
				name_times++;
			} else {
				name_times = 0;
			}
			last_name = the_last_name;
			getDriver().swipe(topX + 10, startY, topX + 10, topY, 2000);
			swipe_time++;
		}
		System.out.println("门店数量::" + store_name_set.size() + "\t滑动次数::" + swipe_time);
	}
}
