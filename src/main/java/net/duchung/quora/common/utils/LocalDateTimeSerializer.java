package net.duchung.quora.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        try {
            String s = value.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSXXX"));
            generator.writeString(s);
        } catch (DateTimeParseException e) {
            System.err.println(e);
            generator.writeString(value.toString());
        }
    }


}
