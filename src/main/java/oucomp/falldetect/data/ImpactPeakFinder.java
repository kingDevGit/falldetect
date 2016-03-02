package oucomp.falldetect.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;

public class ImpactPeakFinder {

  private static final Mean theMean = new Mean();
  private static final Max theMax = new Max();

  public static List<ImpactPeak> extractImpactPeakList(double rms[], HashMap<String, Object> properties) {
    int endWindow = 100;
    double endThreshold = 1.5;
    int startWindow = 120;
    double startThreshold = 1.5;
    double startLowThreshold = 0.8;
    double peakPeriodThreshold = 1.8;
    if (properties != null) {
      endWindow = (Integer) properties.getOrDefault("impact.endWindow", endWindow);
      endThreshold = (Double) properties.getOrDefault("impact.endThreshold", endThreshold);
      startWindow = (Integer) properties.getOrDefault("impact.startWindow", startWindow);
      startThreshold = (Double) properties.getOrDefault("impact.startThreshold", startThreshold);
      startLowThreshold = (Double) properties.getOrDefault("impact.startLowThreshold", startLowThreshold);
      peakPeriodThreshold = (Double) properties.getOrDefault("impact.peakPeriodThreshold", peakPeriodThreshold);
    }
    ArrayList<ImpactPeak> impactPeakList = new ArrayList();
    List<Peak> peakList = extractPeakList(rms, properties);
    for (Peak p : peakList) {
      ImpactPeak impactPeak = new ImpactPeak(p);
      // find impact end time
      int endIndex = Math.min(p.peakIndex + endWindow, rms.length);
      for (int i = p.peakIndex; i < endIndex; i++) {
        if (rms[i] <= endThreshold) {
          impactPeak.setImpactEndIndex(i - 1);
          break;
        }
      }
      // find impact start time
      int startIndex = Math.max(0, impactPeak.getImpactEndIndex() - startWindow);
      boolean lowThresholdFound = false;
      for (int i = startIndex; i < p.peakIndex; i++) {
        if (rms[i] < startLowThreshold) {
          lowThresholdFound = true;
          continue;
        }
        if (rms[i] > startThreshold) {
          if (lowThresholdFound) {
            impactPeak.setImpactStartIndex(i);
            break;
          } else {
            impactPeak.setImpactStartIndex(p.peakIndex);
            break;
          }
        }
      }
      // find peak start and peak end
      for (int i = p.peakIndex + 1; i < rms.length; i++) {
        if (rms[i] < peakPeriodThreshold) {
          impactPeak.setPeakEndIndex(i);
          break;
        }
      }
      for (int i = p.peakIndex - 1; i >= 0 ; i--) {
        if (rms[i] < peakPeriodThreshold) {
          impactPeak.setPeakStartIndex(i);
          break;
        }
      }
      impactPeakList.add(impactPeak);
    }
    return impactPeakList;
  }

  public static List<Peak> extractPeakList(double rms[], HashMap<String, Object> properties) {
    int windowSize = 50;
    double aboveMeanThreshold = 1.5;
    double minThreshold = 4.0;
    if (properties != null) {
      windowSize = (Integer) properties.getOrDefault("window.size", windowSize);
      aboveMeanThreshold = (Double) properties.getOrDefault("threshold.abovemean", aboveMeanThreshold);
      minThreshold = (Double) properties.getOrDefault("threshold.min", minThreshold);
    }
    //System.out.println(windowSize + " " + aboveMeanThreshold + " " + minThreshold);
    int halfWindowSize = windowSize / 2;
    ArrayList<Peak> peakList = new ArrayList();
    boolean isIncreasing = true;
    for (int i = 1; i < rms.length; i++) {
      if (rms[i] < rms[i - 1]) {
        if (isIncreasing) {
          if (rms[i - 1] < minThreshold) {
            continue;
          }
          double mean = 0;
          double max = 0;
          int len = windowSize;
          if (i - halfWindowSize < 0) {
            if (len > rms.length) {
              len = rms.length;
            }
            mean = theMean.evaluate(rms, 0, len);
            max = theMax.evaluate(rms, 0, len);
          } else {
            if (i - halfWindowSize + len > rms.length) {
              len = rms.length - i + halfWindowSize;
            }
            mean = theMean.evaluate(rms, i - halfWindowSize, len);
            max = theMax.evaluate(rms, i - halfWindowSize, len);
          }
          //System.out.println(rms[i - 1] + " " + mean + " " + max + " " + (rms[i - 1] / mean) + " " + (rms[i - 1] >= max));
          if (rms[i - 1] >= max && rms[i - 1] / mean >= aboveMeanThreshold) {
            Peak newPeak = new Peak();
            newPeak.setPeakIndex(i - 1);
            newPeak.setPeakValue(rms[i - 1]);
            peakList.add(newPeak);
          }
        }
        isIncreasing = false;
      } else {
        isIncreasing = true;
      }
    }
    return peakList;
  }

  public static void main(String args[]) throws Exception {
    File datasetFolder = new File("../../data/fall-detection");
    AccelDataset dataSet = new AccelDataset(datasetFolder);
    HashMap<String, Object> properties = new HashMap();
    properties.put("window.size", 25);
    properties.put("threshold.abovemean", 1.5);
    properties.put("threshold.min", 4.0);
    for (AccelRecord record : dataSet.getRecordList()) {
      System.out.println(record.getRecordLabel());
      List<Peak> peakList = ImpactPeakFinder.extractPeakList(record.getRMS(), properties);
      for (Peak p : peakList) {
        System.out.println(p);
      }
    }
  }
}
