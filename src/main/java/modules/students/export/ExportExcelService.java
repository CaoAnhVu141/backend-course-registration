package modules.students.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class ExportExcelService {

    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] export(String sheetName, String title, List<String> headers, List<? extends List<?>> rows) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            createTitle(sheet, workbook, title, headers.size() - 1);
            createHeader(sheet, workbook, headers);

            int rowIndex = 3;
            for (List<?> values : rows) {
                Row row = sheet.createRow(rowIndex++);
                for (int column = 0; column < headers.size(); column++) {
                    Object value = column < values.size() ? values.get(column) : null;
                    writeCell(row.createCell(column), value);
                }
            }

            sheet.createFreezePane(0, 3);
            for (int column = 0; column < headers.size(); column++) {
                sheet.autoSizeColumn(column);
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể tạo file Excel", exception);
        }
    }

    public static ResponseEntity<byte[]> download(byte[] file, String filePrefix) {
        String fileName = filePrefix + "-" + LocalDate.now() + ".xlsx";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(CONTENT_TYPE))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(fileName, java.nio.charset.StandardCharsets.UTF_8)
                                .build().toString())
                .body(file);
    }

    private void createTitle(Sheet sheet, Workbook workbook, String title, int lastColumn) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, Math.max(lastColumn, 0)));
    }

    private void createHeader(Sheet sheet, Workbook workbook, List<String> headers) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row headerRow = sheet.createRow(2);
        for (int column = 0; column < headers.size(); column++) {
            Cell cell = headerRow.createCell(column);
            cell.setCellValue(headers.get(column));
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeCell(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else if (value instanceof Boolean bool) {
            cell.setCellValue(bool);
        } else if (value instanceof LocalDateTime dateTime) {
            cell.setCellValue(dateTime.format(DATE_TIME_FORMATTER));
        } else if (value instanceof Enum<?> enumValue) {
            cell.setCellValue(enumValue.name());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

}
