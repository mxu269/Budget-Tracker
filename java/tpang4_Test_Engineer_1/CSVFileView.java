// --== CS400 File Header Information ==--
// Name: Tao Pang
// Email: tpang4@wisc.edu
// Team: IA
// Role: Test Engineer 1
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader:

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This is a basic utility class for reading/writing csv file.
 */
public class CSVFileView implements Iterable<CSVFileView.RowEntry> {

    /**
     * Model class for csv data row.
     */
    public class RowEntry {
        // All data of columns within the row.
        private final String[] columnData;

        /**
         * Constructor of RowEntry which parses a raw data string.
         *
         * @param rowString The raw data string from .csv file.
         */
        public RowEntry(String rowString) {
            // Split the raw data string into columns.
            this(rowString.split("\\s*,\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
        }

        /**
         * Constructor of RowEntry from parsed column values.
         *
         * @param columnValues The values of columns of the row.
         */
        private RowEntry(String... columnValues) {
            columnData = columnValues;
            // If the size doesn't match.
            if (columnData.length != columnNames.size())
                throw new IllegalArgumentException("Invalid data for initializing RowEntry.");
            // Clean out surrounding quotes.
            for (int i = 0; i < columnData.length; i++)
                if (columnData[i].startsWith("\"") && columnData[i].endsWith("\""))
                    columnData[i] = columnData[i].substring(1, columnData[i].length() - 1);
        }

        /**
         * Get the string of certain column.
         *
         * @param columnIndex The index of the column.
         * @return The data of the given column.
         */
        public String getColumn(int columnIndex) {
            return columnData[columnIndex];
        }

        /**
         * Parse string into Date object.
         *
         * @param dateString Date string in standard US format "MM/dd/yyyy".
         * @return The parsed Date object.
         */
        @SuppressWarnings("MagicConstant")
        private Date parseDate(String dateString) {
            Matcher matcher = Pattern.compile("(\\d+)[/-](\\d+)[/-](\\d+)").matcher(dateString);
            try {
                boolean found = matcher.find();
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2)) - 1;
                int day = Integer.parseInt(matcher.group(3));
                return new GregorianCalendar(year, month, day).getTime();
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format("Invalid date string: \"%s\"", dateString));
            }
        }

        /**
         * Parse string into LocalDateTime object.
         *
         * @param dateString Date string in standard US format "MM/dd/yyyyThh:mm".
         * @return The parsed Date object.
         */
        private LocalDateTime parseDateTime(String dateString) {
            Matcher matcher = Pattern.compile("(\\d+)[/-](\\d+)[/-](\\d+)T(\\d+):(\\d+)(:.+)?").matcher(dateString);
            try {
                boolean found = matcher.find();
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                int day = Integer.parseInt(matcher.group(3));
                int hour = Integer.parseInt(matcher.group(4));
                int minute = Integer.parseInt(matcher.group(5));
                if (matcher.group(6) != null) {
                    String[] terms = matcher.group(6).substring(1).split("\\.");
                    int seconds = Integer.parseInt(terms[0]);
                    int nanoSeconds = Integer.parseInt(terms[1]) * 1000000;
                    return LocalDateTime.of(year, month, day, hour, minute, seconds, nanoSeconds);
                }
                return LocalDateTime.of(year, month, day, hour, minute);
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format("Invalid date-time string: \"%s\"", dateString));
            }
        }

        /**
         * Get the data of a specific column as the given type.
         *
         * @param columnIndex The index of the column.
         * @param type        The desired data type.
         * @return The data in desired type.
         */
        public <D> D getColumn(int columnIndex, Class<D> type) {
            if (type.equals(Integer.class))
                return type.cast(Integer.valueOf(getColumn(columnIndex)));
            else if (type.equals(Float.class))
                return type.cast(Float.valueOf(getColumn(columnIndex)));
            else if (type.equals(Double.class))
                return type.cast(Double.valueOf(getColumn(columnIndex)));
            else if (type.equals(String.class))
                return type.cast(getColumn(columnIndex));
            else if (type.equals(Date.class))
                return type.cast(parseDate(getColumn(columnIndex)));
            else if (type.equals(LocalDate.class))
                return type.cast
                        (LocalDate.ofInstant(getColumn(columnIndex, Date.class).toInstant(), ZoneId.systemDefault()));
            else if (type.equals(LocalDateTime.class))
                return type.cast(parseDateTime(getColumn(columnIndex)));
            else
                throw new IllegalArgumentException("Given data type is unsupported.");
        }

        /**
         * Get the string of certain column.
         *
         * @param columnName The name of the column.
         * @return The data of the given column.
         */
        public String getColumn(String columnName) {
            return getColumn(columnNames.indexOf(columnName));
        }

        /**
         * Get the data of a specific column as the given type.
         *
         * @param columnName The name of the column.
         * @param type       The desired data type.
         * @return The data in desired type.
         */
        public <D> D getColumn(String columnName, Class<D> type) {
            return getColumn(columnNames.indexOf(columnName), type);
        }

        /**
         * Get the string representation for display.
         *
         * @return The string representation of the row.
         */
        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(", ", "RowEntry{", "}");
            for (int i = 0; i < columnData.length; i++)
                joiner.add(String.format("%s=\"%s\"", columnNames.get(i), columnData[i]));
            return joiner.toString();
        }

        /**
         * Get the string representation for storing back to csv file.
         *
         * @return The string representation of the row for storing.
         */
        private String serialize() {
            StringJoiner joiner = new StringJoiner(",");
            for (String col : columnData)
                joiner.add(String.format(col.contains(",") ? "\"%s\"" : "%s", col));
            return joiner.toString();
        }
    }

    // The name of columns with the csv table.
    private final ArrayList<String> columnNames;
    // The data row entries.
    private final ArrayList<RowEntry> rowEntries = new ArrayList<>();

    /**
     * Initializing by reading in a .csv data file.
     *
     * @param path The path of the data file.
     * @throws IOException If error occurs during file reading.
     */
    public CSVFileView(Path path) throws IOException {
        this(Files.readString(path));
    }

    /**
     * Initializing by parsing the .csv data file content.
     *
     * @param content The content of th data file.
     */
    public CSVFileView(String content) {
        // Clean out duplicate newline symbols.
        String rawData = content.replaceAll("[\\r\\n]+", "\n");
        // Get the header line and body lines.
        String header = rawData.substring(0, rawData.indexOf("\n"));
        String body = rawData.substring(rawData.indexOf("\n") + 1);
        // Parse the column names from the header line.
        columnNames = new ArrayList<>(Arrays.asList(header.split("\\s*,\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")));
        for (int i = 0; i < columnNames.size(); i++)
            if (columnNames.get(i).startsWith("\"") && columnNames.get(i).endsWith("\""))
                columnNames.set(i, columnNames.get(i).substring(1, columnNames.get(i).length() - 1));
        // Parse all row lines of the body part.
        body.lines().forEachOrdered(line -> rowEntries.add(new RowEntry(line)));
    }

    /**
     * Constructor for creating an non-initialized view.
     */
    private CSVFileView() {
        columnNames = new ArrayList<>();
    }

    /**
     * Create an empty view with given columns.
     *
     * @param columnNames The name of columns.
     * @return The empty view with given column names.
     */
    public static CSVFileView create(String... columnNames) {
        CSVFileView view = new CSVFileView();
        view.columnNames.addAll(Arrays.asList(columnNames));
        return view;
    }

    /**
     * Create a view from parse collection of elements.
     *
     * @param columns The name of columns.
     * @param rows    The parsed row entries.
     */
    private CSVFileView(Collection<String> columns, Collection<RowEntry> rows) {
        this.columnNames = new ArrayList<>();
        this.columnNames.addAll(columns);
        this.rowEntries.addAll(rows);
    }

    /**
     * Get the number of rows in table.
     *
     * @return The number of rows.
     */
    public int getRowCount() {
        return rowEntries.size();
    }

    /**
     * Get a specific row of the csv table.
     *
     * @param rowIndex The index of the row.
     * @return The RowEntry object corresponding to the given row index.
     */
    public RowEntry getRow(int rowIndex) {
        return rowEntries.get(rowIndex);
    }

    /**
     * Get a range of rows of the csv table.
     *
     * @param startIndex The start index of the range.
     * @param endIndex   The end index of th range.
     * @return The array of RowEntry objects within the range.
     */
    public RowEntry[] getRows(int startIndex, int endIndex) {
        return rowEntries.subList(startIndex, endIndex).toArray(RowEntry[]::new);
    }

    /**
     * Get the view of certain range of rows of the csv table.
     *
     * @param startIndex The start index of the range.
     * @param endIndex   The end index of th range.
     * @return The view of the rows in range.
     */
    public CSVFileView getRowsView(int startIndex, int endIndex) {
        return new CSVFileView(columnNames, rowEntries.subList(startIndex, endIndex));
    }

    /**
     * Get filtered rows of the csv table.
     *
     * @param filter The predicate for filtering the desired row.
     * @return The array of filtered RowEntry objects.
     */
    public RowEntry[] getFilteredRows(Predicate<RowEntry> filter) {
        return rowEntries.stream().filter(filter).toArray(RowEntry[]::new);
    }

    /**
     * Get the view of filtered csv table.
     *
     * @param filter The predicate for filtering the desired row.
     * @return The view of table after filtering the rows.
     */
    public CSVFileView getFilteredRowsView(Predicate<RowEntry> filter) {
        return new CSVFileView(columnNames, rowEntries.stream().filter(filter).collect(Collectors.toList()));
    }

    /**
     * Get a specific column of the csv table.
     *
     * @param columnIndex The index of the column.
     * @return The array of strings with the column.
     */
    public String[] getColumn(int columnIndex) {
        return rowEntries.stream().map(row -> row.getColumn(columnIndex)).toArray(String[]::new);
    }

    /**
     * Get a specific column of the csv table in given type.
     *
     * @param columnIndex The index of the column.
     * @param type        The desired data type.
     * @return The array of data within the column in desired type.
     */
    @SuppressWarnings("unchecked")
    public <D> D[] getColumn(int columnIndex, Class<D> type) {
        return rowEntries.stream().map(row -> row.getColumn(columnIndex, type)).toArray(
                size -> (D[]) Array.newInstance(type, size));
    }

    /**
     * Get a specific column of the csv table.
     *
     * @param columnName The name of the column.
     * @return The array of strings with the column.
     */
    public String[] getColumn(String columnName) {
        return getColumn(columnNames.indexOf(columnName));
    }

    /**
     * Get a specific column of the csv table in given type.
     *
     * @param columnName The name of the column.
     * @param type       The desired data type.
     * @return The array of data within the column in desired type.
     */
    @SuppressWarnings("unchecked")
    public <D> D[] getColumn(String columnName, Class<D> type) {
        return rowEntries.stream().map(row -> row.getColumn(columnName, type)).toArray(
                size -> (D[]) Array.newInstance(type, size));
    }

    /**
     * Get the content of a specific location of the csv table.
     *
     * @param rowIndex    The index of th row.
     * @param columnIndex The column of the row.
     * @return The string content.
     */
    public String getCell(int rowIndex, int columnIndex) {
        return rowEntries.get(rowIndex).getColumn(columnIndex);
    }

    /**
     * Get the content of a specific location of the csv table.
     *
     * @param rowIndex   The index of th row.
     * @param columnName The name of the row.
     * @return The string content.
     */
    public String getCell(int rowIndex, String columnName) {
        return rowEntries.get(rowIndex).getColumn(columnName);
    }

    /**
     * Add a new row to the csv table.
     *
     * @param rowContent The raw content of the row.
     */
    public void appendRow(String rowContent) {
        rowEntries.add(new RowEntry(rowContent));
    }

    /**
     * Add a new row to the csv table.
     *
     * @param columnValues The values of columns in the row.
     */
    public void appendRow(String... columnValues) {
        rowEntries.add(new RowEntry(columnValues));
    }

    /**
     * Insert a new row to the csv table.
     *
     * @param rowIndex   The desired line number of the new row.
     * @param rowContent The raw content of the row.
     */
    public void insertRow(int rowIndex, String rowContent) {
        rowEntries.add(rowIndex, new RowEntry(rowContent));
    }

    /**
     * Insert a new row to the csv table.
     *
     * @param rowIndex     The desired line number of the new row.
     * @param columnValues The values of columns in the row.
     */
    public void insertRow(int rowIndex, String... columnValues) {
        rowEntries.add(rowIndex, new RowEntry(columnValues));
    }

    /**
     * Remove a row from the csv table.
     *
     * @param rowIndex The index of the removed row.
     */
    public void removeRow(int rowIndex) {
        rowEntries.remove(rowIndex);
    }

    /**
     * Save the csv table into a file.
     *
     * @param path The path of desired file.
     * @throws IOException If error occurs during writing the file.
     */
    public void saveAs(Path path) throws IOException {
        StringJoiner joiner = new StringJoiner("\n");
        StringJoiner header = new StringJoiner(",");
        // Generate header line.
        for (String entry : columnNames)
            header.add(String.format(entry.contains(",") ? "\"%s\"" : "%s", entry));
        joiner.add(header.toString());
        // Add column line contents.
        for (RowEntry row : rowEntries)
            joiner.add(row.serialize());
        Files.writeString(path, joiner.toString());
    }

    /**
     * Get an iterator for iterating through the rows.
     *
     * @return The iterator object.
     */
    @Override
    public Iterator<RowEntry> iterator() {
        return new Iterator<>() {
            int rowIndex = 0;

            @Override
            public boolean hasNext() {
                return rowIndex < rowEntries.size();
            }

            @Override
            public RowEntry next() {
                return rowEntries.get(rowIndex++);
            }
        };
    }
}
