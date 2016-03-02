package oucomp.falldetect;

public class ExperimentResult {

  protected String labelPositive = "Positive";
  protected String labelNegative = "Negative";
  double truePos = 0;
  double falsePos = 0;
  double trueNeg = 0;
  double falseNeg = 0;

  public ExperimentResult() {
  }

  public ExperimentResult(String labelPositive, String labelNegative) {
    this.labelPositive = labelPositive;
    this.labelNegative = labelNegative;
  }

  public void addResults(ExperimentResult results) {
    this.truePos += results.truePos;
    this.trueNeg += results.trueNeg;
    this.falsePos += results.falsePos;
    this.falseNeg += results.falseNeg;
  }

  public void addTruePos(double amount) {
    this.truePos += amount;
  }

  public void addFalsePos(double amount) {
    this.falsePos += amount;
  }

  public void addTrueNeg(double amount) {
    this.trueNeg += amount;
  }

  public void addFalseNeg(double amount) {
    this.falseNeg += amount;
  }

  public void setTruePos(double truePos) {
    this.truePos = truePos;
  }

  public void setFalsePos(double falsePos) {
    this.falsePos = falsePos;
  }

  public void setTrueNeg(double trueNeg) {
    this.trueNeg = trueNeg;
  }

  public void setFalseNeg(double falseNeg) {
    this.falseNeg = falseNeg;
  }

  public double getTruePos() {
    return truePos;
  }

  public double getFalsePos() {
    return falsePos;
  }

  public double getTrueNeg() {
    return trueNeg;
  }

  public double getFalseNeg() {
    return falseNeg;
  }

  public double getTotalActualPersons() {
    return truePos + falseNeg;
  }

  public double getTotalSamples() {
    return truePos + falsePos + trueNeg + falseNeg;
  }

  public double getPrecision() {
    // or Positive predictive value (PPV)
    return truePos / (truePos + falsePos);
  }

  public double getRecall() {
    // or True positive rate (TPR) or Sensitivity
    return truePos / (truePos + falseNeg);
  }

  public double getAccuracy() {
    return (truePos + trueNeg) / (truePos + falsePos + trueNeg + falseNeg);
  }

  public double getF1() {
    return (2 * truePos) / (2 * truePos + falsePos + falseNeg);
  }

  public double getSpecificity() {
    // or True Negative Rate
    return trueNeg / (trueNeg + falsePos);
  }

  public double getSensitivity() {
    // or True positive rate or Recall
    return truePos / (truePos + falseNeg);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[LABELS]\n");
    sb.append("POSITIVE: ").append(labelPositive).append('\n');
    sb.append("NEGATIVE: ").append(labelNegative).append('\n');
    sb.append("[STATISTICS]\n");
    sb.append("TRUE POS: ").append(truePos).append('\n');
    sb.append("TRUE NEG: ").append(trueNeg).append('\n');
    sb.append("FALSE POS: ").append(falsePos).append('\n');
    sb.append("FALSE NEG: ").append(falseNeg).append('\n');
    sb.append("[CONFUSION MATRIX]\n");
    sb.append("\t\tActual\n");
    sb.append("\t\t").append(labelPositive).append("\t\t").append(labelNegative).append("\n");
    sb.append("Predict");
    sb.append("\t").append(labelPositive).append("\t").append(truePos).append("\t\t").append(falsePos).append('\n');
    sb.append("\t").append(labelNegative).append("\t").append(falseNeg).append("\t\t").append(trueNeg).append('\n');
    sb.append("[DERIVATIONS]\n");
    sb.append(String.format("Precision\t: %.4f%%\n", getPrecision() * 100));
    sb.append(String.format("Recall\t\t: %.4f%%\n", getRecall() * 100));
    sb.append(String.format("Accuracy\t: %.4f%%\n", getAccuracy() * 100));
    sb.append(String.format("F1\t\t: %.4f%%\n", getF1() * 100));
    sb.append(String.format("Specificity\t: %.4f%%\n", getSpecificity() * 100));
    sb.append(String.format("Sensitivity\t: %.4f%%\n", getSensitivity() * 100));
    return sb.toString();
  }
}
