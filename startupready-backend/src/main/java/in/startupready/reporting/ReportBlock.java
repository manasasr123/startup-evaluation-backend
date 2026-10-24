
package in.startupready.reporting;

import java.util.List;

public record ReportBlock(
        String type,
        List<String> cells) {

    public ReportBlock {
        cells = cells == null ? List.of() : List.copyOf(cells);
    }

    public static ReportBlock heading(String text) {
        return new ReportBlock("h1", List.of(text));
    }

    public static ReportBlock subheading(String text) {
        return new ReportBlock("h2", List.of(text));
    }

    public static ReportBlock paragraph(String text) {
        return new ReportBlock("p", List.of(text));
    }

    public static ReportBlock bullet(String text) {
        return new ReportBlock("li", List.of(text));
    }

    public static ReportBlock tableHeader(String... cells) {
        return new ReportBlock("th", List.of(cells));
    }

    public static ReportBlock tableRow(String... cells) {
        return new ReportBlock("tr", List.of(cells));
    }
}
