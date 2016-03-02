package oucomp.indoorpos.exp;

import static oucomp.indoorpos.exp.BasicFeatureSet.featureArray;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public abstract class FeatureSet {

  protected static String classLabelArray[];
  private static Instances dataSet = null;
  
  protected String classLabel = "UNKNOWN";


  protected static Instances getDataSetInstances() {
    if (dataSet == null) {
      initClassLabelSet("Default", new String[]{"CLASS 0", "CLASS 1"});
    }
    return dataSet;
  }

  public static void initClassLabelSet(String relationName, String classLabelArray[]) {
    FeatureSet.classLabelArray = classLabelArray;
    FastVector atts = new FastVector();
    FastVector catAttribute = new FastVector(); // define the class attribute
    for (String cl : classLabelArray) {
      catAttribute.addElement(cl);
    }
    atts.addElement(new Attribute("class", catAttribute));
    for (String featureName : featureArray) {
      atts.addElement(new Attribute(featureName));
    }
    dataSet = new Instances(relationName, atts, 0);
    dataSet.setClassIndex(0);
  }

  public String getClassLabel() {
    return classLabel;
  }

  public void setClassLabel(String classLabel) {
    this.classLabel = classLabel;
  }
  
  public static String[] getClassLabelArray() {
    return classLabelArray;
  }

  public static String getClassLabel(int classifiedValue) {
    Attribute catAttr = dataSet.attribute(0);
    return catAttr.value(classifiedValue);
  }

  public abstract Instance getInstance();

}
