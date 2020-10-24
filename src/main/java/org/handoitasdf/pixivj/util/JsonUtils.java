package org.handoitasdf.pixivj.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.handoitasdf.pixivj.adapter.LocalDateAdapter;
import org.handoitasdf.pixivj.adapter.OffsetDateTimeAdapter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class JsonUtils {
  public static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(LocalDate .class, new LocalDateAdapter())
      .registerTypeAdapter(OffsetDateTime .class, new OffsetDateTimeAdapter())
      .create();
}
