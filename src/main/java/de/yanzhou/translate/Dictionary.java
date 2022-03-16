package de.yanzhou.translate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Look up word from https://dictionaryapi.dev/
 */
public class Dictionary {

  /**
   * Set up logger for Dictionary Class.
   */
  private static final Logger logger = LogManager.getLogger(Dictionary.class);

  /**
   * Check if the word is noun or is a special word.
   * @param word The word for looking up.
   * @return True if the word is noun, or it is a special word.
   */
  public Boolean getWord(String word)  {
    boolean isWordNoun = false;
    String uri = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
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
    if(response.body().startsWith("[")) {
      JSONArray jsonArray = new JSONArray(response.body());
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      JSONArray jsonArrayMeaning = (JSONArray) jsonObject.get("meanings");
      for (int i = 0; i < jsonArrayMeaning.length(); i++) {
        isWordNoun = jsonArrayMeaning.getJSONObject(i).get("partOfSpeech").equals("noun");
        if (isWordNoun)  break;
      }
    } else {
      isWordNoun = true;
    }
    return isWordNoun;
  }
}