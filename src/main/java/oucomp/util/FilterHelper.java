package oucomp.util;

public class FilterHelper {

  public static final float ALPHA = 0.15f;

  public static double[] lowPass(double[] input, double[] output) {
    if (output == null) {
      return input;
    }
    for (int i = 0; i < input.length; i++) {
      output[i] = output[i] + ALPHA * (input[i] - output[i]);
    }
    return output;
  }
}
