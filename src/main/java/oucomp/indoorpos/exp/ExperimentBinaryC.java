package oucomp.indoorpos.exp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import oucomp.indoorpos.AccelDatasetModel;
import oucomp.indoorpos.AccelRecord;
import oucomp.indoorpos.DataPeakAnalysis;
import oucomp.indoorpos.Peak;
import oucomp.indoorpos.SpectralAnalysis;
import oucomp.indoorpos.WekaHelper;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class ExperimentBinaryC {

  private static AccelDatasetModel model;

  private static BasicFeatureSetSpectral createFeatureFromData(String classLabel, AccelRecord rec) {
    BasicFeatureSetSpectral fs = new BasicFeatureSetSpectral(classLabel);
    DataPeakAnalysis da = new DataPeakAnalysis(rec.getRMSArray());
    rec.putExtra("SIMPLEDA", da);

    fs.max = da.getMin();
    fs.min = da.getMin();
    fs.mean = da.getMean();
    fs.skewness = da.getSkewness();
    fs.variance = da.getVariance();

    fs.peak05spec = 0;
    SpectralAnalysis sa = new SpectralAnalysis(rec.getRMSArray(), rec.getSampleCount(), rec.getSampleRate());
    rec.putExtra("SPECTRAL", sa);
    LinkedHashMap<Double, Double> spikeMap = sa.getSpikeMap();
    for (Double freq: spikeMap.keySet()) {
      Double strength = spikeMap.get(freq);
      if (freq > 0.5)
        break;
      if (freq > 0.1 && freq < 0.5 && strength > 15 && strength < 30) {
         fs.peak05spec = 1;
         break;
      }
    }
    fs.maxbeforemin = 1; // true: high peak before low peak
    List<Peak> highpeak = da.getHighPeakList();
    List<Peak> lowpeak = da.getLowPeakList();
    if (highpeak.size() == 1 && lowpeak.size() == 1) {
      fs.twopeaks = 1;
      Peak hp = highpeak.get(0);
      Peak lp = lowpeak.get(0);
      if (lp.getIndex() < hp.getIndex()) {
        fs.maxbeforemin = 0;
      }
    } else {
      fs.twopeaks = 0;
    }
    
    return fs;
  }

  private static List<BasicFeatureSetSpectral> createFeatureSetFromData(String classLabel, List<AccelRecord> recList) {
    List<BasicFeatureSetSpectral> result = new ArrayList();
    for (AccelRecord rec : recList) {
      result.add(createFeatureFromData(classLabel, rec));
    }
    return result;
  }

  public static void main(String args[]) throws Exception {
    try {
      model = new AccelDatasetModel(new File("../../data/indoorpos"));
    } catch (IOException ex) {
      System.err.println(ex);
      System.exit(1);
    }
    Instances dataModel = FeatureSetHelper.createDataModel("IndoorPos", new String[]{"down", "up"}, BasicFeatureSetSpectral.class);
    List<BasicFeatureSetSpectral> fsAll = createFeatureSetFromData("down", model.getAccelRecordList("ElevatorDown"));
    fsAll.addAll(createFeatureSetFromData("up", model.getAccelRecordList("ElevatorUp")));
    Instances instances = FeatureSetHelper.convertToInstances(dataModel, fsAll);

    // start training
    Classifier classifier = new J48();
    //Classifier classifier = new SMO();
    //Evaluation evaluation = WekaHelper.runTrainSetOnly(instances, classifier);
    //Evaluation evaluation = WekaHelper.runTrainTestSplit(instances, classifier, 0.5);
    Evaluation evaluation = WekaHelper.run10FoldedTest(instances, classifier);
    
    
    WekaHelper.printEvaluation(evaluation);
    WekaHelper.printPCA(instances);
  }

}