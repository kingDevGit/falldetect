package oucomp.indoorpos;

import java.util.ArrayList;
import java.util.List;

public class DataPeakAnalysis extends DataAnalysis {

  public static final double IGNORE_SD_LEVEL = 0.8;
  public static final double PEAK_MIN_AREARATIO = 80.0;
  public static final double PEAK_MIN_WIDTH = 10;

  protected List<Peak> peakList = new ArrayList();
  protected List<Peak> highPeakList = new ArrayList();
  protected List<Peak> lowPeakList = new ArrayList();

  public DataPeakAnalysis(double data[]) {
    super(data);
    findPeaks(data);
  }

  private void findPeaks(double data[]) {
    double sdHighLevel = mean + sd * IGNORE_SD_LEVEL;
    double sdLowLevel = mean - sd * IGNORE_SD_LEVEL;
    int state = 0; // 0: within sd region; 1: above sd region; 2: below sd region
    double peakArea = 0;
    int peakWidth = 0;
    double peakValue = 0;
    int peakIndex = 0;
    // search for peaks
    for (int i = 0; i < data.length; i++) {
      switch (state) {
        case 0:
          if (data[i] >= sdHighLevel) {
            state = 1;
            peakArea = 0;
            peakWidth = 1;
            peakValue = data[i];
          } else if (data[i] <= sdLowLevel) {
            state = 2;
            peakArea = 0;
            peakWidth = 1;
            peakValue = data[i];
          }
          break;
        case 1:
          if (data[i] < sdHighLevel) {
            //System.out.println("High Peak Found: " + i + " " + peakArea + " " + (peakArea / sd) + " " + peakWidth + " " + peakValue + " (" + peakIndex + ")");
            if ((peakArea / sd) >= PEAK_MIN_AREARATIO && peakWidth >= PEAK_MIN_WIDTH) {
              Peak newpeak = new Peak(peakIndex, peakValue, peakArea, peakWidth);
              peakList.add(newpeak);
              highPeakList.add(newpeak);
            }
            state = 0;
          } else {
            peakArea += data[i];
            peakWidth++;
            if (data[i] > peakValue) {
              peakValue = data[i];
              peakIndex = i;
            }
          }
          break;
        case 2:
          if (data[i] > sdLowLevel) {
            //System.out.println("Low Peak Found: " + i + " " + peakArea + " " + (peakArea / sd) + " " + peakWidth + " " + peakValue + " (" + peakIndex + ")");
            if ((peakArea / sd) >= PEAK_MIN_AREARATIO && peakWidth >= PEAK_MIN_WIDTH) {
              Peak newpeak = new Peak(peakIndex, peakValue, peakArea, peakWidth);
              peakList.add(newpeak);
              lowPeakList.add(newpeak);
            }
            state = 0;
          } else {
            peakArea += data[i];
            peakWidth++;
            if (data[i] < peakValue) {
              peakValue = data[i];
              peakIndex = i;
            }
          }
          break;
      }
    }
  }

  public List<Peak> getPeakList() {
    return peakList;
  }

  public List<Peak> getHighPeakList() {
    return highPeakList;
  }

  public List<Peak> getLowPeakList() {
    return lowPeakList;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString());
    int i = 0;
    sb.append("HIGH PEAKS\n");
    for (Peak p : highPeakList) {
      sb.append(String.format("%d: %s\n", i++, p.toString()));
    }
    i = 0;
    sb.append("LOW PEAKS\n");
    for (Peak p : lowPeakList) {
      sb.append(String.format("%d: %s\n", i++, p.toString()));
    }
    return sb.toString();
  }
}
