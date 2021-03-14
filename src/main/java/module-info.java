module com.github.hanshsieh.pixivj {
  requires org.checkerframework.checker.qual;
  requires org.slf4j;
  requires org.apache.commons.lang3;
  requires kotlin.stdlib;
  requires kotlin.stdlib.common;
  requires okhttp3;
  requires com.google.gson;
  opens com.github.hanshsieh.pixivj.model to com.google.gson;
  exports com.github.hanshsieh.pixivj.token;
  exports com.github.hanshsieh.pixivj.api;
  exports com.github.hanshsieh.pixivj.oauth;
  exports com.github.hanshsieh.pixivj.model;
  exports com.github.hanshsieh.pixivj.exception;
  exports com.github.hanshsieh.pixivj.http;
}
