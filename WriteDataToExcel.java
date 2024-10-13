// Java program to write data in excel sheet using java code
  
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import java.io.FileOutputStream;
/*
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;*/
import javax.swing.JFileChooser;
  
public class WriteDataToExcel {
    
    public void createTableHorizontal(double[][] data, String sheetName, String fileName) throws Exception {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
        // spreadsheet object
        XSSFSheet spreadsheet
            = workbook.createSheet(sheetName);
  
        // creating a row object
        //XSSFRow row;

  
        //int rowid = 0;
        
        XSSFRow row0 = spreadsheet.createRow(0);
        XSSFRow row1 = spreadsheet.createRow(1);
        
        //int cellid = 0;
        for (int i = 0; i < data[0].length; i++) {
            Cell cell0 = row0.createCell(i);
            cell0.setCellValue(data[0][i]);
            
            Cell cell1 = row1.createCell(i);
            cell1.setCellValue(data[1][i]);
        }
  
        // writing the data into the sheets...
  /*
        for (String key : keyid) {
  
            row = spreadsheet.createRow(rowid++);
            int cellid = 0;
  
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }*/
  
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            FileOutputStream out = new FileOutputStream(new File(selectedFile.getAbsolutePath()+".xlsx"));
            workbook.write(out);
            out.close();
        }
        workbook.close();
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        //FileOutputStream out = new FileOutputStream(new File(fileName+".xlsx"));
    }
    
    public void createAllTablesHorizontal(double[][][] data, String[] sheetName, String fileName) throws Exception {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
        // spreadsheet object
        
        System.out.println("First:" + data.length + ",Second:" + data[0].length + ",Third:" + data[0][0].length);
        for (int l = 0; l < sheetName.length; l++) {
            XSSFSheet spreadsheet
                = workbook.createSheet(sheetName[l]);

            for (int p = 0; p < data[l].length; p++) {
            XSSFRow row0 = spreadsheet.createRow(p);
            //XSSFRow row1 = spreadsheet.createRow(1);

                //int cellid = 0;
                for (int i = 0; i < data[0][p].length; i++) {
                    Cell cell0 = row0.createCell(i);
                    cell0.setCellValue(data[l][p][i]);

                    //Cell cell1 = row1.createCell(i);
                    //cell1.setCellValue(data[l][1][i]);
                }
            }
        }
  
        // writing the data into the sheets...
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(new File(fileName+".xlsx"));
        workbook.write(out);
        out.close();
        workbook.close();
  
    }
    
    
    public void createTable(double[][] data, String sheetName, String fileName) throws Exception {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
        // spreadsheet object
        XSSFSheet spreadsheet
            = workbook.createSheet(sheetName);
  
        for (int i = 0; i < data[0].length; i++) {
	        XSSFRow row = spreadsheet.createRow(i);
        
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(Math.round(data[0][i] * 1000.0) / 1000.0);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(Math.round(data[1][i] * 1000.0) / 1000.0);
        }
  
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            FileOutputStream out = new FileOutputStream(new File(selectedFile.getAbsolutePath()+".xlsx"));
            workbook.write(out);
            out.close();
        }
        workbook.close();
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        //FileOutputStream out = new FileOutputStream(new File(fileName+".xlsx"));
    }
    
    public void createAllTables(double[][][] data, String[] sheetName, String fileName) throws Exception {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
        // spreadsheet object
        System.out.println("First:" + data.length + ",Second:" + data[0].length + ",Third:" + data[0][0].length);
        
        for (int l = 0; l < sheetName.length; l++) {
            XSSFSheet spreadsheet
                = workbook.createSheet(sheetName[l]);

            for (int p = 0; p < data[l][0].length; p++) {
            XSSFRow row0 = spreadsheet.createRow(p);

                for (int i = 0; i < data[l].length; i++) {
                    Cell cell0 = row0.createCell(i);
                    cell0.setCellValue(Math.round(data[l][i][p] * 1000.0) / 1000.0);

                }
            }
        }
  
        // writing the data into the sheets...
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            FileOutputStream out = new FileOutputStream(new File(selectedFile.getAbsolutePath()+".xlsx"));
            workbook.write(out);
            out.close();
        }
        // FileOutputStream out = new FileOutputStream(new File(fileName+".xlsx"));
        //workbook.write(out);
        //out.close();
        workbook.close();
  
    }
    
    
    
    //Creates at location of files
    public void createTableAtHome(double[][] data, String sheetName, String fileName) throws Exception {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
        // spreadsheet object
        XSSFSheet spreadsheet
            = workbook.createSheet(sheetName);
  
        // creating a row object
        //XSSFRow row;

  
        //int rowid = 0;
        
        XSSFRow row0 = spreadsheet.createRow(0);
        XSSFRow row1 = spreadsheet.createRow(1);
        
        //int cellid = 0;
        for (int i = 0; i < data[0].length; i++) {
            Cell cell0 = row0.createCell(i);
            cell0.setCellValue(data[0][i]);
            
            Cell cell1 = row1.createCell(i);
            cell1.setCellValue(data[1][i]);
        }
  
        // writing the data into the sheets...
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(new File(fileName+".xlsx"));
        workbook.write(out);
        out.close();
        workbook.close();
  
    }
  
    
    
}