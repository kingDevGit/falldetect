package oucomp.indoorpos.exp.longroute;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import oucomp.indoorpos.DataPeakAnalysis;
import oucomp.indoorpos.RawDataBuffer;
import oucomp.indoorpos.RawDataReader;
import oucomp.indoorpos.SpectralAnalysis;
import oucomp.indoorpos.exp.BasicFeatureSetSpectral;
import oucomp.indoorpos.exp.FeatureSetHelper;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class LongRouteTestA {

  public static void main(String args[]) throws Exception {
    // create the model again, should be consistent with the classifier below
    String eventArray[] = new String[]{"up", "down", "standstill", "stairup", "stairdown", "walking", "pull", "push"};
    Instances dataModel = FeatureSetHelper.createDataModel("IndoorPos", eventArray, BasicFeatureSetSpectral.class);
    dataModel.setClassIndex(0);
    // read serialized object of classifier
    ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream("../../data/indoorpos/j48.model"));
    Classifier classifer = (Classifier) ois.readObject();
    ois.close();
    // read long route data
    File sampleFile = new File("../../data/indoorpos/LongRoute/LongRoute2.csv");
    RawDataBuffer buffer = new RawDataBuffer();

    RawDataReader.read(sampleFile, buffer);
    System.out.println("Number of samples : " + buffer.getSampleCount());
    System.out.println("Sampling rate = " + buffer.getSampleRate() + " Hz");
    double rawDataSampleRate = buffer.getSampleRate();
    // go through the data
    // take sample of 6 seconds every 0.5 second
    double eventDuration = 5.0;
    double eventStep = 1.0;
    double eventStartTime = 0.0;
    int eventDurationSampleCount = (int) (eventDuration * rawDataSampleRate);
    double rms[] = new double[eventDurationSampleCount];
    while (true) {
      int eventStartSampleID = (int) (eventStartTime * rawDataSampleRate);
      if (!buffer.getSample(eventStartSampleID, eventDurationSampleCount, rms)) {
        break;
      }
      BasicFeatureSetSpectral fs = new BasicFeatureSetSpectral("Test");
      DataPeakAnalysis da = new DataPeakAnalysis(rms);
      fs.evaluateBasicData(da.getMean(), da.getVariance(), da.getSkewness(), da.getMax(), da.getMin());
      fs.evaluatePeakFeatures(da);

      SpectralAnalysis sa = new SpectralAnalysis(rms, rms.length, rawDataSampleRate);
      fs.evaluateSpectral(sa);
      Instance theInstance = FeatureSetHelper.createTestInstance(dataModel, fs);
      dataModel.add(theInstance);
      double result = classifer.classifyInstance(theInstance);
      System.out.println(String.format("Time %fs: %s", eventStartTime, eventArray[(int)result]));
      // update next time
      eventStartTime += eventStep;
    }
  }
}
