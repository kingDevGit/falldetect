package oucomp.falldetect.ml;

import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

public class WekaHelper {

  public static Instances createInstancesWithMeta(String classLabelArray[], int featureCount) {
    FastVector atts = new FastVector();
    FastVector catAttribute = new FastVector(); // define the class attribute
    for (String c : classLabelArray) {
      catAttribute.addElement(c);
    }
    atts.addElement(new Attribute("Class", catAttribute));
    for (int i = 0; i < featureCount; i++) {
      atts.addElement(new Attribute("Feature #" + i));
    }
    Instances dataSet = new Instances("Fall-Detect", atts, 0);
    dataSet.setClassIndex(0);
    return dataSet;
  }

  public static Instances createInstances(Instances dataset) {
    FastVector atts = new FastVector();
    int num = dataset.numAttributes();
    for (int i = 0; i < num; i++) {
      atts.addElement(dataset.attribute(i));
    }
    Instances dataSet = new Instances("Fall-Detect", atts, 0);
    dataSet.setClassIndex(0);
    return dataSet;
  }

  public static Evaluation runTrainTestSplit(Instances dataset, Classifier classifier, double trainPart) throws Exception {
    int numInstances = dataset.numInstances();
    Instances randData = new Instances(dataset);
    randData.randomize(new Random(10));
    // create Instances
     FastVector atts = new FastVector();
     for (int i=0; i<dataset.numAttributes(); i++) {
       atts.addElement(dataset.attribute(i));
     }
    Instances trainSet = new Instances("TrainSet", atts, 0); 
    Instances testSet = new Instances("TestSet", atts, 0); 
    if (trainPart < 0 || trainPart > 1) {
      trainPart = 0.5;
    }
    for (int i = 0; i < numInstances; i++) {
      if (i < numInstances * trainPart) {
        trainSet.add(randData.instance(i));
      } else {
        testSet.add(randData.instance(i));
      }
    }

    Evaluation eval = new Evaluation(trainSet);
    classifier.buildClassifier(trainSet);
    System.out.println(classifier);
    eval.evaluateModel(classifier, testSet);

    return eval;
  }

  public static Evaluation run10FoldedTest(Instances dataset, Classifier classifier) throws Exception {
    /*
     Instances randData = new Instances(dataset);
     randData.randomize(new Random(10));
     randData.stratify(10);
     randData.setClassIndex(2);
     */
    dataset.setClassIndex(2);
    Evaluation eval = new Evaluation(dataset);
    classifier.buildClassifier(dataset);
    System.out.println(classifier);
    eval.crossValidateModel(classifier, dataset, 2, new Random(10));
    return eval;
  }

  public static Evaluation runTrainSetOnly(Instances dataset, Classifier classifier) throws Exception {
    Evaluation eval = new Evaluation(dataset);
    classifier.buildClassifier(dataset);
    System.out.println(classifier);
    eval.evaluateModel(classifier, dataset);
    return eval;
  }

  public static void printEvaluation(Evaluation eval) throws Exception {
    System.out.println(eval.toClassDetailsString());
    System.out.println(eval.toSummaryString());
    System.out.println("True Positives    " + eval.numTruePositives(1) + "   " + eval.truePositiveRate(1) * 100 + "%");
    System.out.println("True Negatives    " + eval.numTrueNegatives(1) + "   " + eval.trueNegativeRate(1) * 100 + "%");
    System.out.println("False Positives    " + eval.numFalsePositives(1) + "   " + eval.falsePositiveRate(1) * 100 + "%");
    System.out.println("False Negatives    " + eval.numFalseNegatives(1) + "   " + eval.falseNegativeRate(1) * 100 + "%");
    System.out.println(eval.precision(1) + " " + eval.recall(1));
  }
}
