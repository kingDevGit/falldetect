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
import weka.core.Instances;

public class ExperimentBinaryASpectral {

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
    Instances dataModel = FeatureSetHelper.createDataModel("IndoorPos", new String[]{"push", "pull"}, BasicFeatureSetSpectral.class);
    List<BasicFeatureSetSpectral> fsAll = createFeatureSetFromData("push", model.getAccelRecordList("DoorPush"));
    fsAll.addAll(createFeatureSetFromData("pull", model.getAccelRecordList("DoorPull")));
    Instances instances = FeatureSetHelper.convertToInstances(dataModel, fsAll);

    // start training
    //Classifier classifier = new weka.classifiers.trees.Id3();
    Classifier classifier = new weka.classifiers.trees.J48();
    //Classifier classifier = new weka.classifiers.functions.LinearRegression();
    //Classifier classifier = new weka.classifiers.functions.RBFNetwork();
    //Classifier classifier = new weka.classifiers.functions.SMO();
    //Classifier classifier = new weka.classifiers.bayes.NaiveBayes();
    //Classifier classifier = new weka.classifiers.bayes.BayesNet();
    //Classifier classifier = new weka.classifiers.functions.MultilayerPerceptron();
    
    //Evaluation evaluation = WekaHelper.runTrainSetOnly(instances, classifier);
    //Evaluation evaluation = WekaHelper.runTrainTestSplit(instances, classifier, 0.5);
    Evaluation evaluation = WekaHelper.run10FoldedTest(instances, classifier);

    WekaHelper.printEvaluation(evaluation);
    WekaHelper.printPCA(instances);
    // Test with split
    System.out.println("TEST WITH SPLIT PARTS");
    WekaHelper.runTrainTestSplit(instances, classifier, 0.8);
    WekaHelper.printEvaluation(evaluation);   
    WekaHelper.printPCA(instances);
  }

}
