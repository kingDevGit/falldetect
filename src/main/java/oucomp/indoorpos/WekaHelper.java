package oucomp.indoorpos;

import java.util.Random;
import weka.attributeSelection.PrincipalComponents;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class WekaHelper {

  public static Evaluation runTrainTestSplit(Instances dataset, Classifier classifier, double trainPart) throws Exception {
    int numInstances = dataset.numInstances();
    Instances randData = new Instances(dataset);
    randData.randomize(new Random(10));
    // create Instances
    FastVector atts = new FastVector();
    for (int i = 0; i < dataset.numAttributes(); i++) {
      atts.addElement(dataset.attribute(i));
    }
    Instances trainSet = new Instances("TrainSet", atts, 0);
    Instances testSet = new Instances("TestSet", atts, 0);
    trainSet.setClassIndex(dataset.classIndex());
    testSet.setClassIndex(dataset.classIndex());
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

    Instances randset = new Instances(dataset);
    //randset.randomize(new Random(10));
    //randset.stratify(10);
    //printInstances(randset);
    Evaluation eval = new Evaluation(randset);
    classifier.buildClassifier(randset);
    System.out.println(classifier);
    eval.crossValidateModel(classifier, randset, 10, new Random(10));
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
    System.out.println("True Positives  : " + eval.numTruePositives(1) + "   " + eval.truePositiveRate(1) * 100 + "%");
    System.out.println("True Negatives  : " + eval.numTrueNegatives(1) + "   " + eval.trueNegativeRate(1) * 100 + "%");
    System.out.println("False Positives : " + eval.numFalsePositives(1) + "   " + eval.falsePositiveRate(1) * 100 + "%");
    System.out.println("False Negatives : " + eval.numFalseNegatives(1) + "   " + eval.falseNegativeRate(1) * 100 + "%");
    System.out.println("Precision       : " + eval.precision(1));
    System.out.println("Recall          : " + eval.recall(1));
    System.out.println(eval.toMatrixString());
  }

  public static void printPCA(Instances dataset) {
    try {
      PrincipalComponents pca = new PrincipalComponents();
      pca.buildEvaluator(dataset);
      System.out.println("\n" + pca.toString());
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
  }

  public static void printPredictions(Evaluation evaluation, Instances dataset) {
    Attribute classAttr = dataset.classAttribute();
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%12s %10s", "INCORRECT", "ACTUAL"));
    for (int i = 1; i < dataset.numAttributes(); i++) {
      sb.append(String.format("%12s", dataset.attribute(i).name()));
    }
    sb.append("\n");
    FastVector predictions = evaluation.predictions();
    for (int i = 0, trainDataSize = dataset.numInstances(); i < trainDataSize; i++) {
      Instance instance = dataset.instance(i);
      Prediction prediction = (Prediction) predictions.elementAt(i);
      if (prediction.actual() != prediction.predicted()) {
        String predictedClass = "[" + classAttr.value((int) prediction.predicted()) + "]";
        String actualClass = classAttr.value((int) prediction.actual());
        sb.append(String.format("%12s %10s", predictedClass, actualClass));
        for (int j = 1; j < dataset.numAttributes(); j++) {
          sb.append(String.format("%12.4f", instance.value(j)));
        }
        sb.append('\n');
      }
    }
    System.out.println(sb);
  }

  public static void printAttributes(Instances dataset) {
    int num = dataset.numAttributes();
    System.out.println("Number of attributes: " + num);
    for (int i = 0; i < num; i++) {
      System.out.println(i + ": " + dataset.attribute(i).toString());
    }
  }

  public static void printInstances(Instances dataset) {
    int num = dataset.numInstances();
    System.out.println("Number of instances: " + num);
    for (int i = 0; i < num; i++) {
      System.out.println(i + ": " + dataset.instance(i).toString());
    }
  }
}
