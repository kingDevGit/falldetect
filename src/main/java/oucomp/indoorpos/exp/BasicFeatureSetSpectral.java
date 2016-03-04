package oucomp.indoorpos.exp;

import java.util.LinkedHashMap;
import java.util.List;
import oucomp.indoorpos.DataPeakAnalysis;
import oucomp.indoorpos.Peak;
import oucomp.indoorpos.SpectralAnalysis;

public class BasicFeatureSetSpectral extends BasicFeatureSet {

  // peaks
  public double fewpeaks;
  public double somepeaks;  
  public double manypeaks;
  public double maxbeforemin;
  public double highpeakmean;
  public double lowpeakmean;
  // spectral
  public double peak05spec; // peak < 0.5 in spectral graph

  public BasicFeatureSetSpectral(String classLabel) {
    super(classLabel);
  }

  public void evaluatePeakFeatures(DataPeakAnalysis dp) {
    List<Peak> highPeakList = dp.getHighPeakList();
    List<Peak> lowPeakList = dp.getLowPeakList();
    highpeakmean = dp.getHighPeakValueMean();
    lowpeakmean = dp.getLowPeakValueMean();
    
    int peakcount = lowPeakList.size() + highPeakList.size();
    fewpeaks = (peakcount > 0 && peakcount <= 5) ? 1 : 0;
    somepeaks = (peakcount >= 6 && peakcount <= 10) ? 1 : 0;
    manypeaks = (peakcount > 10) ? 1 : 0;    
    int bestHighPeakIndex = -1;
    int bestLowPeakIndex = -1;
    // find the index of the lowest low-peak and the highest high-peak
    for (int i = 0; i < lowPeakList.size(); i++) {
      if (bestLowPeakIndex == -1 || lowPeakList.get(i).getPeakValue() < lowPeakList.get(bestLowPeakIndex).getPeakValue()) {
        bestLowPeakIndex = i;
      }
    }
    for (int i = 0; i < highPeakList.size(); i++) {
      if (bestHighPeakIndex == -1 || highPeakList.get(i).getPeakValue() > highPeakList.get(bestHighPeakIndex).getPeakValue()) {
        bestHighPeakIndex = i;
      }
    }
    if (bestHighPeakIndex == -1 || bestLowPeakIndex == -1) {
      maxbeforemin = -1; // this should not happen
    } else {
      if (highPeakList.get(bestHighPeakIndex).getIndex() < lowPeakList.get(bestLowPeakIndex).getIndex()) {
        maxbeforemin = 1;
      } else {
        maxbeforemin = 0;
      }
    }
  }

  public void evaluateSpectral(SpectralAnalysis sa) {
    peak05spec = 0;
    LinkedHashMap<Double, Double> spikeMap = sa.getSpikeMap();
    for (Double freq : spikeMap.keySet()) {
      Double strength = spikeMap.get(freq);
      if (freq > 0.5) {
        break;
      }
      if (freq > 0.1 && freq < 0.5 && strength > 10 && strength < 30) {
        peak05spec = 1;
        break;
      }
    }
  }
}
