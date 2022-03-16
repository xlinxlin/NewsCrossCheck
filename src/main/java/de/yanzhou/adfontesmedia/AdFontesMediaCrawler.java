package de.yanzhou.adfontesmedia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Crawler for Ad Fontes Media https://adfontesmedia.com/
 */
public class AdFontesMediaCrawler {

  /**
   * Set up logger for AdFontesMediaCrawler Class.
   */
  private static final Logger logger = LogManager.getLogger(AdFontesMediaCrawler.class);

  /**
   * Get reliability score and bias score by giving media name.
   * @param mediaName The media name, for example: new york times
   * @return AdFontesMediaModel stores media name, reliability score and bias score.
   */
  public AdFontesMediaModel crawler(String mediaName)  {
    HttpURLConnection connection;
    String url = buildMediaUrl(mediaName);
    AdFontesMediaModel afm = new AdFontesMediaModel();
    try {
      connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setRequestMethod("HEAD");
      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        Document doc = Jsoup.connect(url).get();
        String reliabilityText = doc.select("p strong").get(2).toString();
        String biasText = doc.select("p strong").get(3).toString();
        afm.setMediaName(mediaName);
        afm.setReliabilityScore(getScore(reliabilityText));
        afm.setBiasScore(getScore(biasText));
      }
    } catch (IOException e) {
      logger.error("IOException in AdFontesMediaCrawler Class. " + e.getMessage(), e.getCause());
    }
    return afm;
  }

  /**
   * Build the adfontesmedia URL for crawler.
   * @param mediaName The media name.
   * @return The adfontesmedia URL.
   */
  private String buildMediaUrl(String mediaName){
    return "https://adfontesmedia.com/" + mediaName.replaceAll(" ", "-").toLowerCase()
            + "-bias-and-reliability/";
  }

  /**
   * Get the score.
   * @param docText The text which contains the score.
   * @return The score.
   */
  private double getScore(String docText){
    return Double.parseDouble(docText.substring(docText.indexOf(":")+1,docText.indexOf("</")).trim());
  }
}