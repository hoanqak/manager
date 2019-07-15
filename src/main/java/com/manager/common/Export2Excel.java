package com.manager.common;

import com.manager.data.Position;
import com.manager.model.TotalWorkingDay;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Export2Excel {
	private static final int COLUMN_INDEX_USERID = 0;
	private static final int COLUMN_INDEX_NAME = 1;
	private static final int COLUMN_INDEX_POSITION = 2;
	private final String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL",
			"MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

	private static final Logger logger = LoggerFactory.getLogger(Export2Excel.class);

	public void writeExcel(List<TotalWorkingDay> workingDays, String excelFilePath) throws Exception {
//		create workbook
		Workbook workbook = getWorkbook(excelFilePath);
//		create sheet
		Sheet sheet = workbook.createSheet(months[workingDays.get(0).getMonth()]);

		int rowIndex = 0;
		int month = workingDays.get(0).getMonth() + 1;
		addStyleSheet(workbook, sheet, month);
		rowIndex++;
		writeHeader(sheet.createRow(rowIndex));
		rowIndex++;
		for (TotalWorkingDay totalWorkingDay : workingDays) {
			Row row = sheet.createRow(rowIndex);
			writeData(totalWorkingDay, row);
			rowIndex++;

		}

		createOutputFile(workbook, excelFilePath);

	}

	private Workbook getWorkbook(String excelFilePath) {
		Workbook workbook;
		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook();
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook();
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}
		return workbook;
	}

	private void writeData(TotalWorkingDay totalWorkingDay, Row row) {

		Cell cell = row.createCell(COLUMN_INDEX_USERID);
		cell.setCellValue(totalWorkingDay.getUserId());

		cell = row.createCell(COLUMN_INDEX_NAME);
		cell.setCellValue(totalWorkingDay.getName());

		cell = row.createCell(COLUMN_INDEX_POSITION);
		try {
			int index = totalWorkingDay.getPosition();
			cell.setCellValue(Position.values()[index].toString());

		} catch (Exception e) {
			logger.error("ArrayIndexOutOfBoundsException: ");
		}


		int totalDay = 31;
		int month = totalWorkingDay.getMonth() + 1;

//		Map<String, Integer> days có key theo format: "d/M" ( trong do: d la ngay trong thang, M la so thu tu thang).
		for (int i = 1; i <= totalDay; i++) {
			cell = row.createCell(i + COLUMN_INDEX_POSITION);
			if (totalWorkingDay.getDays().get(i + "/" + month) != null) {
				cell.setCellValue(totalWorkingDay.getDays().get(i + "/" + month));
			} else {
				cell.setCellValue(0);
			}


		}

		cell = row.createCell(COLUMN_INDEX_POSITION + totalDay + 1);
		double total = totalWorkingDay.getTotal();
		cell.setCellValue(total);

//		cell = row.createCell(COLUMN_INDEX_POSITION + totalDay + 1 + 1, CellType.FORMULA);
//		cell.setCellFormula();

	}

	private void writeHeader(Row row) {
		Cell cell = row.createCell(COLUMN_INDEX_USERID);
		cell.setCellValue("ID");

		cell = row.createCell(COLUMN_INDEX_NAME);
		cell.setCellValue("Họ và tên");

		cell = row.createCell(COLUMN_INDEX_POSITION);
		cell.setCellValue("Bộ phận/Chức vụ");

		final int totalDay = 31;

		for (int i = 1; i <= totalDay; i++) {
			cell = row.createCell(i + COLUMN_INDEX_POSITION);
			cell.setCellValue(i);
		}

		cell = row.createCell(COLUMN_INDEX_POSITION + totalDay + 1);
		cell.setCellValue("Tổng ngày công");

		cell = row.createCell(COLUMN_INDEX_POSITION + totalDay + 1 + 1);
		cell.setCellValue("Tổng đối chiếu");


	}

	private void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {

		OutputStream os = new FileOutputStream(excelFilePath);
		workbook.write(os);
	}

	private void addStyleSheet(Workbook workbook, Sheet sheet, int month) {
		final int lastColumn2Merge = 35;
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastColumn2Merge));
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		String title = "BẢNG CHẤM CÔNG THÁNG " + month;
		cell.setCellValue(title);

		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setBold(true);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		cell.setCellStyle(style);
	}

}
