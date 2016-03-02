package oucomp.indoorpos.exp;

public class BasicFeatureSet extends FeatureSet {

  public double mean;
  public double variance;
  public double skewness; // sample skewness
  public double max;
  public double min;

  public BasicFeatureSet(String classLabel) {
    super(classLabel);
  }

}
