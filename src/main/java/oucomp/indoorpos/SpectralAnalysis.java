package oucomp.indoorpos;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import oucomp.indoorpos.exp.BasicFeatureSetSpectral;

public class SpectralAnalysis {

  // condition to be a spike - at least 2.0 x the local mean
  public static final double SPIKE_MIN_MEANRATIO = 2.0;
  public static final double SPIKE_MIN_STRENGTH = 1.0;

  protected final static FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
  protected double spikeThreshold = 1.0;
  protected LinkedHashMap<Double, Double> spikeMap = null;
  protected double spectrum[][] = null;
  protected double mean[] = null;

  public SpectralAnalysis(double signal[], int len, double sampleRate) {
    this.spectrum = generateSpectrum(signal, len, sampleRate);
    this.spectrum[1][0] = 0; // set the DC component to 0 before calculating the mean
    this.mean = DataHelper.lowPassMA(spectrum[1], 30);
    this.spikeMap = generateSpikes(this.spectrum, this.mean, sampleRate);
  }

  protected static double[][] generateSpectrum(double signal[], int len, double sampleRate) {
    if (signal == null) {
      return null;
    }
    signal = trimmingToPower2(signal, len);
    len = signal.length;
    Complex[] complex = transformer.transform(signal, TransformType.FORWARD);
    double real;
    double im;
    double spectrum[][] = new double[2][complex.length];

    for (int i = 0; i < complex.length; i++) {
      real = complex[i].getReal();
      im = complex[i].getImaginary();
      double magnitude = Math.sqrt((real * real) + (im * im));
      double frequency = (sampleRate * i) / len;
      spectrum[0][i] = frequency;
      spectrum[1][i] = magnitude;
    }
    return spectrum;
  }

  public double[][] getSpectrum() {
    return spectrum;
  }

  public LinkedHashMap<Double, Double> getSpikeMap() {
    return spikeMap;
  }

  public double[] getMeanArray() {
    return this.mean;
  }

  private static double[] paddingToPower2(double signal[], int len) {
    return Arrays.copyOf(signal, nextPower2(len));
  }

  private static double[] trimmingToPower2(double signal[], int len) {
    return Arrays.copyOf(signal, lastPower2(len));

  }

  private static int nextPower2(int n) {
    double y = Math.floor(Math.log10(n) / Math.log10(2));
    return (int) Math.pow(2, y + 1);
  }

  private static int lastPower2(int n) {
    double y = Math.floor(Math.log10(n) / Math.log10(2));
    return (int) Math.pow(2, y);
  }

  protected static LinkedHashMap<Double, Double> generateSpikes(double spectrum[][], double mean[], double sampleRate) {
    LinkedHashMap<Double, Double> map = new LinkedHashMap();
    int state = 0;
    int peakIndex = 0;
    int peakWidth = 0;
    double peakValue = 0;
    for (int i = 0; i < spectrum[1].length; i++) {
      if (i == 0) {
        continue; // skip the DC value
      }      //System.out.println(spectrum[0][i] + ": " + spectrum[1][i]);
      switch (state) {
        case 0:
          if (spectrum[1][i] >= mean[i]) {
            state = 1;
            peakIndex = i;
            peakWidth = 1;
            peakValue = spectrum[1][i];
          }
          break;
        case 1:
          if (spectrum[1][i] < mean[i]) {
            if (spectrum[1][peakIndex] > mean[peakIndex] * SPIKE_MIN_MEANRATIO && spectrum[1][peakIndex] >= SPIKE_MIN_STRENGTH) {
              if (spectrum[0][peakIndex] <= sampleRate / 2) { // remove all aliased spikes
                //System.out.println("Spectral Spike Found: " + i + " " + peakWidth + " " + peakValue + " (" + spectrum[0][peakIndex] + " Hz)");
                map.put(spectrum[0][peakIndex], spectrum[1][peakIndex]);
              }
            }
            state = 0;
          } else {
            peakWidth++;
            if (spectrum[1][i] > peakValue) {
              peakValue = spectrum[1][i];
              peakIndex = i;
            }
          }
          break;
      }
    }
    return map;
  }

  protected static LinkedHashMap<Double, Double> generateSpikesSimple(double spectrum[][]) {
    LinkedHashMap<Double, Double> map = new LinkedHashMap();
    for (int i = 0; i < spectrum[1].length; i++) {
      if (spectrum[1][i] >= SPIKE_MIN_STRENGTH) {  // hard coded to 1.0
        map.put(spectrum[0][i], spectrum[1][i]);
      }
    }
    return map;
  }

  public void printSpikes() {
    StringBuilder sb = new StringBuilder();
    Iterator<Double> it = spikeMap.keySet().iterator();
    int i = 0;
    while (it.hasNext()) {
      Double freq = it.next();
      Double amplitude = spikeMap.get(freq);
      if (freq > 1)
        continue;
      sb.append("Spike ").append(i).append(": ").append(freq).append(" Hz ");
      sb.append("(").append(amplitude).append(")\n");
      i++;
    }
    System.out.println(sb);
  }

  public static double[] generateSignal(double period) {
    double signal[] = new double[1000];
    for (int i = 0; i < signal.length; i++) {
      signal[i] = Math.sin(i / period * 2 * Math.PI);
    }
    return signal;
  }

  public void test() {
    // generate a period 30 and samplerate 60 per second signal, which is equal to 2 cycles per second or 2 Hz
    double signal[] = generateSignal(30);
    SpectralAnalysis spectralAnalyzer = new SpectralAnalysis(signal, signal.length, 60.0);
    spectralAnalyzer.printSpikes();
  }

  public static void main(String args[]) throws Exception {
    AccelDatasetModel model = null;
    try {
      model = new AccelDatasetModel(new File("../../data/indoorpos"));
    } catch (IOException ex) {
      System.err.println(ex);
      System.exit(1);
    }
    List<AccelRecord> list = model.getAccelRecordList("StandStill");
    for (AccelRecord record : list) {
      System.out.println(record.getRecordLabel());
      SpectralAnalysis sa = new SpectralAnalysis(record.getRMSArray(), record.getSampleCount(), record.getSampleRate());
      sa.printSpikes();
      BasicFeatureSetSpectral fs = new BasicFeatureSetSpectral(record.getRecordLabel());
      fs.evaluateSpectral(sa);
      //System.out.println("peak05spec = " + fs.peak05spec);
    }
  }
}
