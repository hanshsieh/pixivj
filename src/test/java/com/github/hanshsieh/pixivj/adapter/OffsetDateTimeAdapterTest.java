package com.github.hanshsieh.pixivj.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OffsetDateTimeAdapterTest {

  private OffsetDateTimeAdapter offsetDateTimeAdapter;

  @BeforeEach
  public void beforeEach() {
    offsetDateTimeAdapter = new OffsetDateTimeAdapter();
  }

  @Test
  public void testSerializeAndDeserialize() {
    JsonElement element = offsetDateTimeAdapter.serialize(
        OffsetDateTime.of(
            LocalDateTime.of(2021, 1, 20, 23, 1, 59),
            ZoneOffset.UTC),
        null,
        null);
    assertTrue(element.isJsonPrimitive());
    assertTrue(((JsonPrimitive) element).isString());
    assertEquals("2021-01-20T23:01:59Z", element.getAsString());
    OffsetDateTime offsetDateTime = offsetDateTimeAdapter.deserialize(element, null, null);
    assertEquals(2021, offsetDateTime.getYear());
    assertEquals(Month.JANUARY, offsetDateTime.getMonth());
    assertEquals(20, offsetDateTime.getDayOfMonth());
    assertEquals(23, offsetDateTime.getHour());
    assertEquals(1, offsetDateTime.getMinute());
    assertEquals(59, offsetDateTime.getSecond());
  }
}
