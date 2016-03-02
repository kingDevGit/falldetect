package oucomp.falldetect;

import oucomp.falldetect.data.ImpactPeak;
import oucomp.falldetect.data.AccelRecord;
import oucomp.falldetect.data.ImpactPeakFinder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oucomp.util.PrintMessageUtils;

public class FallDetectorPeakFinder extends FallDetectorAbstract {

  public FallDetectorPeakFinder(HashMap<String, Object> properties) {
    super(properties);
    System.out.println("[FallDetectorPeakFinder]");
  }

  public String getName() {
    return "Peak Finder";
  }

  public EvaluationResult evaluate(AccelRecord record) {
    EvaluationResult result = new EvaluationResult();
    result.setRecordClass("unknown");
    result.appendNotes(PrintMessageUtils.printMap(properties));
    List<ImpactPeak> peakList = ImpactPeakFinder.extractImpactPeakList(record.getRMS(), properties);
    for (ImpactPeak p: peakList) {
      result.appendNotes(p.toString() + "\n");
    }
    record.setEvaluationResult(result);
    return result;
  }
}
