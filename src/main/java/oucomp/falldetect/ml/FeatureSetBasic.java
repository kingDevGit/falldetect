package oucomp.falldetect.ml;

import java.util.ArrayList;
import java.util.List;
import oucomp.falldetect.data.AccelRecord;
import oucomp.falldetect.data.ImpactPeak;
import oucomp.falldetect.data.ImpactPeakFinder;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class FeatureSetBasic {

  protected static Instances dataSet = null;

  protected String theClass = "unknown";
  protected int peakCount = 0;
  protected double AAMV = 0; // in g
  protected double IDI = 0; // Impact Duration Index (samples)
  protected double MPI = 0; // Maximum Peak Index (g)
  protected double MVI = 0; // Minimal Valley Index (g)
  protected double PDI = 0; // Peak Duration Index (samples)

  static {
    FastVector atts = new FastVector();
    FastVector catAttribute = new FastVector(); // define the class attribute
    catAttribute.addElement("fall");
    catAttribute.addElement("nofall");
    atts.addElement(new Attribute("class", catAttribute));
    atts.addElement(new Attribute("peakcount"));
    atts.addElement(new Attribute("aamv"));
    atts.addElement(new Attribute("idi"));
    atts.addElement(new Attribute("mpi"));
    atts.addElement(new Attribute("mvi"));
    atts.addElement(new Attribute("pdi"));
    dataSet = new Instances("FallDetect", atts, 0);
    dataSet.setClassIndex(0);
  }

  private static Instances getDataSetInstances() {
    return dataSet;
  }

  public static Instances convertToInstances(List<FeatureSetBasic> featureSetList) {
    Instances instances = getDataSetInstances();
    for (FeatureSetBasic fs : featureSetList) {
      instances.add(fs.getInstance());
    }
    return instances;
  }

  public Instance getInstance() {
    Instance instance = new Instance(7);
    instance.setDataset(dataSet);
    instance.setValue((Attribute) dataSet.attribute(0), theClass);
    instance.setValue((Attribute) dataSet.attribute(1), peakCount);
    instance.setValue((Attribute) dataSet.attribute(2), AAMV);
    instance.setValue((Attribute) dataSet.attribute(3), IDI);
    instance.setValue((Attribute) dataSet.attribute(4), MPI);
    instance.setValue((Attribute) dataSet.attribute(5), MVI);
    instance.setValue((Attribute) dataSet.attribute(6), PDI);
    return instance;
  }

  public static String getClassLabel(int classifiedValue) {
    Attribute catAttr = dataSet.attribute(0);
    return catAttr.value(classifiedValue);
  }

  public static List<FeatureSetBasic> buildFeatureSet(List<AccelRecord> recordList) {
    List<FeatureSetBasic> fsList = new ArrayList();
    for (AccelRecord record : recordList) {
      fsList.add(buildFeatureSet(record));
    }
    return fsList;
  }

  public String getTheClass() {
    return theClass;
  }

  public void setTheClass(String theClass) {
    this.theClass = theClass;
  }

  public int getPeakCount() {
    return peakCount;
  }

  public void setPeakCount(int peakCount) {
    this.peakCount = peakCount;
  }

  public double getAAMV() {
    return AAMV;
  }

  public void setAAMV(double AAMV) {
    this.AAMV = AAMV;
  }

  public double getIDI() {
    return IDI;
  }

  public void setIDI(double IDI) {
    this.IDI = IDI;
  }

  public double getMPI() {
    return MPI;
  }

  public void setMPI(double MPI) {
    this.MPI = MPI;
  }

  public double getMVI() {
    return MVI;
  }

  public void setMVI(double MVI) {
    this.MVI = MVI;
  }

  public double getPDI() {
    return PDI;
  }

  public void setPDI(double PDI) {
    this.PDI = PDI;
  }

  public static FeatureSetBasic buildFeatureSet(AccelRecord record) {
    FeatureSetBasic fs = new FeatureSetBasic();
    fs.setTheClass(record.getRecordClass());
    List<ImpactPeak> impactPeakList = ImpactPeakFinder.extractImpactPeakList(record.getRMS(), null);
    fs.setPeakCount(impactPeakList.size());
    if (impactPeakList.isEmpty()) {
      return fs;
    }
    // find highest peak
    ImpactPeak highestPeak = impactPeakList.get(0);
    for (ImpactPeak p : impactPeakList) {
      if (p.getPeakValue() > highestPeak.getPeakValue()) {
        highestPeak = p;
      }
    }
    // calculate AAMV and others
    fs.setAAMV(findAAMV(record, highestPeak));
    fs.setIDI(findIDI(record, highestPeak));
    fs.setPDI(findPDI(record, highestPeak));
    fs.setMPI(findMPI(record, highestPeak));
    fs.setMVI(findMVI(record, highestPeak));
    return fs;
  }

  protected static double findAAMV(AccelRecord record, ImpactPeak p) {
    int count = 0;
    double sum = 0;
    double rms[] = record.getRMS();
    for (int i = p.getImpactStartIndex(); i <= p.getImpactEndIndex() && i < rms.length - 1; i++) {
      sum += Math.abs(rms[i + 1] - rms[i]);
      count++;
    }
    return sum / count;
  }

  protected static double findIDI(AccelRecord record, ImpactPeak p) {
    return p.getImpactEndIndex() - p.getImpactStartIndex();
  }

  protected static double findPDI(AccelRecord record, ImpactPeak p) {
    return p.getPeakEndIndex() - p.getPeakStartIndex();
  }

  protected static double findMPI(AccelRecord record, ImpactPeak p) {
    return p.getPeakValue();
  }

  protected static double findMVI(AccelRecord record, ImpactPeak p) {
    double rms[] = record.getRMS();
    int startIndex = Math.max(0, p.getImpactStartIndex() - 50);
    int endIndex = Math.min(rms.length, p.getImpactEndIndex());
    double minValue = 0;
    for (int i = startIndex; i < endIndex; i++) {
      if (i == startIndex || rms[i] < minValue) {
        minValue = rms[i];
      }
    }
    return minValue;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Class\t: %s\n", theClass));
    sb.append(String.format("Peak Count\t: %d\n", peakCount));
    sb.append(String.format("AAMV (g)\t: %f\n", AAMV));
    sb.append(String.format("IDI (samples)\t: %f\n", IDI));
    sb.append(String.format("PDI\t: %f\n", PDI));
    sb.append(String.format("MPI (g)\t: %f\n", MPI));
    sb.append(String.format("MVI (g)\t: %f\n", MVI));
    return sb.toString();
  }

}
