# PixivJ
A java client for Pixiv

![Java CI](https://github.com/hanshsieh/pixivj/workflows/Java%20CI/badge.svg)  
[![Maven Central](https://img.shields.io/maven-central/v/com.github.hanshsieh/pixivj.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.hanshsieh%22%20AND%20a:%22pixivj%22)

Currently, only a small subset of the APIs are implemented.

# Usage
```java
PixivClient client = new PixivClient.Builder()
    .build();
Credential credential = new Credential();
credential.setUsername("your_user_name");
credential.setPassword("your_password");
try {
  AuthResult authResult = client.login(credential);
  System.out.println(authResult);

  RankedIllustsFilter filter = new RankedIllustsFilter();
  filter.setMode(FilterMode.DAY_MALE);
  filter.setFilter(FilterType.FOR_ANDROID);
  filter.setOffset(0);
  RankedIllusts rankedIllusts = client.getRankedIllusts(filter);
  System.out.println(rankedIllusts);
  
  RecommendedIllustsFilter recommendedIllustsFilter = new RecommendedIllustsFilter();
  recommendedIllustsFilter.setFilter(FilterType.FOR_IOS);
  recommendedIllustsFilter.setIncludePrivacyPolicy(true);
  recommendedIllustsFilter.setOffset(0);
  RecommendIllusts recommendedIllusts = client.getRecommendedIllusts(recommendedIllustsFilter);
  System.out.println(recommendedIllusts);
  IllustDetail detail = client.getIllustDetail(recommendedIllusts.getIllusts().get(0).getId());
  System.out.println(detail);
} catch (AuthException ex) {
  System.out.println(ex.getMessage());
  System.out.println(ex.getError().getDetails().getSystem().getMessage());
}
```

# Release
Follow the guide at [here](https://central.sonatype.org/pages/apache-maven.html) to setup your PGP key and 
`settings.xml`.  
Update the version in `pom.xml` appropriately.  
Then, run
```
mvn clean deploy -P release
```
