# Pixivj
A java client for Pixiv.

![Java CI](https://github.com/hanshsieh/pixivj/workflows/Java%20CI/badge.svg)  
[![Maven Central](https://img.shields.io/maven-central/v/com.github.hanshsieh/pixivj.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.hanshsieh%22%20AND%20a:%22pixivj%22)  
[Pixiv API doc](https://hanshsieh.github.io/pixiv-api-doc)

Currently, only a small subset of the APIs are implemented.

# Usage
## Getting Started
Add dependency to your project.  
Checkout [here](https://mvnrepository.com/artifact/com.github.hanshsieh/pixivj).  
If you want to try out snapshot versions, see [here](https://oss.sonatype.org/content/repositories/snapshots/com/github/hanshsieh/pixivj/) for the available versions.  
You can allow your project to pull snapshot versions by adding the following profile to your `pom.xml` (for Maven)
```xml
<project>
    <profiles>
        <profile>
            <id>dev</id>
            <activation><activeByDefault>true</activeByDefault></activation>
            <repositories>
                <repository>
                    <id>snapshots-repo</id>
                    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
                    <releases><enabled>false</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>
</project>
```
Here's a sample code:
```java
import com.github.hanshsieh.pixivj.api.PixivApiClient;
import com.github.hanshsieh.pixivj.model.FilterType;
import com.github.hanshsieh.pixivj.model.SearchIllustsFilter;
import com.github.hanshsieh.pixivj.model.SearchTarget;
import com.github.hanshsieh.pixivj.model.SearchedIllusts;
import com.github.hanshsieh.pixivj.token.FixedTokenProvider;

public class Demo {
  public static void main(String[] args) throws Exception {
    FixedTokenProvider tokenProvider =
        new FixedTokenProvider("your_access_token");
    PixivApiClient client = new PixivApiClient.Builder()
        .setTokenProvider(tokenProvider)
        .build();
    SearchIllustsFilter searchIllustFilter = new SearchIllustsFilter();
    searchIllustFilter.setFilter(FilterType.FOR_ANDROID);
    searchIllustFilter.setIncludeTranslatedTagResults(true);
    searchIllustFilter.setMergePlainKeywordResults(true);
    searchIllustFilter.setOffset(0);
    searchIllustFilter.setSearchTarget(SearchTarget.PARTIAL_MATCH_FOR_TAGS);
    searchIllustFilter.setWord("swimsuit");
    SearchedIllusts searchedIllusts = client.searchIllusts(searchIllustFilter);
    System.out.println(searchedIllusts);
  }
}
```
The example code assumes that you can get the access token from somewhere.    
Since 2021/02/08, Pixiv no longer supports logging in with username/password by calling its
RESTful API. To login, user must login via its web interface.   
For desktop computers, checkout [pixivj-jfx](https://github.com/hanshsieh/pixivj-jfx), which
displays a web view using JavaFX.  
Then, you can compile your project with
```bash
mvn package
```
## Authentication
Pixiv has two set of RESTful APIs.  
One is under the URL `https://app-api.pixiv.net`. It's used, for example, getting illustrations.
Most of the time, you will use the APIs under this URL. It's implemented by 
`com.github.hanshsieh.pixivj.api.PixivApiClient`.  
Another one is under the URL `https://oauth.secure.pixiv.net`. It's used for doing authentication.
It's implemented by `com.github.hanshsieh.pixivj.api.PixivOAuthClient`.  
After authentication is completed, you can obtain two tokens:
- Access token
  A short-lived token. You need this token when calling the APIs under `https://app-api.pixiv.net`. 
- Refresh token
  Its lifetime is longer. You can use this token to get a new access token and refresh token.

## Refreshing Access Token
If you have a refresh token you can use it to refresh the access token.  
There're some implementations that help you refresh the access tokens. Checkout the classes that
implements `com.github.hanshsieh.pixivj.token.TokenRefresher`.  
For example, `com.github.hanshsieh.pixivj.token.ThreadedTokenRefresher` will refresh the access
token periodically in a background thread.

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
