package ru.nadin.tests.plugin.testLink.data;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;

public class ReportStatus {
   private Integer testCaseId;
   private ExecutionStatus status;

   public ReportStatus(Integer testCaseId, ExecutionStatus status) {
      this.setStatus(status);
      this.testCaseId = testCaseId;
   }

   public Integer getTestCaseId() {
      return testCaseId;
   }

   public void setTestCaseId(Integer testCaseId) {
      this.testCaseId = testCaseId;
   }

   public ExecutionStatus getStatus() {
      return status;
   }

   public void setStatus(ExecutionStatus status) {
      this.status = status;
   }
}
