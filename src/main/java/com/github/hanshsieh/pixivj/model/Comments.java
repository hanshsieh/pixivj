package com.github.hanshsieh.pixivj.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Comments {
  @SerializedName("comments")
  private List<Comment> comments = new ArrayList<>();
  @SerializedName("next_url")
  private String nextUrl;

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public String getNextUrl() {
    return nextUrl;
  }

  public void setNextUrl(String nextUrl) {
    this.nextUrl = nextUrl;
  }
}
