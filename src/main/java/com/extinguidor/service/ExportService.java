package com.extinguidor.service;

import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.Route;
import com.extinguidor.repository.ParteRepository;
import com.extinguidor.repository.CustomerRepository;
import com.extinguidor.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExportService {
    
    private final ParteRepository parteRepository;
    private final CustomerRepository customerRepository;
    private final RouteRepository routeRepository;
    
    @Transactional(readOnly = true)
    public byte[] exportPartesToExcel() throws IOException {
        List<Parte> partes = parteRepository.findByEliminadoFalse();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Partes");
        
        // Estilos
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        // Encabezados
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Título", "Cliente", "Fecha", "Estado", "Tipo", "Categoría", "Dirección", "Facturación"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Datos
        int rowNum = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Parte parte : partes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(parte.getId() != null ? parte.getId() : 0);
            row.createCell(1).setCellValue(parte.getTitle() != null ? parte.getTitle() : "");
            row.createCell(2).setCellValue(parte.getCustomer() != null && parte.getCustomer().getName() != null 
                ? parte.getCustomer().getName() : "");
            row.createCell(3).setCellValue(parte.getDate() != null ? parte.getDate().format(formatter) : "");
            row.createCell(4).setCellValue(parte.getState() != null ? parte.getState().name() : "");
            row.createCell(5).setCellValue(parte.getType() != null ? parte.getType().name() : "");
            row.createCell(6).setCellValue(parte.getCategoria() != null ? parte.getCategoria().name() : "");
            row.createCell(7).setCellValue(parte.getAddress() != null ? parte.getAddress() : "");
            row.createCell(8).setCellValue(parte.getFacturacion() != null ? parte.getFacturacion() : 0.0);
        }
        
        // Ajustar ancho de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
    
    @Transactional(readOnly = true)
    public byte[] exportCustomersToExcel() throws IOException {
        List<Customer> customers = customerRepository.findAll();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes");
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nombre", "Código", "Email", "Teléfono", "NIF/CIF", "Dirección", "Estado", "Zona"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        int rowNum = 1;
        for (Customer customer : customers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(customer.getId() != null ? customer.getId() : 0);
            row.createCell(1).setCellValue(customer.getName() != null ? customer.getName() : "");
            row.createCell(2).setCellValue(customer.getCode() != null ? customer.getCode() : "");
            row.createCell(3).setCellValue(customer.getEmail() != null ? customer.getEmail() : "");
            row.createCell(4).setCellValue(customer.getPhone() != null ? customer.getPhone() : "");
            row.createCell(5).setCellValue(customer.getNifCif() != null ? customer.getNifCif() : "");
            row.createCell(6).setCellValue(customer.getAddress() != null ? customer.getAddress() : "");
            row.createCell(7).setCellValue(customer.getActive() != null && customer.getActive() ? "Activo" : "Inactivo");
            row.createCell(8).setCellValue(customer.getZone() != null && customer.getZone().getName() != null 
                ? customer.getZone().getName() : "");
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
    
    @Transactional(readOnly = true)
    public byte[] exportPartesToPDF() throws IOException {
        List<Parte> partes = parteRepository.findByEliminadoFalse();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(baos);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);
        
        // Título
        document.add(new com.itextpdf.layout.element.Paragraph("LISTADO DE PARTES")
            .setFontSize(18)
            .setBold()
            .setMarginBottom(20));
        
        // Tabla
        float[] columnWidths = {1, 3, 3, 2, 2};
        com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(columnWidths);
        table.addHeaderCell("ID");
        table.addHeaderCell("Título");
        table.addHeaderCell("Cliente");
        table.addHeaderCell("Fecha");
        table.addHeaderCell("Estado");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Parte parte : partes) {
            table.addCell(parte.getId() != null ? parte.getId().toString() : "");
            table.addCell(parte.getTitle() != null ? parte.getTitle() : "");
            table.addCell(parte.getCustomer() != null && parte.getCustomer().getName() != null 
                ? parte.getCustomer().getName() : "");
            table.addCell(parte.getDate() != null ? parte.getDate().format(formatter) : "");
            table.addCell(parte.getState() != null ? parte.getState().name() : "");
        }
        
        document.add(table);
        document.close();
        
        return baos.toByteArray();
    }
}

