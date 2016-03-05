
package oucomp.indoorpos;

public class DataHelper {
  
  private static double getMean(double data[], int index, int width) {
    int halfwidth = width / 2;
    double sum = 0;
    for (int i = index - halfwidth; i <= index + halfwidth; i++) {
      if (i < 0)
        sum += data[-i];
      else if (i >= data.length)
        sum += data[data.length - (i - data.length) - 1];
      else
        sum += data[i];
    }
    return (double)sum / (halfwidth * 2 + 1);
  }
  
  public static double[] lowPassMA(double data[], int width) {
    double newdata[] = new double[data.length];
    for (int i=0; i<newdata.length; i++) {
      newdata[i] = getMean(data, i, width);
    }
    return newdata;
  }
}
