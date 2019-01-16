package ru.sportequipment.util;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Equipment;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelUtil {
    private static final Logger logger = LogManager.getLogger(ExcelUtil.class);
    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd";
    private static String[] columns = {"Contact First Name", "Contact LastName", "Contact Email",
            "Equipment Cost Per Hour", "Booked From", "Booked To"};


    public static void generateExcelReport(List<Contact> contacts, String sheetTitle, String path) throws ApplicationException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        int rowIndex = 0;
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(sheetTitle);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.DARK_RED.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // header
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            for (Contact contact : contacts) {
                for (Equipment equipment : contact.getBookedEquipment()) {
                    int cellIndex = 0;
                    Row row = sheet.createRow(rowIndex++);

                    row.createCell(cellIndex++).setCellValue(contact.getFirstName());
                    row.createCell(cellIndex++).setCellValue(contact.getLastName());
                    row.createCell(cellIndex++).setCellValue(contact.getEmail());

                    row.createCell(cellIndex++).setCellValue(equipment.getCostPerHour().toString());
                    row.createCell(cellIndex++).setCellValue(dateFormat.format(equipment.getBookedFrom() != null ? equipment.getBookedFrom() : new Date()));
                    row.createCell(cellIndex++).setCellValue(dateFormat.format(equipment.getBookedTo() != null ? equipment.getBookedFrom() : new Date()));
                }
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // Write the output to a file
            if (!path.isEmpty()) {
                path = path + "/";
            }
            logger.debug("path " + path);

            FileOutputStream fileOut = new FileOutputStream(path + "equipment_report.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();
        } catch (IOException e) {
            throw new ApplicationException("Cannot generate excel report!", ResponseStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
