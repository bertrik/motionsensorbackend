package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    private static final Logger LOG = LoggerFactory.getLogger(MotionEventWriter.class);

    private final File baseFolder;
    private final CsvMapper csvMapper;

    public MotionEventWriter(File folder) {
        this.baseFolder = folder;

        csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public File write(String deviceId, BaseEvent event) throws IOException {
        // create file per month
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String filename = String.format(Locale.ROOT, "%s_%s_%s.csv", event.getEventType(), deviceId,
                formatter.format(event.getDate()));
        File file = new File(baseFolder, filename);

        // append data
        boolean append = file.exists();
        CsvSchema schema = csvMapper.schemaFor(event.getClass());
        schema = append ? schema.withoutHeader() : schema.withHeader();
        ObjectWriter writer = csvMapper.writer(schema);
        try (FileOutputStream fos = new FileOutputStream(file, append)) {
            String value = writer.writeValueAsString(event).trim();
            LOG.info("Writing to {}: {}", filename, value);
            writer.writeValue(fos, event);
        }
        return file;
    }

}
