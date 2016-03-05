/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oucomp.indoorpos.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import oucomp.indoorpos.AccelDatasetModel;
import oucomp.indoorpos.AccelRecord;
import oucomp.indoorpos.SpectralAnalysis;
import oucomp.indoorpos.DataPeakAnalysis;

public class DataViewer extends javax.swing.JFrame {

  private AccelDatasetModel model = null;
  private String recordClass = null;

  public DataViewer() {
    initComponents();
    try {
      model = new AccelDatasetModel(new File("../../data/indoorpos"));
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(this, "IOException", ex.toString(), JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }
    DefaultListModel model = new DefaultListModel();
    jList1.setModel(model);
    updateClassSelectCombo();
  }

  private void updateClassSelectCombo() {
    jComboBox1.removeAllItems();
    List<String> list = model.getRecordClassList();
    for (String recordClass : list) {
      jComboBox1.addItem(recordClass);
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jPanel1 = new javax.swing.JPanel();
    jComboBox1 = new javax.swing.JComboBox();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList1 = new javax.swing.JList();
    jPanel2 = new javax.swing.JPanel();
    plotRecordPanel1 = new oucomp.indoorpos.gui.PlotRecordPanel();
    spectrumPanel1 = new oucomp.indoorpos.gui.SpectrumPanel();
    jPanel3 = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new java.awt.Dimension(1200, 600));
    setPreferredSize(new java.awt.Dimension(800, 600));
    getContentPane().setLayout(new java.awt.GridBagLayout());

    jPanel1.setMinimumSize(new java.awt.Dimension(280, 400));
    jPanel1.setPreferredSize(new java.awt.Dimension(280, 480));
    jPanel1.setLayout(new java.awt.GridBagLayout());

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    jComboBox1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jComboBox1ActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(jComboBox1, gridBagConstraints);

    jList1.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
    jList1.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        jList1ValueChanged(evt);
      }
    });
    jScrollPane1.setViewportView(jList1);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    jPanel1.add(jScrollPane1, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(jPanel1, gridBagConstraints);

    jPanel2.setLayout(new java.awt.BorderLayout());
    jPanel2.add(plotRecordPanel1, java.awt.BorderLayout.CENTER);

    spectrumPanel1.setPreferredSize(new java.awt.Dimension(640, 240));
    jPanel2.add(spectrumPanel1, java.awt.BorderLayout.SOUTH);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(jPanel2, gridBagConstraints);

    jPanel3.setPreferredSize(new java.awt.Dimension(200, 100));
    jPanel3.setSize(new java.awt.Dimension(200, 100));
    jPanel3.setLayout(new java.awt.GridLayout(1, 0));

    jScrollPane2.setMinimumSize(new java.awt.Dimension(200, 300));

    jTextArea1.setColumns(20);
    jTextArea1.setRows(5);
    jScrollPane2.setViewportView(jTextArea1);

    jPanel3.add(jScrollPane2);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(jPanel3, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    JComboBox cb = (JComboBox) evt.getSource();
    recordClass = (String) cb.getSelectedItem();
    // populate the list
    List<AccelRecord> recordList = model.getAccelRecordList(recordClass);
    if (recordClass == null) {
      return;
    }
    // get the model object from the list and clear it first
    DefaultListModel model = (DefaultListModel) jList1.getModel();
    model.removeAllElements();

    for (AccelRecord record : recordList) {
      model.addElement(record.getRecordLabel());
    }
  }//GEN-LAST:event_jComboBox1ActionPerformed

  private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
    if (evt.getValueIsAdjusting()) {
      return;
    }
    int index = jList1.getSelectedIndex();
    ListModel listmodel = jList1.getModel();
    if (index < 0 || index >= listmodel.getSize()) {
      return;
    }
    //listener.recordSelected((String)jComboBox1.getSelectedItem(), (String)model.getElementAt(index));
    List<AccelRecord> list = model.getAccelRecordList(recordClass);
    AccelRecord record = list.get(index);
    plotRecordPanel1.setAccelRecord(record);
    // update spectral panel
    SpectralAnalysis spectralData;
    if (!record.containExtra("SPECTRAL")) {
      spectralData = new SpectralAnalysis(record.getRMSArray(), record.getSampleCount(), record.getSampleRate());
      record.putExtra("SEPCTRAL", spectralData);
    } else {
      spectralData = (SpectralAnalysis) record.getExtra("SPECTRAL");
    }
    double freqSpectrum[][] = spectralData.getSpectrum();
    spectrumPanel1.setData(freqSpectrum, spectralData.getMeanArray());
    // update data analysis panel
    DataPeakAnalysis stat;
    if (!record.containExtra("SIMPLEDA")) { 
      stat = new DataPeakAnalysis(record.getRMSArray());
      record.putExtra("SIMPLEDA", stat);
    } else {
      stat = (DataPeakAnalysis) record.getExtra("SIMPLEDA");
    }
    jTextArea1.setText(stat.toString());
  }//GEN-LAST:event_jList1ValueChanged

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
      java.util.logging.Logger.getLogger(DataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(DataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(DataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(DataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new DataViewer().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox jComboBox1;
  private javax.swing.JList jList1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JTextArea jTextArea1;
  private oucomp.indoorpos.gui.PlotRecordPanel plotRecordPanel1;
  private oucomp.indoorpos.gui.SpectrumPanel spectrumPanel1;
  // End of variables declaration//GEN-END:variables
}
