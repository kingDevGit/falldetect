package oucomp.indoorpos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccelDataReader {

  public final static int MAXLEN = 100000;
  private final static double buffer[][] = new double[MAXLEN][4];

  public static AccelRecord read(String recordClass, File datafile) throws IOException {
    if (datafile == null || !datafile.exists() || !datafile.isFile()) {
      throw new IOException("Given file is invalid");
    }
    Scanner scanner = new Scanner(datafile);
    int count = -1;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (count >= 0) {
        String item[] = line.split(",");
        if (item.length < 4) {
          System.err.println("[AccelDataReader][read] Incorrect format in line " + count + " " + item.length + " in " + datafile.getName());
        }
        buffer[count][0] = Double.parseDouble(item[1]);
        buffer[count][1] = Double.parseDouble(item[2]);
        buffer[count][2] = Double.parseDouble(item[3]);
        buffer[count][3] = Double.parseDouble(item[0]);
      }
      count++;
    }
    scanner.close();
    if (count <= 0) {
      throw new IOException("Given file content is invalid");
    }
    //System.out.println(count + " " + buffer[count-1][3]);
    AccelRecord record = new AccelRecord(recordClass, datafile.getName(), count);
    for (int i = 0; i < count; i++) {
      record.addSample(buffer[i][3], buffer[i][0], buffer[i][1], buffer[i][2]);
    }
    return record;
  }

  public static List<AccelRecord> read(File datafolder) throws IOException {
    if (datafolder == null || !datafolder.exists() || !datafolder.isDirectory()) {
      throw new IOException("Given folder is invalid");
    }
    File filearray[] = datafolder.listFiles();
    if (filearray.length == 0) {
      throw new IOException("Given folder is empty");
    }
    String recordClass = datafolder.getName();
    ArrayList result = new ArrayList();
    for (int i = 0; i < filearray.length; i++) {
      if (!filearray[i].isFile()) {
        continue;
      }
      try {
        AccelRecord record = read(recordClass, filearray[i]);
        result.add(record);
      } catch (IOException ex) {
        System.err.println("[AccelDataReader][read] Incorrect format in file '" + filearray[i].getName() + "'");
      }
    }
    return result;
  }
}
