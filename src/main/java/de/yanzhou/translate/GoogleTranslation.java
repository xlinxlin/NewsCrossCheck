package de.yanzhou.translate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Translate news title by using Google API Key.
 */
public class GoogleTranslation {

  /**
   * Set up logger for GoogleTranslation Class.
   */
  public static final Logger logger = LogManager.getLogger(GoogleTranslation.class);

  /**
   *  Set up Google translation service by using Google API Key.
   */
  private static Translate translate = TranslateOptions.newBuilder().setApiKey(getGoogleApiKey()).build().getService();

  /**
   * Translate news title from source language to target language.
   * @param newsTitle The news title.
   * @param sourceLanguage The source language, for example: "zh".
   * @param targetLanguage The target language, for example: "en".
   * @return  The translation of the news title.
   */
  public String getGoogleTranslation(String newsTitle, String sourceLanguage, String targetLanguage){
    Translation translation =
            translate.translate(
                    newsTitle,
                    Translate.TranslateOption.sourceLanguage(sourceLanguage),
                    Translate.TranslateOption.targetLanguage(targetLanguage),
                    // Use "base" for standard edition, "nmt" for the premium model.
                    Translate.TranslateOption.model("nmt"));
    return translation.getTranslatedText();
  }

  /**
   * Get the Google API Key from key.properties file.
   * @return Google API Key.
   */
  private static String getGoogleApiKey() {
    String key = null;
    InputStream in = null;
    try {
      Properties props = new Properties();
      in = GoogleTranslation.class.getClassLoader().getResourceAsStream("key.properties");
      props.load(in);
      key = props.getProperty("GOOGLE_API_KEY");
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