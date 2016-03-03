package oucomp.indoorpos.exp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oucomp.indoorpos.AccelDatasetModel;
import oucomp.indoorpos.AccelRecord;
import oucomp.indoorpos.DataAnalysis;
import oucomp.indoorpos.WekaHelper;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class ExperimentBinaryA {

  private static AccelDatasetModel model;

  private static BasicFeatureSet createFeatureFromData(String classLabel, AccelRecord rec) {
    DataAnalysis da = new DataAnalysis(rec.getRMSArray());
    rec.putExtra("SIMPLEDA", da);

    BasicFeatureSet fs = new BasicFeatureSet(classLabel);
    fs.max = da.getMax();
    fs.min = da.getMin();
    fs.mean = da.getMean();
    fs.skewness = da.getSkewness();
    fs.variance = da.getVariance();
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
    Instances dataModel = FeatureSetHelper.createDataModel("IndoorPos", new String[]{"down", "up"}, BasicFeatureSet.class);
    List<BasicFeatureSet> fsAll = createFeatureSetFromData("down", model.getAccelRecordList("StairDown"));
    fsAll.addAll(createFeatureSetFromData("up", model.getAccelRecordList("StairUp")));
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
