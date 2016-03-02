package oucomp.falldetect;

import java.awt.Dimension;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotRecordPanelWithVelocity extends PlotRecordPanel {

  protected XYPlot velPlot;
  protected XYSeriesCollection velSeriesCollection;

  protected void createChart() {
    super.createChart();
    // create velocity subplot
    velSeriesCollection = new XYSeriesCollection();
    velPlot = new XYPlot(velSeriesCollection, null, new NumberAxis("Velocity"), new StandardXYItemRenderer());
    velPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
    thePlot.add(velPlot, 1);
    chartPanel.setPreferredSize(new Dimension(360, 320));
  }

  protected void updateChart() {
    if (record == null || record.getSamples() == null) {
      return;
    }
    super.updateChart();
    double samplePeriod = record.samplePeriod();
    // update velocity
    double vel[] = record.getVelocity();
    velSeriesCollection.removeAllSeries();
    XYSeries series = new XYSeries("Velocity");
    for (int j = 0; j < vel.length; j++) {
      series.add(j * samplePeriod, vel[j]);
    }
    velSeriesCollection.addSeries(series);
  }
}
