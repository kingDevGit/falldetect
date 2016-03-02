/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oucomp.falldetect.app;

import java.io.File;
import javax.swing.JOptionPane;
import oucomp.falldetect.data.AccelDataset;
import oucomp.falldetect.data.AccelRecord;
import oucomp.falldetect.RecordSelectListener;

public class ViewRecordWithDetectorApp extends javax.swing.JFrame implements RecordSelectListener {

  private AccelDataset theDataset = null;

  public ViewRecordWithDetectorApp() {
    initComponents();
    File datasetFolder = new File("../../data/fall-detection");
    try {
      theDataset = new AccelDataset(datasetFolder);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error in loading dataset", "IO Error", JOptionPane.ERROR_MESSAGE);
    }
    this.recordSelectPanel1.setDataset(theDataset);  
    this.recordSelectPanel1.setRecordSelectListener(this);
  }

  public void recordSelected(String recordClass, String recordLabel) {
    //System.out.println("Selected " + recordClass + " " + recordLabel);
    AccelRecord record = theDataset.getRecord(recordClass, recordLabel);
    this.plotRecordPanel1.setAccelRecord(record);
    this.fallDetectorPanel1.setAccelRecord(record);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    recordSelectPanel1 = new oucomp.falldetect.RecordSelectPanel();
    plotRecordPanel1 = new oucomp.falldetect.PlotRecordPanel();
    fallDetectorPanel1 = new oucomp.falldetect.FallDetectorPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().add(recordSelectPanel1, java.awt.BorderLayout.WEST);
    getContentPane().add(plotRecordPanel1, java.awt.BorderLayout.CENTER);
    getContentPane().add(fallDetectorPanel1, java.awt.BorderLayout.EAST);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(ViewRecordWithDetectorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(ViewRecordWithDetectorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(ViewRecordWithDetectorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(ViewRecordWithDetectorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new ViewRecordWithDetectorApp().setVisible(true);
      }
    });
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private oucomp.falldetect.FallDetectorPanel fallDetectorPanel1;
  private oucomp.falldetect.PlotRecordPanel plotRecordPanel1;
  private oucomp.falldetect.RecordSelectPanel recordSelectPanel1;
  // End of variables declaration//GEN-END:variables
}
