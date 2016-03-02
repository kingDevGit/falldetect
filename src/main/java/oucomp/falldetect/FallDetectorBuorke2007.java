package oucomp.falldetect;

import oucomp.falldetect.data.AccelRecord;
import java.util.HashMap;

public class FallDetectorBuorke2007 extends FallDetectorAbstract {

  protected Double lowThreshold = 0.5;
  protected Double highThreshold = 4.3;

  public FallDetectorBuorke2007(HashMap<String, Object> properties) {
    super(properties);
    if (properties.containsKey("threshold.low")) {
      this.lowThreshold = (Double) properties.get("threshold.low");
    }
    if (properties.containsKey("threshold.high")) {
      this.highThreshold = (Double) properties.get("threshold.high");
    }
    System.out.println("[FallDetectorBuorke2007] Using Threshold: " + lowThreshold + " " + highThreshold);
  }

  public String getName() {
    return "Buorke 2007";
  }

  public EvaluationResult evaluate(AccelRecord record) {
    EvaluationResult result = new EvaluationResult();
    result.setRecordClass("nofall");
    result.appendNotes("Low threshold \t:").appendNotes(lowThreshold + "\n");
    result.appendNotes("High threshold \t:").appendNotes(highThreshold + "\n");
    int sampleCount = record.getSampleCount();
    double rms[] = record.getRMS();
    boolean highPass = false;
    boolean lowPass = false;
    double largest = 0;
    double smallest = 0;
    for (int i = 0; i < sampleCount; i++) {
      if (i == 0 || rms[i] > largest) {
        largest = rms[i];
      }
      if (i == 0 || rms[i] < smallest) {
        smallest = rms[i];
      }
      if (rms[i] > highThreshold) {
        highPass = true;
      }
      if (rms[i] < lowThreshold) {
        lowPass = true;
      }
      if (lowPass && highPass) {
        result.setRecordClass("fall");
        break;
      }
    }
    result.appendNotes("Highest\t:").appendNotes(largest + "\n");
    result.appendNotes("Lowest \t:").appendNotes(smallest + "\n");
    //System.out.println(largest + " " + smallest);
    record.setEvaluationResult(result);
    return result;
  }
}
