package baseClass;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryTestng implements IRetryAnalyzer {
	private int retryCount = 0;// 计数器
	private int maxRetryCount = 0; // 设置最大重试次数

	public boolean retry(ITestResult result) {
		// TODO Auto-generated method stub
		if (retryCount < maxRetryCount) {
			retryCount++;
			return true;
		}
		return false;
	}
}
