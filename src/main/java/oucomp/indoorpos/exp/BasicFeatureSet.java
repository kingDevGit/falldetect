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
  
  public void evaluateBasicData(double mean, double variance, double skewness, double max, double min) {
    this.mean = mean;
    this.variance = variance;
    this.skewness = skewness;
    this.max = max;
    this.min = min;
  }

}
