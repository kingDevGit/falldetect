package oucomp.indoorpos.exp;

import java.util.List;
import weka.core.Instances;

public abstract class FeatureSet {

  protected String classLabel;

  protected FeatureSet(String classLabel) {
    this.classLabel = classLabel;
  }

  public String getClassLabel() {
    return classLabel;
  }


}
