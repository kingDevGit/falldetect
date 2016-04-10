package oucomp.indoorpos;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class RawDataReader {
  
  public static void read(File datafile, RawDataBuffer buffer) throws IOException {
    if (datafile == null || !datafile.exists() || !datafile.isFile()) {
      throw new IOException("Given file is invalid");
    }
    Scanner scanner = new Scanner(datafile);
    int count = 0;
    int linecount = -1;
    double lasttime = 0;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (linecount >= 0) {
        String item[] = line.split(",");
        if (item.length < 4) {
          System.err.println("[RawDataReader][read] Incorrect format in line " + count + " " + item.length + " in " + datafile.getName());
        }
        double x = Double.parseDouble(item[1]);
        double y = Double.parseDouble(item[2]);
        double z = Double.parseDouble(item[3]);
        lasttime = Double.parseDouble(item[0]);
        double rms = Math.sqrt(x * x + y * y + z * z) / 9.8;
        if (!buffer.addSample(rms)) {
          System.err.println("[RawDataReader][read] The buffer is full after adding " + count + " smaples");
        }
        count++;
      }
      linecount++;
    }
    scanner.close();
    if (count > 1) {
      buffer.setSampleRate((count - 1) / lasttime);
    }
  }
  
  public static void main(String args[]) throws Exception {
    File sampleFile = new File("../../data/indoorpos/LongRoute/LongRoute1.csv");
    RawDataBuffer buffer = new RawDataBuffer();
    
    RawDataReader.read(sampleFile, buffer);
    System.out.println("Number of samples = " + buffer.getSampleCount());
  }
}
