package com.github.hanshsieh.pixivj.model;

public class AddBookmark {
  private Long illustId;
  private Restrict restrict;

  public Long getIllustId() {
    return illustId;
  }

  public void setIllustId(Long illustId) {
    this.illustId = illustId;
  }

  public Restrict getRestrict() {
    return restrict;
  }

  /**
   * Sets who can see the bookmark.
   * @param restrict Restrict string.
   */
  public void setRestrict(Restrict restrict) {
    this.restrict = restrict;
  }
}
