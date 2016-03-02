package oucomp.falldetect;

public class EvaluationResult {
  private String recordClass = "UNKNOWN";
  private StringBuilder evaluationNotes = new StringBuilder();

  public String getRecordClass() {
    return recordClass;
  }

  public void setRecordClass(String recordClass) {
    this.recordClass = recordClass;
  }

  public String getEvaluationNotes() {
    return evaluationNotes.toString();
  }

  public EvaluationResult appendNotes(String s) {
    this.evaluationNotes.append(s);
    return this;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[EVALUATION]\n");
    sb.append("CLASSIFICATION RESULT: " + this.recordClass).append('\n');
    sb.append("EVALUATION NOTES:\n");
    sb.append(this.evaluationNotes);
    return sb.toString();
  }
}
