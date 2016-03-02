package oucomp.indoorpos.exp;

import java.lang.reflect.Field;
import java.util.List;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class FeatureSetHelper {

  // the class attribute is always the first one
  public static Instances createDataModel(String relationName, String classSet[], Class theFeatureClass) {
    FastVector attributeList = new FastVector();
    FastVector catAttribute = new FastVector(); // define the class attribute
    for (String cl : classSet) {
      catAttribute.addElement(cl);
    }
    attributeList.addElement(new Attribute("class", catAttribute));
    Field fields[] = theFeatureClass.getFields();
    for (Field field : fields) {
      attributeList.addElement(new Attribute(field.getName()));
    }
    Instances dataModel = new Instances(relationName, attributeList, 0);
    dataModel.setClassIndex(0);
    return dataModel;
  }

  public static Instance createInstance(Instances dataModel, String classLabel, Object theFeatureObject) {

    Instance instance = new Instance(dataModel.numAttributes());
    instance.setValue((Attribute) dataModel.attribute(0), classLabel);
    // using reflections to access the instance variables which are features
    for (int i = 1; i < dataModel.numAttributes(); i++) {
      String featureName = dataModel.attribute(i).name();
      try {
        Field field = theFeatureObject.getClass().getField(featureName);
        if (field.getType() == String.class) {
          instance.setValue((Attribute) dataModel.attribute(i), (String) field.get(theFeatureObject));
        } else {
          instance.setValue((Attribute) dataModel.attribute(i), field.getDouble(theFeatureObject));
        }
      } catch (Exception ex) {
        ex.printStackTrace(System.err);
      }
    }
    return instance;
  }
  
    public static Instances convertToInstances(Instances dataModel, List<? extends FeatureSet> featureSetList) {
    for (FeatureSet fs : featureSetList) {
      dataModel.add(FeatureSetHelper.createInstance(dataModel, fs.classLabel, fs));
    }
    return dataModel;
  }


  public static void printAttributes(Instances dataModel) {
    System.out.println("Num Attributes = " + dataModel.numAttributes());
    for (int i = 0; i < dataModel.numAttributes(); i++) {
      System.out.println(i + ": " + dataModel.attribute(i).name());
    }
  }

  public static void printInstances(Instances dataModel) {
    System.out.println(dataModel.numInstances());
    for (int i = 0; i < dataModel.numInstances(); i++) {
      Instance inst = dataModel.instance(i);
      System.out.println(i + ": " + inst.toString());
    }
  }
}
