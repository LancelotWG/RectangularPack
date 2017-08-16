package lwg.nwpu.tc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {
	static int Sheet = 0;
	static String Sheet_Name = "";
	static int Header_Row = 2;
	static int Footer_Row = 0;
	static int Index_Cell = 0;
	static int Length_Cell = 1;
	static int Width_Cell = 2;
	static private Rect[] rect;
	private ArrayList<Rect> arrayRect = new ArrayList<Rect>();
	public int size = 0;
	enum InfoLevel {
		Info, Warn, Error;
		public String getName() {
			return this.name() + ":";
		}
	}
	
	public  ExcelParser(String file) throws InvalidFormatException, IOException {
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		Sheet sheet = wb.getSheetAt(Sheet);
		for (int i = Header_Row; i <= sheet.getLastRowNum() - Footer_Row; i++) {
			Row row = sheet.getRow(i);
			Cell indeCell = row.getCell(Index_Cell);
			if(checkCell(indeCell)){
				continue;
			}
			int index = (int) indeCell.getNumericCellValue();
			Cell lengthCell = row.getCell(Length_Cell);
			if(checkCell(lengthCell)){
				continue;
			}
			int length = (int) lengthCell.getNumericCellValue();
			Cell widthCell = row.getCell(Width_Cell);
			if(checkCell(widthCell)){
				continue;
			}
			int width = (int) widthCell.getNumericCellValue();
			Rect rect = new Rect();
			rect.index = index;
			rect.length = length;
			rect.width = width;
			arrayRect.add(rect);
		}
		this.size = arrayRect.size();
		this.rect = (Rect[]) new Rect().toArray(size);
		for (int i = 0;i < arrayRect.size(); i++) {
			rect[i].index = arrayRect.get(i).index;
			rect[i].length = arrayRect.get(i).length;
			rect[i].width = arrayRect.get(i).width;
		}
	}
	public void createRectangular(Rect[] rect){
		for (int i = 0;i < arrayRect.size(); i++) {
			rect[i].index = arrayRect.get(i).index;
			rect[i].length = arrayRect.get(i).length;
			rect[i].width = arrayRect.get(i).width;
		}
	}
	private boolean checkCell(Cell cell){
		boolean result = true;
		String value = "";
        if(cell != null){
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                value = String.valueOf((int) cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            //case Cell.CELL_TYPE_BLANK:
            //    break;
            default:
                break;
            }
            if(!value.trim().equals("")){
                result = false;              
            }
        }
		return result;
	}
	public Rect[] getRect() {
		return rect;
	}
}
