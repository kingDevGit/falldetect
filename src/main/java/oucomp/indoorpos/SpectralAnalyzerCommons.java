package oucomp.indoorpos;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class SpectralAnalyzerCommons {

  protected final static FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
  protected double spikeThreshold = 1.0;
  protected LinkedHashMap<Double, Double> spikeMap = null;
  protected double spectrum[][] = null;

  public SpectralAnalyzerCommons(double signal[], int len, double sampleRate) {
    this.spectrum = generateSpectrum(signal, len, sampleRate);
    this.spikeMap = generateSpikes(this.spectrum, spikeThreshold);
  }

  protected static double[][] generateSpectrum(double signal[], int len, double sampleRate) {
    if (signal == null) {
      return null;
    }
    signal = trimmingToPower2(signal, len);
    Complex[] complex = transformer.transform(signal, TransformType.FORWARD);
    double real;
    double im;
    double spectrum[][] = new double[complex.length][2];

    for (int i = 0; i < complex.length; i++) {
      real = complex[i].getReal();
      im = complex[i].getImaginary();
      double magnitude = Math.sqrt((real * real) + (im * im));
      double frequency = (sampleRate * i) / len;
      spectrum[i][0] = frequency;
      spectrum[i][1] = magnitude;
    }
    return spectrum;
  }

  public double[][] getSpectrum() {
    return spectrum;
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

  protected static LinkedHashMap<Double, Double> generateSpikes(double spectrum[][], double spikeThreshold) {
    LinkedHashMap<Double, Double> map = new LinkedHashMap();
    for (int i = 0; i < spectrum.length; i++) {
      if (spectrum[i][1] >= spikeThreshold) {
        map.put(spectrum[i][0], spectrum[i][1]);
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
      sb.append("Spike ").append(i).append(": ").append(freq).append("Hz ");
      sb.append("(").append(amplitude).append(")\n");
      i++;
    }
  }

  public static double[] generateSignal(double period) {
    double signal[] = new double[400];
    for (int i = 0; i < signal.length; i++) {
      signal[i] = Math.sin(i / period * 2 * Math.PI);
    }
    return signal;
  }

  public static void main(String args[]) throws Exception {
    double signal[] = generateSignal(30);
    SpectralAnalyzerCommons spectralAnalyzer = new SpectralAnalyzerCommons(signal, signal.length, 30.0);
    spectralAnalyzer.printSpikes();
  }
}
