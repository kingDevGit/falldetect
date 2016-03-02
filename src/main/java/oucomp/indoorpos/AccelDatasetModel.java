package oucomp.indoorpos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccelDatasetModel {
  private List<String> recordClassList = new ArrayList();
  private HashMap<String, List<AccelRecord>> recordListTable = new HashMap();
  
  public AccelDatasetModel(File datasetFolder) throws IOException {
    init(datasetFolder);
  }
  
  private void init(File datasetFolder) throws IOException {
    if (datasetFolder == null || !datasetFolder.isDirectory()) {
      throw new IOException("[AccelDatasetModel][init] Given datasetFolder not valid");
    }
    File filearray[] = datasetFolder.listFiles();
    int count = 0;
    for (int i=0; i<filearray.length; i++) {
      if (!filearray[i].isDirectory()) {
        continue;
      }
      List<AccelRecord> recordList = AccelDataReader.read(filearray[i]);
      if (recordList.isEmpty()) {
        continue;
      }
      recordClassList.add(filearray[i].getName());
      recordListTable.put(filearray[i].getName(), recordList);
      count++;
    }
  }

  public List<String> getRecordClassList() {
    return recordClassList;
  }
  
  public List<AccelRecord> getAccelRecordList(String recordClass) {
    if (!recordListTable.containsKey(recordClass)) {
      return null;
    }
    return recordListTable.get(recordClass);
  }
  
  
}
