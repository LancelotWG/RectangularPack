package lwg.nwpu.tc;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class Rect extends Structure {
	public int length;
	public int width;
	public static class ByReference extends Rect implements Structure.ByReference{}
	public static class ByValue extends Rect implements Structure.ByValue{}
	@Override
	protected List getFieldOrder(){
		return Arrays.asList(new String[]{"length","width"});
	}
}
