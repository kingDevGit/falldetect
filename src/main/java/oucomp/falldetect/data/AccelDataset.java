package oucomp.falldetect.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AccelDataset {

  private File dataSetFolder;
  private static final int MAX_SAMPLECOUNT = 1000; // about 10 seconds for 100 Hz sampling rate
  private static final double sampleArray[][] = new double[MAX_SAMPLECOUNT][3];
  private Map<String, List> dataSet = new HashMap();
  private List recordList = new ArrayList();

  public AccelDataset(File dataSetFolder) throws Exception {
    this.dataSetFolder = dataSetFolder;
    loadData(dataSetFolder);
  }

  private void loadData(File dataSetFolder) throws Exception {
    if (dataSetFolder == null) {
      System.err.println("[System Error] Null Parameter");
      return;
    }
    File folderArray[] = dataSetFolder.listFiles();
    for (File theFile : folderArray) {
      if (!theFile.isDirectory()) {
        continue;
      }
      List<AccelRecord> recordListOfFolder = loadDataFolder(theFile.getName(), theFile);
      dataSet.put(theFile.getName(), recordListOfFolder);
      recordList.addAll(recordListOfFolder);
    }
  }

  private final static List<AccelRecord> loadDataFolder(String label, File theFolder) {
    List<AccelRecord> recordList = new ArrayList();
    File fileArray[] = theFolder.listFiles();
    for (File theFile : fileArray) {
      if (!theFile.isFile()) {
        continue;
      }
      if (!theFile.getName().endsWith(".txt")) {
        continue;
      }
      AccelRecord record = new AccelRecord();
      record.setRecordClass(label);
      record.setRecordLabel(theFile.getName());
      int count = 0;
      long starttime = 0;
      long timeOfSample = 0;
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(theFile)));
        while (true) {
          String line = reader.readLine();
          if (line == null) {
            break;
          }
          Scanner theScanner = new Scanner(line);
          timeOfSample = theScanner.nextLong();
          double d2x = theScanner.nextDouble();
          double d2y = theScanner.nextDouble();
          double d2z = theScanner.nextDouble();
          if (count == 0) {
            starttime = timeOfSample;
          }
          sampleArray[count][0] = d2x;
          sampleArray[count][1] = d2y;
          sampleArray[count][2] = d2z;
          count++;
        }
      } catch (Exception ex) {
        ex.printStackTrace(System.err);
      }
      record.setSampleArray(sampleArray, count);
      if (count >= 1) {
        record.setSampleRate(1 / ((timeOfSample - starttime) / 1000.0 / (count - 1)));
      }
      recordList.add(record);
    }
    return recordList;
  }
  
  public Iterator<AccelRecord> iterateRecords() {
    return recordList.iterator();
  }
  
  public List<AccelRecord> getRecordList() {
    return recordList;
  }
  
  public Iterator<String> iterateRecordClass() {
    return dataSet.keySet().iterator();
  }

  public List<AccelRecord> getDataSet(String recordClass) {
    return dataSet.get(recordClass);
  }

  public AccelRecord getRecord(String recordClass, String recordLabel) {
    if (!dataSet.containsKey(recordClass)) {
      return null;
    }
    List<AccelRecord> recordList = dataSet.get(recordClass);
    for (AccelRecord record : recordList) {
      if (record.getRecordLabel().equals(recordLabel)) {
        return record;
      }
    }
    return null;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[Dataset Folder] ").append(dataSetFolder.getAbsolutePath()).append('\n');
    Iterator<String> it = iterateRecordClass();
    while (it.hasNext()) {
      String recordClass = it.next();
      sb.append("Class: ").append(recordClass).append('\n');
      List<AccelRecord> recordList = getDataSet(recordClass);
      for (AccelRecord record : recordList) {
        sb.append(record);
      }
    }
    return sb.toString();
  }

  public static void main(String args[]) throws Exception {
    File datasetFolder = new File("../../data/fall-detection");
    System.out.println(datasetFolder.getAbsolutePath());
    AccelDataset dataSet = new AccelDataset(datasetFolder);
    System.out.println(dataSet);
  }
}
