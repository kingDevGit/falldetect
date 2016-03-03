package oucomp.indoorpos;

public class DataAnalysis {

  protected double mean;
  protected double variance;
  protected double sd;
  protected double skewness; // sample skewness
  protected double max;
  protected double min;
  protected int maxIndex;
  protected int minIndex;

  public DataAnalysis(double data[]) {
    findBasicStat(data);
  }

  private void findBasicStat(double data[]) {
    double sum = 0;
    for (int i = 0; i < data.length; i++) {
      sum += data[i];
      if (i == 0 || data[i] > max) {
        max = data[i];
        maxIndex = i;
      }
      if (i == 0 || data[i] < min) {
        min = data[i];
        minIndex = i;
      }
    }
    mean = sum / data.length;
    sum = 0;
    double m3 = 0;
    for (int i = 0; i < data.length; i++) {
      sum += (mean - data[i]) * (mean - data[i]);
      m3 += Math.pow(data[i] - mean, 3);
    }
    variance = sum / data.length;
    sd = Math.sqrt(variance);
    skewness = (m3 / data.length) / Math.pow(variance, 1.5);
  }

  public double getMean() {
    return mean;
  }

  public double getVariance() {
    return variance;
  }

  public double getSd() {
    return sd;
  }
  
  public double getSkewness() {
    return skewness;
  }

  public double getMax() {
    return max;
  }

  public double getMin() {
    return min;
  }

  public int getMaxIndex() {
    return maxIndex;
  }

  public int getMinIndex() {
    return minIndex;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("MEAN: ").append(mean).append('\n');
    sb.append("VARIANCE: ").append(variance).append('\n');
    sb.append("SD: ").append(sd).append('\n');    
    sb.append("SKEWNESS: ").append(skewness).append('\n');
    sb.append("MAX: ").append(max).append(" (").append(maxIndex).append(")\n");
    sb.append("MIN: ").append(min).append(" (").append(minIndex).append(")\n");
    return sb.toString();
  }
}
