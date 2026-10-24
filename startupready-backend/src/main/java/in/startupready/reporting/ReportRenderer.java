
package in.startupready.reporting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class ReportRenderer {

    public byte[] markdown(List<ReportBlock> blocks) {
        StringBuilder output = new StringBuilder();

        for (ReportBlock block : blocks) {
            List<String> cells = block.cells();
            String text = cells.isEmpty() ? "" : cells.get(0);

            switch (block.type()) {
                case "h1" -> output.append("# ").append(text).append("\n\n");
                case "h2" -> output.append("## ").append(text).append("\n\n");
                case "p" -> output.append(text).append("\n\n");
                case "li" -> output.append("- ").append(text).append("\n");
                case "th", "tr" -> output.append("| ")
                        .append(String.join(" | ", cells))
                        .append(" |\n");
                default -> output.append(text).append("\n\n");
            }
        }

        return output.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    public byte[] pdf(List<ReportBlock> blocks) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Font headingFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 18);
        Font subheadingFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 13);
        Font normalFont = FontFactory.getFont(
                FontFactory.HELVETICA, 10);

        for (int i = 0; i < blocks.size(); i++) {
            ReportBlock block = blocks.get(i);
            List<String> cells = block.cells();
            String text = cells.isEmpty() ? "" : cells.get(0);

            switch (block.type()) {
                case "h1" -> {
                    Paragraph p = new Paragraph(text, headingFont);
                    p.setSpacingAfter(10);
                    document.add(p);
                }
                case "h2" -> {
                    Paragraph p = new Paragraph(text, subheadingFont);
                    p.setSpacingBefore(8);
                    p.setSpacingAfter(5);
                    document.add(p);
                }
                case "p" -> {
                    Paragraph p = new Paragraph(text, normalFont);
                    p.setSpacingAfter(5);
                    document.add(p);
                }
                case "li" -> {
                    Paragraph p = new Paragraph("\u2022 " + text, normalFont);
                    p.setIndentationLeft(12);
                    document.add(p);
                }
                case "th", "tr" -> {
                    PdfPTable table = new PdfPTable(
                            Math.max(1, cells.size()));
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(4);
                    table.setSpacingAfter(6);

                    for (String cell : cells) {
                        com.lowagie.text.pdf.PdfPCell pdfCell =
                                new com.lowagie.text.pdf.PdfPCell(
                                        new Paragraph(cell, normalFont));
                        pdfCell.setPadding(5);
                        table.addCell(pdfCell);
                    }

                    if (block.type().equals("th")) {
                        table.setHeaderRows(1);
                    }
                    document.add(table);
                }
                default -> document.add(new Paragraph(text, normalFont));
            }
        }

        document.close();
        return output.toByteArray();
    }

    public byte[] docx(List<ReportBlock> blocks) throws IOException {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            for (ReportBlock block : blocks) {
                List<String> cells = block.cells();
                String text = cells.isEmpty() ? "" : cells.get(0);

                switch (block.type()) {
                    case "h1", "h2" -> {
                        XWPFParagraph paragraph = document.createParagraph();
                        paragraph.setStyle(
                                block.type().equals("h1")
                                        ? "Title" : "Heading2");
                        paragraph.createRun().setText(text);
                    }
                    case "li" -> {
                        XWPFParagraph paragraph = document.createParagraph();
                        paragraph.setStyle("ListParagraph");
                        paragraph.createRun().setText("\u2022 " + text);
                    }
                    case "th", "tr" -> {
                        XWPFTable table = document.createTable(1, cells.size());
                        XWPFTableRow row = table.getRow(0);

                        for (int i = 0; i < cells.size(); i++) {
                            row.getCell(i).setText(cells.get(i));

                            if (block.type().equals("th")) {
                                for (XWPFParagraph p :
                                        row.getCell(i).getParagraphs()) {
                                    for (XWPFRun run : p.getRuns()) {
                                        run.setBold(true);
                                    }
                                }
                            }
                        }
                    }
                    default -> {
                        XWPFParagraph paragraph = document.createParagraph();
                        paragraph.createRun().setText(text);
                    }
                }
            }

            document.write(output);
            return output.toByteArray();
        }
    }
}
