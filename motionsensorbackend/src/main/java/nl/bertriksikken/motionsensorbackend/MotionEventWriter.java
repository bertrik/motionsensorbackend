package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Writes motion events as CSV. <br>
 * Events are written into a sub-directory for each device and grouped by month.
 */
public final class MotionEventWriter {

    private final File baseFolder;
    private final CsvSchema baseSchema;
    private final CsvMapper mapper;

    public MotionEventWriter(File folder) {
        this.baseFolder = folder;

        mapper = new CsvMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        baseSchema = mapper.schemaFor(MotionEvent.class);
    }

    public File write(String deviceId, MotionEvent event) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

        // create folder
        File folder = new File(baseFolder, deviceId);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IOException("Failed to create " + folder.getAbsolutePath());
            }
        }
        
        // create file
        String filename = String.format(Locale.ROOT, "%s.csv", formatter.format(event.getTime()));
        File file = new File(folder, filename);

        // append data
        boolean append = file.exists();
        CsvSchema schema = append ? baseSchema.withoutHeader() : baseSchema.withHeader();
        ObjectWriter writer = mapper.writer(schema);
        try (FileOutputStream fos = new FileOutputStream(file, append)) {
            writer.writeValue(fos, event);
        }
        return file;
    }

}
