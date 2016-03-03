package oucomp.indoorpos;

public class Peak {
  private int index;
  private double peakValue;
  private double peakArea;
  private int peakWidth;

  Peak(int index, double peakValue, double peakArea, int peakWidth) {
    this.index = index;
    this.peakValue = peakValue;
    this.peakArea = peakArea;
    this.peakWidth = peakWidth;
  }
  
  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public double getPeakValue() {
    return peakValue;
  }

  public void setPeakValue(double peakValue) {
    this.peakValue = peakValue;
  }
  
  public double getPeakArea() {
    return peakArea;
  }

  public void setPeakArea(double peakArea) {
    this.peakArea = peakArea;
  }

  public int getPeakWidth() {
    return peakWidth;
  }

  public void setPeakWidth(int peakWidth) {
    this.peakWidth = peakWidth;
  }
  
  public String toString() {
    return String.format("%d:[V:%.4f][A:%.4f][WIDTH:%d]", index, peakValue, peakArea, peakWidth);
  }
}
