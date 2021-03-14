package com.github.hanshsieh.pixivj.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocalDateAdapterTest {

  private LocalDateAdapter localDateAdapter;

  @BeforeEach
  public void beforeEach() {
    localDateAdapter = new LocalDateAdapter();
  }

  @Test
  public void testSerializeAndDeserialize() {
    JsonElement element = localDateAdapter.serialize(
        LocalDate.of(2021, 1, 20),
        null, null);
    assertTrue(element.isJsonPrimitive());
    assertTrue(((JsonPrimitive) element).isString());
    assertEquals("2021-01-20", element.getAsString());
    LocalDate localDate = localDateAdapter.deserialize(element, null, null);
    assertEquals(2021, localDate.getYear());
    assertEquals(Month.JANUARY, localDate.getMonth());
    assertEquals(20, localDate.getDayOfMonth());
  }
}
