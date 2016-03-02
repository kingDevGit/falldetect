package oucomp.falldetect;

import oucomp.falldetect.data.AccelRecord;
import java.util.HashMap;
import java.util.Iterator;

public abstract class FallDetectorAbstract {

  protected HashMap<String, Object> properties = null;

  public FallDetectorAbstract(HashMap<String, Object> properties) {
    this.properties = properties;
    if (this.properties == null) {
      this.properties = new HashMap();
    }
    printProperties();
  }

  public void printProperties() {
    Iterator<String> it = properties.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      Object value = properties.get(key);
      System.out.println("[Marking Property] " + key + ": " + value.toString());
    }
  }
  
  public abstract String getName();

  public abstract EvaluationResult evaluate(AccelRecord record);
}
