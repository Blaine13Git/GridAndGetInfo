package baseClass;

public class FCTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		PerformanceData pd = new PerformanceData();
//		pd.getCSVFile("qb_total_memSize.txt","totalMem_test.csv");
		
		ExecCmd.toExecCmd("adb shell top -d 0.05 -n 1 | grep com.twl.qichechaoren_business$");

	}

}
