package lwg.nwpu.tc;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class MainGUI implements ActionListener {
	static String dll_path = "";
	static String data_name = "";
	static int boxLength = 1201;
	static int boxWidth = 235;
	
	static String result="";
	static String ref="";
	
	static JFrame frame;
	static JTextField boxLengthText=new JTextField(8);
	static JTextField boxWidthText=new JTextField(8);
	static JTextField dataPathText=new JTextField(15);
	static JButton dataPathBt=new JButton("Choose");
	static JButton runBt=new JButton("Run");
	
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
	
	public static void run() throws InvalidFormatException, IOException{
		ExcelParser excelParser = new ExcelParser(data_name);
		Rect[] rect = excelParser.getRect();//(Rect[]) new Rect().toArray(500);
		DLLLoader.Instance.calculate(excelParser.size, rect, boxLength, boxWidth);
		opeanBrowser();
	}
	
	public static void main(String[] args){
		initDllPath();
		initFrame();
	}
	public static void opeanBrowser(){
		//判断当前系统是否支持Java AWT Desktop扩展
        if(java.awt.Desktop.isDesktopSupported()){
            try{
                //创建一个URI实例,注意不是URL
                java.net.URI uri=java.net.URI.create("http://127.0.0.1/Rectangular/Viewer.html");
                //获取当前系统桌面扩展
                java.awt.Desktop dp=java.awt.Desktop.getDesktop();
                //判断系统桌面是否支持要执行的功能
                if(dp.isSupported(java.awt.Desktop.Action.BROWSE)){
                    //获取系统默认浏览器打开链接
                    dp.browse(uri);
                }
            }catch(java.lang.NullPointerException e){
                //此为uri为空时抛出异常
            }catch(java.io.IOException e){
                //此为无法获取系统默认浏览器
            }
        }
	}
	public static void initFrame(){
		frame =new JFrame("Rectangular Pack");
		frame.setSize(450,180);
		frame.setLocationRelativeTo(null); 
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
		
		JPanel p = new JPanel();
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		
		FlowLayout fl=new FlowLayout();
		p1.setLayout(fl);
		p2.setLayout(fl);
		p3.setLayout(fl);	
		
		JLabel boxLengthLabel=new JLabel("Box Lenth:     ");
		JLabel boxWidthLabel=new JLabel("Box Width:     ");
		JLabel dataPathLabel=new JLabel("Data File Path(*.xls):     ");
		
		dataPathText.setEditable(false);
		
		p1.add(boxLengthLabel);
		p1.add(boxLengthText);
		p1.add(boxWidthLabel);
		p1.add(boxWidthText);
		
		p2.add(dataPathLabel);
		p2.add(dataPathText);
		p2.add(dataPathBt);
		
		p3.add(runBt);
		
		MainGUI action=new MainGUI();
		dataPathBt.addActionListener(action);
		runBt.addActionListener(action);
		
		p.setLayout(new GridLayout(3,1));
		p.add(p1);
		p.add(p2);
		p.add(p3);		
		frame.setContentPane(p);
		frame.setVisible(true);
	}
	public void printInfo(ExcelParser.InfoLevel level,String info)
	{
		
		String val=level.getName()+info;
		JOptionPane.showMessageDialog(frame.getContentPane(),
					 info, level.getName(), JOptionPane.INFORMATION_MESSAGE);
		
	}
	public  String getFile()
	{
		JFileChooser jf = new JFileChooser(".");  
		
		ExcelFileFilter excelFilter = new ExcelFileFilter(); 
		jf.addChoosableFileFilter(excelFilter);  
		jf.setFileFilter(excelFilter);  
		  
		jf.setFileSelectionMode(JFileChooser.FILES_ONLY);  
		if(jf.showDialog(null,null)!=JFileChooser.APPROVE_OPTION)
			return "";
	
		File fi = jf.getSelectedFile(); 
		String filename=fi.getAbsolutePath();
	    if(!fi.exists() )
	    {	
	    	printInfo(ExcelParser.InfoLevel.Error, "File:"+filename+" is not exist!");
	    	return "";
	    }
	    if(!filename.endsWith(".xls"))
	    {
	    	printInfo(ExcelParser.InfoLevel.Error, "File:"+filename+" format is not xlsx,this program only accept xlsx file!");
	    	return "";
	    }
	   
		return fi.getAbsolutePath();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==dataPathBt)
		{
			data_name=getFile();
			dataPathText.setText(data_name);			
		}
	    else if(e.getSource()==runBt)
		{
	    	if(boxLengthText.getText().equals("") ||boxWidthText.getText().equals("")||data_name.equals(""))
			{
				JOptionPane.showMessageDialog(frame.getContentPane(),
						 "the Data(or Box Length,or Box Width) is null", "Error", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
	    	boxLength=Integer.parseInt(boxLengthText.getText());
	    	boxWidth=Integer.parseInt(boxWidthText.getText());
	    	try {
				run();
			} catch (InvalidFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
class ExcelFileFilter extends FileFilter {    
    public String getDescription() {    
        return "*.xls";    
    }    
    
    public boolean accept(File file) {    
        String name = file.getName();    
        return file.isDirectory() || name.toLowerCase().endsWith(".xls"); //name.toLowerCase().endsWith(".xls") || 
    }    
} 
