package baseClass;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.common.ats.LoggerUtils.TcRunLog;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import baseClass.CarmanLocalBase;
/**
 * Grid module
 *     
 * 项目名称：GridAutoTest    
 * 类名称：AppUiBaseDevice    
 * 类描述：    
 * 创建人：user    
 * 创建时间：2016年6月30日 下午4:47:35    
 * 修改人：user    
 * 修改时间：2016年6月30日 下午4:47:35    
 * 修改备注：    
 * @version     
 *
 */
public class AppUiBaseDevice extends CarmanLocalBase {
	
	// 切换环境使用：1是线上 2是测试环境 3是开发环境
	public static Properties oProperties = getProperies(2);
	public static DesiredCapabilities desiredCapabilities;
	public static ThreadLocal<AndroidDriver<WebElement>> threadLocal = new ThreadLocal<>();
	public static PerformanceData performanceData = new PerformanceData();
	
	/**
	 * 连接设备、启动应用
	 */
	@Parameters({"device", "app", "appPackage", "appActivity", "url"})
	@BeforeTest(alwaysRun = true)
	public static void initConnection(String device, String app, String appPackage, String appActivity, String url) throws Exception {
		
		
		
		desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device);
		desiredCapabilities.setCapability(MobileCapabilityType.APP, app);

		// 不重置app信息
		desiredCapabilities.setCapability("noReset", "true");

		// 设置可以输入中文的属性
		desiredCapabilities.setCapability("unicodeKeyboard", "true");
		desiredCapabilities.setCapability("resetKeyboard", "true");

		// 延长等待时间
		desiredCapabilities.setCapability("newCommandTimeout", "300");

		// 忽略不重要的无关元素
		desiredCapabilities.setCapability("ignoreUnimportantViews", "true");
		
		//webView debug
		desiredCapabilities.setCapability("setWebContentsDebuggingEnabled","true");

		// 新建驱动
		AndroidDriver<WebElement>  androidDriver = new AndroidDriver<WebElement>(new URL(url), desiredCapabilities);
		setDriver(androidDriver);

		TcRunLog.info("=========连接了设备===========");
		
		//判断是否安装了app
		Boolean flag = getDriver().isAppInstalled(appPackage);
		if(!flag){
			TcRunLog.info("======install app======");
			getDriver().installApp(app);
		}
		
		TcRunLog.info("======app installed ======");
		
		getDriver().startActivity(appPackage, appActivity);
		
		performanceData.getData(appPackage);
		
		getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		TcRunLog.info("～～～～～～启动app～～～～～～～");
	}
	/**
	 * 退出驱动
	 */
	@AfterTest
	public void tearDown() {
		getDriver().quit();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		performanceData.getCSVFile(performanceData.totalMemoryFileName,"totalMem.csv");
		performanceData.getCSVFile(performanceData.cpuFileName,"cpuData.csv");
//		performanceData.getCSVFile(performanceData.nativeFileName,"nativeMem.csv");
//		performanceData.getCSVFile(performanceData.dalvikFileName,"dalvikMem.csv");

	}

	public static void setDriver(AndroidDriver<WebElement> driver) {
		threadLocal.set(driver);
	}

	public static AndroidDriver<WebElement> getDriver() {
		return threadLocal.get();
	}

	/**
	 * 截图，保存
	 */
	public static void takeScreenShot() {

		// 格式化截图命名前缀
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String dateStr = simpleDateFormat.format(Calendar.getInstance().getTime());

		// 截图
		File picDir = new File("screenshots");
		File screenShot = getDriver().getScreenshotAs(OutputType.FILE);
		String screenShotName = picDir.getAbsolutePath() + File.separator + dateStr + ".png";
		try {
			FileUtils.copyFile(screenShot, new File(screenShotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取模糊的元素
	 */
	public static WebElement getEleTextContains(String text, int instance) {
		String using = "new UiSelector().textContains(\"" + text + "\").instance(" + instance + ")";
		return getDriver().findElementByAndroidUIAutomator(using);
	}

	/**
	 * 获取单个元素 uiautomator get element
	 * 
	 * @param source,定位的资源text,id,class
	 *            instance,资源的绝对index，从0开始
	 */
	public static WebElement getEle(String source, int instance) {
		// android.widget.TextView
		// com.twl.qichechaoren_business:id/product_type_tv
		String flag = source.split("\\.")[0];
		String using = "";
		switch (flag) {
		case "android":
			using = "new UiSelector().className(\"" + source + "\").instance(" + instance + ")";
			return getDriver().findElementByAndroidUIAutomator(using);
		case "com":
			using = "new UiSelector().resourceId(\"" + source + "\").instance(" + instance + ")";
			return getDriver().findElementByAndroidUIAutomator(using);
		default:
			using = "new UiSelector().text(\"" + source + "\").instance(" + instance + ")";
			return getDriver().findElementByAndroidUIAutomator(using);

		}
	}

	/**
	 * 获取元素列表 uiautomator get elements
	 * 
	 * @param source,定位的资源text,id,class
	 *            instance,资源的绝对index，从0开始
	 */
	public static List<WebElement> getEles(String source) {
		// android.widget.TextView
		// com.twl.qichechaoren_business:id/product_type_tv
		String flag = source.split("\\.")[0];
		String using = "";
		switch (flag) {
		case "android":
			using = "new UiSelector().className(\"" + source + "\")";
			return getDriver().findElementsByAndroidUIAutomator(using);
		case "com":
			using = "new UiSelector().resourceId(\"" + source + "\")";
			return getDriver().findElementsByAndroidUIAutomator(using);
		default:
			using = "new UiSelector().textContains(\"" + source + "\")";
			return getDriver().findElementsByAndroidUIAutomator(using);
		}
	}
	
	//获取元素的文本
	public static String getEle_Text(String source ,int instance){
		return getEle(source, instance).getText();
	}
	
	/**
	 * 判断对象是否存在的
	 */
	public static boolean isFind(String source, int instance) {
		boolean flag = true;
		try {
			getEle(source, instance);
			flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
		}
		return flag;
	}

	/**
	 * 根据坐标点击
	 */
	public static TouchAction clickXY(int x, int y) {
		return new TouchAction(getDriver()).press(x, y).release().perform();
	}

	/**
	 * 获取屏幕的宽度
	 */
	public static int getScreenWidth() {
		return getDriver().manage().window().getSize().getWidth();
	}

	/**
	 * 获取屏幕的高度
	 */
	public static int getScreenHeight() {
		return getDriver().manage().window().getSize().getHeight();
	}

	/**
	 * 坐标元素触摸点击
	 * 
	 * @param s
	 *            :source
	 */
	public static void clickEle(String s) {
		// int scrHeight = driver.manage().window().getSize().getHeight();
		// int scrWidth = driver.manage().window().getSize().getWidth();
		WebElement ele = getEle(s, 0);
		Dimension ele_size = ele.getSize();
		int x = ele_size.getWidth();
		int y = ele_size.getHeight();
		Point ele_p = ele.getLocation();
		new TouchAction(getDriver()).press(ele_p.x + x / 2, ele_p.y + y / 2).release().perform();
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	

	/**
	 * 同行元素组件 定位元素相对位置，比较两个元素的右下角和左上角的差值获得是否在同一个组件内
	 * 
	 */
	public boolean theSameComponent(String source1, int instance1, String source2, int instance2) {
		// 获取两个元素
		WebElement ele_01 = getEle(source1, instance1);
		WebElement ele_02 = getEle(source2, instance2);

		// 获取元素尺寸
		Dimension ele_size = ele_01.getSize();
		int x = ele_size.getWidth();
		int y = ele_size.getHeight();

		// 两个元素的左上角
		Point ele_01_point = ele_01.getLocation();
		Point ele_02_point = ele_02.getLocation();

		// 获取右下角的位置
		int xx = ele_01_point.getX() + x;
		int yy = ele_01_point.getY() + y;

		int e2x = ele_02_point.getX();
		int e2y = ele_02_point.getY();

		if (xx - e2x < 0 & yy - e2y < 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 滚动到指定的字符串
	 */
	public static WebElement rollTo(String name) {
		return getDriver().scrollToExact(name);
	}
	
	/**
	 * 滚动到指定的字符串并点击
	 */
	public static void rollToAndClick(String name) {
		getDriver().scrollToExact(name).click();
	}

	// 实现最大延迟10秒的功能
	public static void toClick(String source, int instance) {
		getEle(source, instance).click();
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	//去掉详情页的头图
	public static void trimTitlePic(){
		WebElement ele = getEle("android.widget.ImageView", 1);
		Dimension dXY = ele.getSize();
		int height_Y = dXY.getHeight();
		Point e_XY = ele.getLocation();
		getDriver().swipe(e_XY.x, e_XY.y + height_Y-20, e_XY.x, e_XY.y, 3000);
	}
	
	/**
	 * longPress(长按屏幕操作)
	 * @author wangmingding
	 * @param source：界面元素,
	 * @param instance：元素索引
	 * @param presstime：长按时间，以秒为单位
	 */
	public static void longPress(String source, int instance, int presstime) {
		WebElement ele = getEle(source, instance);
		int nSecond = presstime * 1000;// 毫秒转换成秒
		TouchAction action = new TouchAction(getDriver());
		action.longPress(ele, nSecond).release().perform();
	}
	
	/**
	 * 调用系统的返回操作
	 */
	public static void back(){
		getDriver().pressKeyCode(AndroidKeyCode.BACK);
	}
	
	/**
	 * 获取当前的activity
	 * @return 
	 */
	public static String getActivity(){
		return getDriver().currentActivity();
	}
	
	/**
	 * 取消订单
	 * @return 
	 */
	public static void cancel_order(){
		toClick("取消订单", 0);
		toClick("残忍抛弃", 0);
	}
	
	/**
	 * 滑动指定元素的高度（up）
	 * @param ele_source
	 * @param instance
	 */
	public static void swipe_to_element_bottom(String ele_source, int instance){
		WebElement ele = getEle(ele_source, instance);
		Dimension dXY = ele.getSize();
		int height_Y = dXY.getHeight();
		Point e_XY = ele.getLocation();
		getDriver().swipe(e_XY.x, e_XY.y + height_Y, e_XY.x, e_XY.y, 2000);
	}
	
	/**
	 * 滑动固定高度
	 * @param height （上滑：height 传正值； 下滑：height 传负值）
	 */
	public static void swipe_for_height(int height){
		int startx = 50;
		int starty = getScreenHeight()/2;
		int endx = 50 ;
		int endy =  starty-height;
		int duration = 2000;
		getDriver().swipe(startx,starty, endx, endy,duration);
	}

	
	/**
	 * 获取元素的高度
	 * @param ele_source
	 * @param instance
	 */
	public int getEle_height(String source, int instance){
		return getEle(source, instance).getSize().height;
	}
	
	/**
	 * 根据"content-desc"获取单个元素 uiautomator get element
	 * @author wangmingding
	 * @param source,instance
	 */
	public static WebElement getElementByDescription(String source, int instance) {
		String using = "new UiSelector().description(\"" + source + "\").instance(" + instance + ")";
		return getDriver().findElementByAndroidUIAutomator(using);
	}
	
    /**
     * getElementByDescContains(根据content-desc模糊获取单个元素)
     * @author zhangxinya
     * @param source,instance
     */
	public static WebElement getElementByDescContains(String source, int instance) {
		String using = "new UiSelector().descriptionContains(\"" + source + "\").instance(" + instance + ")";
		return getDriver().findElementByAndroidUIAutomator(using);
	}
	
	/**
	 * 判断对象是否存在的
	 * @author wangmingding
	 */
	public static boolean isFindByDescription(String source, int instance) {
		boolean flag = true;
		try {
			getElementByDescription(source, instance);
			flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
		}
		return flag;
	}
	
	/**
     * swipeToUp(上滑操作，自定义时间，从屏幕3/4处滑到1/4处)
     * @author wangmingding
     * @param during
     */
    public void swipeToUp(int during) {
        int width = getDriver().manage().window().getSize().getWidth();
        int height = getDriver().manage().window().getSize().getHeight();
        getDriver().swipe(width / 2, height * 3 / 4, width / 2, height / 4, during);
    }
    
    /**
     * swipeToDown(下滑操作，自定义时间，从屏幕1/4处滑到3/4处)
     * @author zhangxinya
     * @param during
     */
    public void swipeToDown(int during) {
        int width = getDriver().manage().window().getSize().getWidth();
        int height = getDriver().manage().window().getSize().getHeight();
        getDriver().swipe(width / 2, height / 4, width / 2, height * 3 / 4, during);
    }
    
	/**   
	 * isEnd(判断列表是否滑动到底)
	 * @author wangmingding 
	 */
	public boolean isEnd() {
		// 获取当前手机的分辨率
		int width = getDriver().manage().window().getSize().getWidth();
        int height = getDriver().manage().window().getSize().getHeight();
		String str1;
		String str2;
		// 滑动前获取pagesource
		str1 = getDriver().getPageSource();
		getDriver().swipe(width / 2, height * 3 / 4, width / 2, height / 4, 1000);
		// 滑动后获取pagesource
		str2 = getDriver().getPageSource();
		if(str1.equals(str2)){
			return true;
		}else{
			return false;
		}
	}
}
