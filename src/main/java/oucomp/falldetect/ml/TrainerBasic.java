package oucomp.falldetect.ml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import oucomp.falldetect.data.AccelDataset;
import oucomp.falldetect.data.AccelRecord;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class TrainerBasic {

  public static void main(String args[]) throws Exception {
    File datasetFolder = new File("../../data/fall-detection");
    AccelDataset theDataset = new AccelDataset(datasetFolder);
    List<AccelRecord> recordList = theDataset.getRecordList();
    
    Instances instances = FeatureSetBasic.convertToInstances(FeatureSetBasic.buildFeatureSet(recordList));
    // start training
    Classifier classifier = new J48();
    Evaluation evaluation = WekaHelper.runTrainSetOnly(instances, classifier);
    WekaHelper.printEvaluation(evaluation);
    // save classifier
   SerializationHelper.write(new FileOutputStream(new File("../../data/fall-detection/classifier-j48.ser")), classifier);
  }
}
