package oucomp.indoorpos.exp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oucomp.indoorpos.AccelDatasetModel;
import oucomp.indoorpos.AccelRecord;
import oucomp.indoorpos.DataPeakAnalysis;
import oucomp.indoorpos.SpectralAnalysis;
import oucomp.indoorpos.WekaHelper;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

public class ExperimentMultiA {

  private static AccelDatasetModel model;

  private static BasicFeatureSetSpectral createFeatureFromData(String classLabel, AccelRecord rec) {
    BasicFeatureSetSpectral fs = new BasicFeatureSetSpectral(classLabel);
    DataPeakAnalysis da = new DataPeakAnalysis(rec.getRMSArray());
    fs.evaluateBasicData(da.getMean(), da.getVariance(), da.getSkewness(), da.getMax(), da.getMin());
    fs.evaluatePeakFeatures(da);    
    
    SpectralAnalysis sa = new SpectralAnalysis(rec.getRMSArray(), rec.getSampleCount(), rec.getSampleRate());
    fs.evaluateSpectral(sa);
    return fs;
  }

  private static List<BasicFeatureSet> createFeatureSetFromData(String classLabel, List<AccelRecord> recList) {
    List<BasicFeatureSet> result = new ArrayList();
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
    Instances dataModel = FeatureSetHelper.createDataModel("IndoorPos", new String[]{"up", "down", "standstill"}, BasicFeatureSetSpectral.class);
    List<BasicFeatureSet> fsAll = createFeatureSetFromData("up", model.getAccelRecordList("ElevatorUp"));
    fsAll.addAll(createFeatureSetFromData("down", model.getAccelRecordList("ElevatorDown")));
    fsAll.addAll(createFeatureSetFromData("standstill", model.getAccelRecordList("StandStill")));
    Instances instances = FeatureSetHelper.convertToInstances(dataModel, fsAll);
    
    WekaHelper.printAttributes(dataModel);
    WekaHelper.printInstances(dataModel);
    // start training
    //Classifier classifier = new J48();
    Classifier classifier = new SMO();
    //Evaluation evaluation = WekaHelper.runTrainSetOnly(instances, classifier);
    //Evaluation evaluation = WekaHelper.runTrainTestSplit(instances, classifier, 0.5);
    Evaluation evaluation = WekaHelper.run10FoldedTest(instances, classifier);
    WekaHelper.printEvaluation(evaluation);
    WekaHelper.printPCA(instances);
    // Print incorrect predictions
    //System.out.println("THE INCORRECTLY PREDICTED CASES");
    //WekaHelper.printPredictions(evaluation, instances);
  }

}
