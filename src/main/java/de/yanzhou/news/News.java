package de.yanzhou.news;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * Get news by using newsapi.
 */
public class News {
  /**
   * Set up logger for News Class.
   */
  private static final Logger logger = LogManager.getLogger(News.class);

  /**
   * Set up newsapi search address.
   */
  private static final String NEWS_API_SEARCH_ADDRESS = "https://newsapi.org/v2/everything?q=";

  /**
   * Get news by using newsapi.
   * @param newsTitle The news title.
   * @return The articles information which are stored in a JSONArray.
   */
  public JSONArray getNews(String newsTitle)  {
    String uri = NEWS_API_SEARCH_ADDRESS + newsTitle +"&from=" + buildDate() + "&searchIn=title&sortBy=publishedAt&apiKey=" + getNewsApiKey();
    System.out.println(uri);
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      logger.error("IOException." + e.getMessage(), e.getCause());
    } catch (InterruptedException e) {
      logger.error("InterruptedException." + e.getMessage(), e.getCause());
    }
    JSONObject jsonObject = new JSONObject(response.body());
    return (JSONArray) jsonObject.get("articles");
    /*
    for(int i = 0; i < arr.length(); i++){
      arr.getJSONObject(0).get()
    }
    System.out.println(arr.get(0).);
    */
  }

  /**
   * Build the date.
   * @return The previous month date.
   */
  private String buildDate(){
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -2);
    return format.format(calendar.getTime());
  }

  /**
   * Get newsapi key from key.properties file.
   * @return NEWS_API_KEY.
   */
  private String getNewsApiKey() {
    String key = null;
    InputStream in = null;
    try {
      Properties props = new Properties();
      in = getClass().getClassLoader().getResourceAsStream("key.properties");
      props.load(in);
      key = props.getProperty("NEWS_API_KEY");
    } catch (FileNotFoundException e) {
      logger.error("File key.properties not found." + e.getMessage(), e.getCause());
    } catch (IOException e) {
      logger.error("File key.properties can not be read." + e.getMessage(), e.getCause());
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.error("File key.properties can not be closed." + e.getMessage(), e.getCause());
        }
      }
    }
    return key;
  }
}