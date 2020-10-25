package com.github.pixivj.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
  public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
  }

  @Override
  public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
  }
}
