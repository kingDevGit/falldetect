package oucomp.falldetect;

import oucomp.falldetect.data.ImpactPeak;
import oucomp.falldetect.data.AccelRecord;
import oucomp.falldetect.data.ImpactPeakFinder;
import java.util.HashMap;
import java.util.List;
import oucomp.falldetect.ml.FeatureSetBasic;
import oucomp.util.PrintMessageUtils;

public class FallDetectorFeatureExtractor extends FallDetectorAbstract {

  public FallDetectorFeatureExtractor(HashMap<String, Object> properties) {
    super(properties);
    System.out.println("[FallDetectorFeatureExtractor]");
  }

  public String getName() {
    return "Feature Extractor";
  }

  public EvaluationResult evaluate(AccelRecord record) {
    EvaluationResult result = new EvaluationResult();
    result.setRecordClass("unknown");
    result.appendNotes(PrintMessageUtils.printMap(properties));
    List<ImpactPeak> peakList = ImpactPeakFinder.extractImpactPeakList(record.getRMS(), properties);
    for (ImpactPeak p: peakList) {
      result.appendNotes(p.toString() + "\n");
    }
    FeatureSetBasic fs = FeatureSetBasic.buildFeatureSet(record);
    result.appendNotes(fs.toString() + "\n");
    record.setEvaluationResult(result);
    return result;
  }
}
