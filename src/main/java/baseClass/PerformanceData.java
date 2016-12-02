package baseClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;

import com.csvreader.CsvWriter;

public class PerformanceData extends AppUiBaseDevice {

	public boolean flag = true;
	public String totalMemoryFileName = "qb_total_memSize.txt";
	public String cpuFileName = "qb_cpuUseInfo.txt";
	
//	public String nativeFileName = "qb_native_memSize.txt";
//	public String dalvikFileName = "qb_dalvik_memSize.txt";

	/*
	 * app data
	 */
	public void getData(String appPackage) {
		
		final String totalMeminfo = "adb shell dumpsys meminfo " + appPackage + " | grep 'TOTAL' >> /sdcard/"
				+ totalMemoryFileName;
		final String activityInfo = "adb shell dumpsys activity | grep mFocusedActivity >> /sdcard/"
				+ totalMemoryFileName;
		
//		final String cpuInfo = "adb shell dumpsys cpuinfo | grep \"" + appPackage + ":          \" >> /sdcard/"
//				+ cpuFileName;
		final String cpuInfo = "adb shell top -d 0.05 -n 1 | grep \"" + appPackage + "$\" >> /sdcard/"
				+ cpuFileName;
		final String activityInfo_cpu = "adb shell dumpsys activity | grep mFocusedActivity >> /sdcard/"
				+ cpuFileName;

//		 final String activityInfo = "adb shell dumpsys activity| awk '/mFocusedActivity:/{print $4}'";
//		final String nativeMeminfo = "adb shell dumpsys meminfo " + appPackage + " | grep 'Native Heap' >> /sdcard/"
//				+ nativeFileName;
//		final String dalvikMeminfo = "adb shell dumpsys meminfo " + appPackage + " | grep 'Dalvik Heap' >> /sdcard/"
//				+ dalvikFileName;

//		final String cpuInfo = "adb shell top -d 0.1 -n 1 | awk '/" + appPackage + "$/ {print $3}' >> /sdcard/"
//				+ cpuFileName;
                                                
		// 删除文件
		String deleteFile = "adb shell rm -f /sdcard/qb_*";
		ExecCmd.toExecCmd(deleteFile);

		/*
		 * 持续获取信息
		 */
		Runnable target = new Runnable() {
			@Override
			public void run() {
				// 执行脚本得到文件
				while (flag) {
					try {
//						ExecCmd.toExecCmd(cpuInfo); // cpu信息
//						ExecCmd.toExecCmd(nativeMeminfo); // bitmapMem
//						ExecCmd.toExecCmd(dalvikMeminfo); // objectMem
						synchronized(this){
							//获取内存
							ExecCmd.toExecCmd(activityInfo); //activity
							ExecCmd.toExecCmd(totalMeminfo); // totalMem
							
							//获取CPU
							ExecCmd.toExecCmd(activityInfo_cpu); //activity
							ExecCmd.toExecCmd(cpuInfo); // cpu value
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread thread = new Thread(target);
		thread.start();
	}

	/**
	 * 生成CSV文件
	 */
	public void getCSVFile(String sourceFileName, String csvFileName) {
		try {
			// 终止获取文件的线程
			flag = false;

			// 拉去文件
			String pullFile = "adb pull /sdcard/" + sourceFileName + " D:/Work/scripts/data/";
			ExecCmd.toExecCmd(pullFile);

			/*
			 * 读取文件、数据处理
			 */
			String file = "D:/Work/scripts/data/" + sourceFileName;
			FileReader fileReader = new FileReader(file);// 读取文件
			BufferedReader bufferedReader = new BufferedReader(fileReader, 8184);// 缓冲区每次读取8M
			String line = "";

			String csvFilePath = "D:/Work/scripts/data/" + csvFileName;
			CsvWriter wr = new CsvWriter(csvFilePath, ',', Charset.forName("GBK"));
			
			int line_num = 1;
			String memory_velue = null;
			String cpu_value = null;
			String activity = null;
			
			/*
			 * 处理CPU文件的逻辑和处理Total Memory的逻辑不一样
			 * 所以需要作出判断给出不同的处理。此处略if(){}
			 */
			if(sourceFileName.contains("cpu")){
				//处理CPU文件
				while((line = bufferedReader.readLine()) != null){
					if(line_num % 2 == 0){
						line = line.trim();
						cpu_value = line.split("\\s+")[2]; //使用空格进行分割
					}else{
						line = line.trim();
//						activity = line.substring(line.lastIndexOf(".")+1,line.lastIndexOf(" "));
						activity = line.substring(line.lastIndexOf("/")+1,line.lastIndexOf(" "));
					}
					
					if(line_num % 2 == 0 && line_num >=2){
						String[]  activity_cpu = {activity,cpu_value};
						wr.writeRecord(activity_cpu); // 把字符串数组写入csv文件
					}
					line_num++;
				}
			}else{
				//处理内存文件
				while ((line = bufferedReader.readLine()) != null) {
					if(line_num % 2 == 0){
						line = line.trim();
						memory_velue = line.split("\\s+")[1]; //使用空格进行分割
					}else{
						line = line.trim();
//						activity = line.substring(line.lastIndexOf(".")+1,line.lastIndexOf(" "));
						activity = line.substring(line.lastIndexOf("/")+1,line.lastIndexOf(" "));
					}
					
					if(line_num % 2 == 0 && line_num >=2){
						String[]  activity_mem = {activity,memory_velue};
						wr.writeRecord(activity_mem); // 把字符串数组写入csv文件
					}
					line_num++;
				}
			}
			wr.close();
			fileReader.close();
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
