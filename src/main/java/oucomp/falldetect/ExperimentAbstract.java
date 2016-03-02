package oucomp.falldetect;

import java.util.HashMap;

public abstract class ExperimentAbstract {

  protected int verboseLevel = 1;

  public ExperimentAbstract() {
  }

  public ExperimentAbstract(HashMap properties) {
    if (properties != null) {
      verboseLevel = (Integer)properties.getOrDefault("verbose.level", verboseLevel); 
    }
  }
}
