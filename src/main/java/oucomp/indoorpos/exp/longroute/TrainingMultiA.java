package oucomp.indoorpos.exp.longroute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import oucomp.indoorpos.AccelDatasetModel;
import oucomp.indoorpos.AccelRecord;
import oucomp.indoorpos.DataPeakAnalysis;
import oucomp.indoorpos.SpectralAnalysis;
import oucomp.indoorpos.WekaHelper;
import oucomp.indoorpos.exp.BasicFeatureSet;
import oucomp.indoorpos.exp.BasicFeatureSetSpectral;
import oucomp.indoorpos.exp.FeatureSetHelper;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class TrainingMultiA {

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
    Instances dataModel = FeatureSetHelper.createDataModel("IndoorPos",
            new String[]{"up", "down", "standstill", "stairup", "stairdown", "walking", "pull", "push"}, BasicFeatureSetSpectral.class);
    List<BasicFeatureSet> fsAll = createFeatureSetFromData("up", model.getAccelRecordList("ElevatorUp"));
    fsAll.addAll(createFeatureSetFromData("down", model.getAccelRecordList("ElevatorDown")));
    fsAll.addAll(createFeatureSetFromData("standstill", model.getAccelRecordList("StandStill")));
    fsAll.addAll(createFeatureSetFromData("stairup", model.getAccelRecordList("StairUp")));
    fsAll.addAll(createFeatureSetFromData("stairdown", model.getAccelRecordList("StairDown")));
    fsAll.addAll(createFeatureSetFromData("walking", model.getAccelRecordList("Walking")));
    fsAll.addAll(createFeatureSetFromData("pull", model.getAccelRecordList("DoorPull")));
    fsAll.addAll(createFeatureSetFromData("push", model.getAccelRecordList("DoorPush")));

    Instances instances = FeatureSetHelper.convertToInstances(dataModel, fsAll);

    WekaHelper.printAttributes(dataModel);
    WekaHelper.printInstances(dataModel);
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
    // save serialized objects
    ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("../../data/indoorpos/j48.model"));
    oos.writeObject(classifier);
    oos.flush();
    oos.close();
  }

}
