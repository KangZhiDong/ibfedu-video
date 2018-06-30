package com.ibf.live.common.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileExportImportUtil {
	InputStream os;
	List<List<String>> list = new ArrayList();

	public HSSFWorkbook demoWorkBook = new HSSFWorkbook();

	public HSSFSheet demoSheet = this.demoWorkBook.createSheet("Sheet1");

	public void createTableRow(List<String> cells, short rowIndex) {
		HSSFRow row = this.demoSheet.createRow(rowIndex);
		for (short i = 0; i < cells.size(); i = (short) (i + 1)) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue((String) cells.get(i));
		}
	}

	public void createExcelSheeet() throws SQLException {
		for (int i = 0; i < this.list.size(); i++)
			createTableRow((List) this.list.get(i), (short) i);
	}

	public InputStream exportExcel(HSSFSheet sheet) throws IOException {
		sheet.setGridsPrinted(true);
		HSSFFooter footer = sheet.getFooter();
		footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			this.demoWorkBook.write(baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] ba = baos.toByteArray();
		this.os = new ByteArrayInputStream(ba);
		return this.os;
	}

	public InputStream export(List<List<String>> zlist) {
		InputStream myos = null;
		try {
			this.list = zlist;
			createExcelSheeet();
			myos = exportExcel(this.demoSheet);
			return myos;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "表格导出出错，错误信息 ：" + e + "\n错误原因可能是表格已经打开。");
			e.printStackTrace();
			return null;
		} finally {
			try {
				this.os.close();
				if (myos != null)
					myos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public HSSFWorkbook getHSSFWorkbook(List<List<String>> zlist) {
		try {
			this.list = zlist;
			createExcelSheeet();

			this.demoSheet.setGridsPrinted(true);
			HSSFFooter footer = this.demoSheet.getFooter();
			footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
			return this.demoWorkBook;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "表格导出出错，错误信息 ：" + e + "\n错误原因可能是表格已经打开。");
			e.printStackTrace();
		}
		return null;
	}

	public static void createRar(HttpServletResponse response, String dir, List<File> srcfile, String expName) {
		if (!new File(dir).exists()) {
			new File(dir).mkdirs();
		}
		File zipfile = new File(dir + "/" + expName + ".rar");
		FileUtils.deleteFile(zipfile);
		for (int i = 0; i < srcfile.size(); i++) {
			FileUtils.deleteFile(new File(dir + "/" + expName + i + ".xls"));
		}
		zipFiles(srcfile, zipfile);
		try {
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(zipfile.getName().getBytes("gb2312"), "ISO8859-1"));
			response.addHeader("Content-Length", "" + zipfile.length());

			InputStream fis = new BufferedInputStream(new FileInputStream(zipfile));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createExcel(String[] headName, List<List<String>> list, String expName, String dir)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "sheet1", HSSFWorkbook.ENCODING_UTF_16);// 设置sheet中文编码；
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short) 0);
		for (int y = 0; y < headName.length; y++) {
			cell = row.createCell((short) y);
		    cell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell中文编码；
			cell.setCellValue(headName[y]);
		}
		for (int x = 0; x < list.size(); x++) {
			row = sheet.createRow(x + 1);
			List rowString = (List) list.get(x);
			for (int i = 0; i < rowString.size(); i++) {
				cell = row.createCell((short) i);
			    cell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell中文编码；
				cell.setCellValue((String) rowString.get(i));
			}
		}
		File file = new File(dir + "/" + expName + ".xls");
		if (!new File(dir).exists()) {
			new File(dir).mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(file);
		workbook.write(fos);
		fos.close();
		return file;
	}

	public static void zipFiles(List<File> srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		String ZIP_ENCODEING = "GB2312";
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
			out.setEncoding(ZIP_ENCODEING);
			for (int i = 0; i < srcfile.size(); i++) {
				File file = (File) srcfile.get(i);
				FileInputStream in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}