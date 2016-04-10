package oucomp.indoorpos;

public class RawDataBuffer {
  private static final int BUFFER_SIZE = 100000;
  private int size;
  private double[] rms;
  private int in = 0;
  private int outSampleID = 0;
  private int out = 0;
  private int counter = 0;
  
  private double sampleRate = 0.0;
  
  public RawDataBuffer() {
    this(BUFFER_SIZE);
  }
  
  public RawDataBuffer(int size) {
    this.size = size;
    rms = new double[size];
  }

  public double getSampleRate() {
    return sampleRate;
  }

  public void setSampleRate(double sampleRate) {
    this.sampleRate = sampleRate;
  }
  
  public synchronized boolean addSample(double data) {
    if (counter >= size)
      return false;
    rms[in] = data;
    in = (in + 1) % size;
    counter++;
    return true;
  }
  
  public synchronized boolean getSample(int startSampleID, int sampleLen, double output[]) {
    if (startSampleID < outSampleID) {
      System.err.println("[RawDataBuffer] startSampleID is earler than buffer first data");
      return false;
    }
    if (startSampleID + sampleLen > outSampleID + counter) {
      System.err.println("[RawDataBuffer] request sample range is beyond the buffer");
      return false;
    }
    if (output.length < sampleLen) {
      System.err.println("[RawDataBuffer] output buffer size is insufficient");
      return false;      
    }
    int startindex = ((startSampleID - outSampleID) + out) % size;
    //System.out.println("StartIndex = " + startindex + " " + sampleLen);
    for (int i=0; i<sampleLen; i++) {
      output[i] = rms[(startindex + i) % size];
    }
    counter -= (startSampleID - outSampleID);
    outSampleID = startSampleID;
    out = startindex;
    return true;
  }
  
  public synchronized int getSampleCount() {
    return this.counter;
  }
  
}
