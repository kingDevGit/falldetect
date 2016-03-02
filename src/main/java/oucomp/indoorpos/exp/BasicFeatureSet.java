package oucomp.indoorpos.exp;

import java.lang.reflect.Field;
import java.util.List;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class BasicFeatureSet extends FeatureSet {

  public static final String featureArray[] = {"mean", "variance", "skewness", "max", "min"};
  public double mean;
  public double variance;
  public double skewness; // sample skewness
  public double max;
  public double min;



  public static Instances convertToInstances(List<BasicFeatureSet> featureSetList) {
    Instances instances = getDataSetInstances();
    for (FeatureSet fs : featureSetList) {
      instances.add(fs.getInstance());
    }
    return instances;
  }

  public Instance getInstance() {
    Instances dataSet = getDataSetInstances();
    Instance instance = new Instance(featureArray.length + 1);
    instance.setDataset(dataSet);
    instance.setValue((Attribute) dataSet.attribute(0), super.classLabel);
    // using reflections to access the instance variables which are features
    for (int i=0; i<featureArray.length; i++) {
      String featureName = featureArray[i];
      try {
        Field field = this.getClass().getField(featureName);
        if (field.getType() == String.class) {
          instance.setValue((Attribute) dataSet.attribute(i+1), (String)field.get(this));
        } else {
          instance.setValue((Attribute) dataSet.attribute(i+1), field.getDouble(this));
        }

      } catch (Exception ex) {}
    }
    return instance;
  }



}
