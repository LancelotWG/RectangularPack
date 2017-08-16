package lwg.nwpu.tc;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class MainGUI {
	static String dll_path = "";
	static String data_name = "Test.xls";
	// ����ӿ�CLibrary���̳���com.sun.jna.Library
	public interface DLLLoader extends Library {
		DLLLoader Instance = (DLLLoader) Native.loadLibrary(Platform.isWindows()
				? (dll_path + "RectangularPackDLL")
				: "c", DLLLoader.class);

		public void calculate(int number, Rect[] rect, int boxLenght, int boxWidth);
	}

	private static void initDllPath() {
		String path = Rect.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		int firstIndex = path.indexOf("/") + 1;
		int lastIndex = path.lastIndexOf("/") + 1;
		path = path.substring(firstIndex, lastIndex);
		dll_path = path;
	}
	
	public static void main(String[] args) throws InvalidFormatException, IOException {
		initDllPath();
		ExcelParser excelParser = new ExcelParser(dll_path + data_name);
		Rect[] rect = excelParser.getRect();//(Rect[]) new Rect().toArray(500);
		DLLLoader.Instance.calculate(excelParser.size, rect, 1201, 235);
	}

}
