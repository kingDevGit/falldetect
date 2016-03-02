package oucomp.falldetect;

import java.io.File;
import java.io.FileInputStream;
import oucomp.falldetect.data.ImpactPeak;
import oucomp.falldetect.data.AccelRecord;
import oucomp.falldetect.data.ImpactPeakFinder;
import java.util.HashMap;
import java.util.List;
import oucomp.falldetect.ml.FeatureSetBasic;
import oucomp.util.PrintMessageUtils;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.SerializationHelper;

public class FallDetectorMLJ48 extends FallDetectorAbstract {

  Classifier theClassifier;
  File classifierFile = new File("../../data/fall-detection/classifier-j48.ser");

  public FallDetectorMLJ48(HashMap<String, Object> properties) {
    super(properties);
    System.out.println("[FallDetectorMLJ48]");
    try {
      theClassifier = (Classifier) SerializationHelper.read(new FileInputStream(classifierFile));
      System.out.println("[FallDetectorMLJ48] Successfully loaded classifier from " + classifierFile.getPath());      
    } catch (Exception ex) {
      System.out.println("[FallDetectorMLJ48] Error loading classifier from " + classifierFile.getPath());
    }
  }

  public String getName() {
    return "Machine Learning - J48";
  }

  public EvaluationResult evaluate(AccelRecord record) {
    EvaluationResult result = new EvaluationResult();
    result.setRecordClass("unknown");
    result.appendNotes(PrintMessageUtils.printMap(properties));
    List<ImpactPeak> peakList = ImpactPeakFinder.extractImpactPeakList(record.getRMS(), properties);
    for (ImpactPeak p : peakList) {
      result.appendNotes(p.toString() + "\n");
    }
    FeatureSetBasic fs = FeatureSetBasic.buildFeatureSet(record);
    result.appendNotes(fs.toString() + "\n");
    // classify the record
    Instance instance = fs.getInstance();
    try {
      double classifiedValue = theClassifier.classifyInstance(instance);
      result.appendNotes("Predicted Class: " + classifiedValue + " (" + FeatureSetBasic.getClassLabel((int)classifiedValue) + ")\n");
      result.setRecordClass(FeatureSetBasic.getClassLabel((int)classifiedValue));
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    record.setEvaluationResult(result);
    return result;
  }
}
