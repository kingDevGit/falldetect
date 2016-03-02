package oucomp.falldetect.data;

public class AccelRecord {

  protected String recordClass = null;
  protected String recordLabel = null;
  protected double sample[][] = null;
  protected double sampleRate = 100; // in Hertz
  // root-mean-square of tri-axial acceleration
  protected double rms[] = null;
  // estimated velocity
  protected double velocity[] = null;
  
  protected Object evaluationResult = null;

  void setSampleArray(double sample[][], int sampleCount) {
    this.sample = new double[sampleCount][3];
    for (int i = 0; i < sampleCount; i++) {
      this.sample[i][0] = sample[i][0];
      this.sample[i][1] = sample[i][1];
      this.sample[i][2] = sample[i][2];
    }
    rms = null;
  }

  public int getSampleCount() {
    if (sample == null) {
      return 0;
    }
    return sample.length;
  }

  public String getRecordLabel() {
    return recordLabel;
  }

  public void setRecordLabel(String label) {
    this.recordLabel = label;
  }

  public String getRecordClass() {
    return recordClass;
  }

  public void setRecordClass(String recordClass) {
    this.recordClass = recordClass;
  }

  public double getSampleRate() {
    return sampleRate;
  }

  public void setSampleRate(double sampleRate) {
    this.sampleRate = sampleRate;
  }
  
  public double samplePeriod() {
    return 1 / sampleRate;
  }  

  public double[][] getSamples() {
    return this.sample;
  }

  private void evaluateRMS() {
    rms = new double[sample.length];
    for (int i = 0; i < sample.length; i++) {
      rms[i] = Math.sqrt(sample[i][0] * sample[i][0] + sample[i][1] * sample[i][1]
              + sample[i][2] * sample[i][2]) / 9.8;
    }
  }

  public double[] getRMS() {
    if (sample == null) {
      return null;
    }
    if (rms == null) {
      evaluateRMS();
    }
    return rms;
  }

  private void evaluateVelocity() {
    if (rms == null) {
      evaluateRMS();
    }
    velocity = new double[sample.length];
    velocity[0] = 0;
    double samplePeriod = 1 / sampleRate;
    for (int i = 1; i < sample.length; i++) {
      velocity[i] = velocity[i-1] + (rms[i-1] - 1) * samplePeriod;
    }
  }

  public double[] getVelocity() {
    if (sample == null) {
      return null;
    }
    if (velocity == null) {
      evaluateVelocity();
    }
    return velocity;
  }

  public Object getEvaluationResult() {
    return evaluationResult;
  }

  public void setEvaluationResult(Object evaluationResult) {
    this.evaluationResult = evaluationResult;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[").append(recordClass).append("] ");
    sb.append("[").append(recordLabel).append("] ");
    sb.append(sample.length).append(" Samples at Rate ").append(sampleRate).append('\n');
    return sb.toString();
  }
}
