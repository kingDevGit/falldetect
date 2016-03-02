package oucomp.util;

import java.util.Iterator;
import java.util.Map;

public class PrintMessageUtils {

  public static String printMap(Map map) {
    StringBuilder sb = new StringBuilder();
    Iterator it = map.keySet().iterator();
    while (it.hasNext()) {
      Object key = it.next();
      sb.append(key).append(" : ").append(map.get(key)).append('\n');
    }
    return sb.toString();
  }
}
