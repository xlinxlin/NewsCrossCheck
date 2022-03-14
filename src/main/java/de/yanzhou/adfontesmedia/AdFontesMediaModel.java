package de.yanzhou.adfontesmedia;

public class AdFontesMediaModel {

  private String mediaName;

  private double reliabilityScore;

  private double biasScore;

  public String getMediaName() {
    return mediaName;
  }

  public void setMediaName(String mediaName) {
    this.mediaName = mediaName;
  }

  public double getReliabilityScore() {
    return reliabilityScore;
  }

  public void setReliabilityScore(double reliabilityScore) {
    this.reliabilityScore = reliabilityScore;
  }

  public double getBiasScore() {
    return biasScore;
  }

  public void setBiasScore(double biasScore) {
    this.biasScore = biasScore;
  }

  @Override
  public String toString() {
    return "media name: " + this.mediaName + " reliability score: " +
            this.reliabilityScore + " bias score: " + this.biasScore;
  }
}