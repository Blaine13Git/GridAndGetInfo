package baseClass;

import org.testng.annotations.Listeners;

import java.io.FileInputStream;
import java.util.Properties;

import com.common.ats.CsvProviderUtils.CsvDataProvideBase.CsvDataProvideBase;
import com.common.ats.TCRunConfigManage.TCRuningListener;

@Listeners(TCRuningListener.class)
public class CarmanLocalBase extends CsvDataProvideBase {

	public CarmanLocalBase() {
		super();
	}

	public static Properties getProperies(int num) {
		String evn = "test";
		if (num == 1) {
			evn = "integrate";
		} else if (num == 2) {
			evn = "test";
		} else if (num == 3) {
			evn = "dev";
		}
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/ENV/" + evn + ".properties");
			prop.load(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;

	}
}
