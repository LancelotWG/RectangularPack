package lwg.nwpu.tc;

import java.io.File;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class MainGUI {
	static String dll_path = "";
	// 定义接口CLibrary，继承自com.sun.jna.Library
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
	
	public static void main(String[] args) {
		initDllPath();
		Rect[] rect = (Rect[]) new Rect().toArray(200);
		System.out.println(dll_path);
		DLLLoader.Instance.calculate(200, rect, 1201, 235);
	}

}
