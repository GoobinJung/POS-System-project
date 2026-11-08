package posDm;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class CsvExport {
    private CsvExport() {}
    public static void write(Path path, String[] headers, List<Object[]> rows) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writeRow(writer, headers);
            for (Object[] row : rows) writeRow(writer, row);
        }
    }
    private static void writeRow(BufferedWriter writer, Object[] row) throws IOException {
        for (int i = 0; i < row.length; i++) {
            if (i > 0) writer.write(',');
            String text = row[i] == null ? "" : row[i].toString();
            if (row[i] instanceof String && text.matches("^[=+@-].*")) text = "'" + text;
            writer.write("\"" + text.replace("\"", "\"\"") + "\"");
        }
        writer.newLine();
    }
}
