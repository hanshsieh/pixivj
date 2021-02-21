package com.github.hanshsieh.pixivj.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeAdapter implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
  public JsonElement serialize(OffsetDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
  }

  @Override
  public OffsetDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    return OffsetDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }
}
