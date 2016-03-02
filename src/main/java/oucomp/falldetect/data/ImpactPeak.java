package oucomp.falldetect.data;

public class ImpactPeak extends Peak {

  protected int impactStartIndex;
  protected int impactEndIndex;
  protected int peakStartIndex;
  protected int peakEndIndex;

  public ImpactPeak() {
  }

  public ImpactPeak(Peak thePeak) {
    super.peakIndex = thePeak.getPeakIndex();
    super.peakValue = thePeak.getPeakValue();
  }

  public int getImpactStartIndex() {
    return impactStartIndex;
  }

  public void setImpactStartIndex(int impactStartIndex) {
    this.impactStartIndex = impactStartIndex;
  }

  public int getImpactEndIndex() {
    return impactEndIndex;
  }

  public void setImpactEndIndex(int impactEndIndex) {
    this.impactEndIndex = impactEndIndex;
  }

  public int getPeakStartIndex() {
    return peakStartIndex;
  }

  public void setPeakStartIndex(int peakStartIndex) {
    this.peakStartIndex = peakStartIndex;
  }

  public int getPeakEndIndex() {
    return peakEndIndex;
  }

  public void setPeakEndIndex(int peakEndIndex) {
    this.peakEndIndex = peakEndIndex;
  }

  public String toString() {
    return super.toString() + String.format(" [%d %d]\nImpact [%d %d]",
      peakStartIndex, peakEndIndex, impactStartIndex, impactEndIndex);
  }
}
