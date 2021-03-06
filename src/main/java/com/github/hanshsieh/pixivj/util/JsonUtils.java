package com.github.hanshsieh.pixivj.util;

import com.github.hanshsieh.pixivj.adapter.LocalDateAdapter;
import com.github.hanshsieh.pixivj.adapter.OffsetDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class JsonUtils {

  public static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
      .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
      .create();
}
