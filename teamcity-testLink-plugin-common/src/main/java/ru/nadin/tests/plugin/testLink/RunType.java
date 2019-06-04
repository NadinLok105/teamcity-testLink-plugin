package ru.nadin.tests.plugin.testLink;

public enum RunType {
   REPORT_RESULTS("Report results"), CREATE_BUILD("Create build");

   private String name;

   RunType(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }
}
