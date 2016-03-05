package oucomp.indoorpos;

import java.util.HashMap;

public class AccelRecord {
  protected String recordClass = null;
  protected String recordLabel = null;
  protected double sample[][] = null;
  protected double sampleRate = -1;
  protected int sampleCount = 0;
  // root-mean-square of tri-axial acceleration
  protected double rms[] = null; // in unit of g
  // estimated velocity
  protected double velocity[] = null;
  protected HashMap dataMap = new HashMap();
  protected Object evaluationResult = null;

  public AccelRecord(String recordClass, String recordLabel, int maxSampleCount) {
    this.recordClass = recordClass;
    this.recordLabel = recordLabel;
    this.sample = new double[maxSampleCount][4];
  }

  void addSample(double time, double x, double y, double z) {
    sample[sampleCount][0] = x;
    sample[sampleCount][1] = y;
    sample[sampleCount][2] = z;
    sample[sampleCount][3] = time;
    sampleCount++;
  }

  public int getSampleCount() {
    return sampleCount;
  }

  public double[][] getSamples() {
    return this.sample;
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
    return sampleCount / sample[sampleCount - 1][3];
  }

  public double getSamplePeriod() {
    return sample[sampleCount - 1][3] / sampleCount;
  }

  private void evaluateRMS() {
    rms = new double[sample.length];
    for (int i = 0; i < sampleCount; i++) {
      rms[i] = Math.sqrt(sample[i][0] * sample[i][0] + sample[i][1] * sample[i][1]
              + sample[i][2] * sample[i][2]) / 9.8;
      //System.out.print(rms[i] + " ");
    }
    // apply low pass filter
    //rms = DataHelper.lowPass(rms, 20);
  }

  public double[] getRMSArray() {
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
    for (int i = 1; i < sampleCount; i++) {
      velocity[i] = velocity[i - 1] + (rms[i - 1] - 1) * samplePeriod;
    }
  }

  public double[] getVelocityArray() {
    if (sample == null) {
      return null;
    }
    if (velocity == null) {
      evaluateVelocity();
    }
    return velocity;
  }
  
  public boolean containExtra(String key) {
    return this.dataMap.containsKey(key);
  }
  
  public void putExtra(String key, Object data) {
    this.dataMap.put(key, data);
  }
  
  public Object getExtra(String key) {
    if (!this.dataMap.containsKey(key)) {
      return null;
    }
    return this.dataMap.get(key);
  }

  public Object getEvaluationResult() {
    return evaluationResult;
  }

  public void setEvaluationResult(Object evaluationResult) {
    this.evaluationResult = evaluationResult;
  }

  public String toString() {
    return recordClass + ":" + recordLabel;
    /*
    StringBuilder sb = new StringBuilder();
    sb.append("[").append(recordClass).append("] ");
    sb.append("[").append(recordLabel).append("] ");
    sb.append(sample.length).append(" Samples at Rate ").append(sampleRate).append('\n');
    return sb.toString();
    */
  }
}
