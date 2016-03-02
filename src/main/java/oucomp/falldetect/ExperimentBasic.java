package oucomp.falldetect;

import oucomp.falldetect.data.AccelDataset;
import oucomp.falldetect.data.AccelRecord;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class ExperimentBasic extends ExperimentAbstract {
  
  protected final String labelPositive = "fall";
  protected final String labelNegative = "nofall";  

  public ExperimentResult test(AccelDataset dataset, FallDetectorAbstract fallDetector) throws Exception {
    // initialize Experiment result
    ExperimentResult result = new ExperimentResult(labelPositive, labelNegative);
    Iterator<AccelRecord> it = dataset.iterateRecords();
    while (it.hasNext()) {
      AccelRecord record = it.next();
      EvaluationResult er = fallDetector.evaluate(record);
      if (record.getRecordClass().equals(labelPositive)) {
        if (er.getRecordClass().equals(labelPositive)) {
          result.addTruePos(1.0);
        } else {
          result.addFalsePos(1.0);          
        }
      } else {
        if (er.getRecordClass().equals(labelPositive)) {
          result.addFalseNeg(1.0);
        } else {
          result.addTrueNeg(1.0);          
        }
      }
      if (verboseLevel >= 1) {
        System.out.print(record.getRecordClass() + ":" + record.getRecordLabel() + " ");
        System.out.println("Actual (" + record.getRecordClass() + ") Predict (" + er.getRecordClass() + ")");
      }
    }
    return result;
  }

  public static void main(String args[]) throws Exception {
    // load test data set
    File datasetFolder = new File("../../data/fall-detection");
    AccelDataset dataSet = new AccelDataset(datasetFolder);
    // call test
    ExperimentBasic theExperiment = new ExperimentBasic();
    HashMap<String, Object> properties = new HashMap();
    properties.put("threshold.low", 0.5D);
    properties.put("threshold.high", 4.3D);    
    FallDetectorAbstract fallDetector = new FallDetectorBuorke2007(properties);
    
    ExperimentResult result = theExperiment.test(dataSet, fallDetector);
    System.out.println(result);
  }
}
