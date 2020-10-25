package org.handoitasdf.pixivj;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mockit.Expectations;
import mockit.Injectable;
import okhttp3.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RequestSenderTest {
  @Injectable
  private OkHttpClient httpClient;
  @Injectable
  private Request request;
  @Injectable
  private Response response;
  @Injectable
  private ResponseBody responseBody;
  @Injectable
  private Call call;
  private RequestSender<RuntimeException> requestSender;

  @BeforeEach
  public void beforeEach() throws Exception {
    new Expectations() {{
      request.method();
      result = "TEST_METHOD";
      minTimes = 0;

      request.url();
      result = HttpUrl.parse("http://www.example.com/testapi");
      minTimes = 0;

      httpClient.newCall(request);
      result = call;
      minTimes = 0;

      response.body();
      result = responseBody;
      minTimes = 0;
    }};
    requestSender = new RequestSender<RuntimeException>(httpClient) {
      @Override
      protected RuntimeException createExceptionFromRespBody(@NonNull String respStr) {
        return new RuntimeException(respStr);
      }
    };
  }

  @Test
  @DisplayName("Request is sent successfully with expected response body")
  void testSendSuccessRequest() throws Exception {
    new Expectations() {{
      call.execute();
      result = response;
      times = 1;

      response.code();
      result = 200;

      response.isSuccessful();
      result = true;

      response.close();
      times = 1;

      responseBody.string();
      result = "{\"hello\": \"world\"}";
    }};
    Map<?, ?> result = requestSender.send(request, Map.class);
    Map<String, String> wantResult = new HashMap<>();
    wantResult.put("hello", "world");
    assertEquals(result, wantResult);
  }

  @Test
  @DisplayName("Request is sent successfully with expected empty response body")
  void testSendSuccessRequestWithoutBody() throws Exception {
    new Expectations() {{
      call.execute();
      result = response;
      times = 1;

      response.code();
      result = 200;

      response.isSuccessful();
      result = true;

      response.close();
      times = 1;

      responseBody.string();
      result = "{\"hello\": \"world\"}";
    }};
    Object result = requestSender.send(request, Void.class);
    assertNull(result);
  }

  @Test
  @DisplayName("Server return 500")
  void testSendFailedRequest() throws Exception {
    new Expectations() {{
      call.execute();
      result = response;
      times = 1;

      response.code();
      result = 500;

      response.isSuccessful();
      result = false;

      response.close();
      times = 1;

      responseBody.string();
      result = "{\"hello\": \"world\"}";
    }};
    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
      requestSender.send(request, Map.class);
    });
    assertEquals(ex.getMessage(), "{\"hello\": \"world\"}");
  }

  @Test
  @DisplayName("Got IO error")
  void testSendRequestWithIOError() throws Exception {
    new Expectations() {{
      call.execute();
      result = new IOException("IO error");
      times = 1;
    }};
    IOException ex = assertThrows(IOException.class, () -> {
      requestSender.send(request, Map.class);
    });
    assertEquals(ex.getMessage(), "IO error");
  }
}