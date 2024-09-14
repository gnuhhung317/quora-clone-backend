package net.duchung.quora.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException {
        String str = p.getText();
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSXXX"));
        } catch (DateTimeParseException e) {
            System.err.println(e);
            return null;
        }
    }
}
