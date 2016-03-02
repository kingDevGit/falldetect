package oucomp.falldetect.data;

public class Peak {
  protected int peakIndex;
  protected double peakValue;

  public int getPeakIndex() {
    return peakIndex;
  }

  public void setPeakIndex(int peakIndex) {
    this.peakIndex = peakIndex;
  }

  public double getPeakValue() {
    return peakValue;
  }

  public void setPeakValue(double peakValue) {
    this.peakValue = peakValue;
  }
  
  public String toString() {
    return String.format("Peak (%d) %.2f", peakIndex, peakValue);
  } 
}
