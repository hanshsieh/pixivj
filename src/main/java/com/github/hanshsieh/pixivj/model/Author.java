package com.github.hanshsieh.pixivj.model;

import com.github.hanshsieh.pixivj.util.JsonUtils;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Author {

  @SerializedName("id")
  private Long id = null;

  @SerializedName("profile_image_urls")
  private AuthorProfileImageUrls profileImageUrls = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("account")
  private String account = null;

  @SerializedName("is_followed")
  private Boolean followed = null;

  /**
   * Get id
   *
   * @return id
   **/
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Get profileImageUrls
   *
   * @return profileImageUrls
   **/
  public AuthorProfileImageUrls getProfileImageUrls() {
    return profileImageUrls;
  }

  public void setProfileImageUrls(AuthorProfileImageUrls profileImageUrls) {
    this.profileImageUrls = profileImageUrls;
  }

  /**
   * Get name
   *
   * @return name
   **/
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Account name.
   *
   * @return account
   **/
  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  /**
   * Get isFollowed
   *
   * @return isFollowed
   **/
  public Boolean isFollowed() {
    return followed;
  }

  public void setFollowed(Boolean followed) {
    this.followed = followed;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Author author = (Author) o;
    return Objects.equals(this.id, author.id) &&
        Objects.equals(this.profileImageUrls, author.profileImageUrls) &&
        Objects.equals(this.name, author.name) &&
        Objects.equals(this.account, author.account) &&
        Objects.equals(this.followed, author.followed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, profileImageUrls, name, account, followed);
  }


  @Override
  public String toString() {
    return JsonUtils.GSON.toJson(this);
  }

}
