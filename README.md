# PixivJ
A java client for Pixiv.

![Java CI](https://github.com/hanshsieh/pixivj/workflows/Java%20CI/badge.svg)  
[![Maven Central](https://img.shields.io/maven-central/v/com.github.hanshsieh/pixivj.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.hanshsieh%22%20AND%20a:%22pixivj%22)  
[Pixiv API doc](https://hanshsieh.github.io/pixiv-api-doc)

Currently, only a small subset of the APIs are implemented.  
> Notice: Because of the change of Pixiv authentication API, this library currently doesn't provide
> a way to obtain the access token programically. The implementation is in progress. Please see the 
> *Issues*.  
> For now, you can manually go to the [login URL](https://app-api.pixiv.net/web/v1/login?code_challenge=F_reT3JvK8doGUKdrVR1rG8DV2iVpFTxQ-vEeZH8TVA&code_challenge_method=S256&client=pixiv-android),
> open your browser's debug console, login your account. You should be able to find the access token
> and refresh token from the responses. Then, you can manually feed the tokens into the library.

# Usage

```java
package com.github.hanshsieh.pixivj;
import com.github.hanshsieh.pixivj.api.PixivApiClient;
import com.github.hanshsieh.pixivj.model.FilterMode;
import com.github.hanshsieh.pixivj.model.FilterType;
import com.github.hanshsieh.pixivj.model.IllustDetail;
import com.github.hanshsieh.pixivj.model.RankedIllusts;
import com.github.hanshsieh.pixivj.model.RankedIllustsFilter;
import com.github.hanshsieh.pixivj.model.RecommendIllusts;
import com.github.hanshsieh.pixivj.model.RecommendedIllustsFilter;
import com.github.hanshsieh.pixivj.model.SearchIllusts;
import com.github.hanshsieh.pixivj.oauth.PixivOAuthClient;
import com.github.hanshsieh.pixivj.token.ThreadedTokenRefresher;
import java.time.Instant;

public class Main {
  public static void main(String[] args) {
    PixivOAuthClient authClient = new PixivOAuthClient.Builder().build();
    ThreadedTokenRefresher tokenProvider = new ThreadedTokenRefresher.Builder()
        .setAuthClient(authClient)
        .build();
    tokenProvider.updateTokens(
        "your_access_token",
        "your_refresh_token",
        Instant.now().plusSeconds(86400));
    PixivApiClient client = new PixivApiClient.Builder()
        .setTokenProvider(tokenProvider)
        .build();
    try {
      // Searches for the illustration.
      SearchIllusts illusts = client.searchIllusts("yuri", 0);

      // Print out all the illustration's large image urls.
      illusts.getIllusts().forEach(illustration -> System.out.println(illustration.getImageUrls().getLarge()));
      RankedIllustsFilter filter = new RankedIllustsFilter();
      filter.setMode(FilterMode.DAY_MALE);
      filter.setFilter(FilterType.FOR_ANDROID);
      filter.setOffset(0);
      RankedIllusts rankedIllusts = client.getRankedIllusts(filter);
      System.out.println(rankedIllusts);

      // Recommended Illustrations
      RecommendedIllustsFilter recommendedIllustsFilter = new RecommendedIllustsFilter();
      recommendedIllustsFilter.setFilter(FilterType.FOR_IOS);
      recommendedIllustsFilter.setIncludePrivacyPolicy(true);
      recommendedIllustsFilter.setOffset(0);
      RecommendIllusts recommendedIllusts = client.getRecommendedIllusts(recommendedIllustsFilter);
      System.out.println(recommendedIllusts);
      IllustDetail detail = client.getIllustDetail(recommendedIllusts.getIllusts().get(0).getId());
      System.out.println(detail);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
```

# Contribution
## Style
Please follow the [Google coding style](https://google.github.io/styleguide/javaguide.html).  
You may apply the IntelliJ style file [here](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml).  

## Release
Follow the guide at [here](https://central.sonatype.org/pages/apache-maven.html) to setup your PGP key and 
`settings.xml`.  
Update the version in `pom.xml` appropriately.  
Then, run
```
mvn -Duser.name="$(git config --get user.name)" clean deploy -P release
```
