package ru.nadin.tests.plugin.testLink.tasks;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import ru.nadin.tests.plugin.testLink.Constants;
import ru.nadin.tests.plugin.testLink.client.TeamCityClient;
import ru.nadin.tests.plugin.testLink.client.TestLinkClient;
import ru.nadin.tests.plugin.testLink.data.ReportStatus;
import ru.nadin.tests.plugin.testLink.data.TestOccurrence;
import ru.nadin.tests.plugin.testLink.data.TestOccurrences;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportResultsTask extends RootTask {
   private TeamCityClient teamCityClient;
   private String projectName;
   private String testPlanName;
   private String platformName;
   private String customFieldName;

   public ReportResultsTask(BuildRunnerContext buildRunnerContext, BuildProgressLogger logger,
                            TestLinkClient testLinkClient, TeamCityClient teamCityClient, String customFieldName) {
      super(logger, testLinkClient);
      this.teamCityClient = teamCityClient;
      this.projectName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_PROJECT);
      this.testPlanName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_TEST_PLAN);
      this.platformName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_PLATFORM);
      this.customFieldName = customFieldName;
   }

   @Override
   public void run() {
      TestOccurrences results = getTestsResultsFromTeamCity();
      if (results != null)
         forwardToTestLink(results);
   }

   private void forwardToTestLink(TestOccurrences allResults) {
      try {
         TestProject project = testLinkClient.getProject(projectName);
         logger.message("TestLink project name = " + project.getName());
         TestPlan plan = testLinkClient.getProjectTestPlan(project, testPlanName);
         logger.message("TestLink test plan name = " + plan.getName());
         Platform platform = testLinkClient.getPlatform(plan, platformName);
         logger.message("TestLink platform name = " + platform.getName());
         Build build = testLinkClient.getLatestBuild(plan);
         logger.message("TestLink build name = " + build.getName());

         TestCase[] cases = testLinkClient.getAutomatedTestCasesFromTestPlan(plan, build);
         List<ReportStatus> reportStatuses = transformTestCases(cases, allResults.getTestOccurrences(), project);
         testLinkClient.sendResultsToTestLink(reportStatuses, plan, build, platform);
         logger.message("Results are reported! Total cases = " + reportStatuses.size());
      } catch (Exception e) {
         logger.exception(e);
      }
   }

   private List<ReportStatus> transformTestCases(TestCase[] TCList, List<TestOccurrence> toList, TestProject project) {
      List<ReportStatus> result = new ArrayList<>();
      for (TestCase tc : TCList) {
         TestCase fullTC = testLinkClient.getTestCaseById(tc.getId());
         CustomField field = testLinkClient.getCustomFieldFromTestCase(fullTC, project, customFieldName);
         String valueToSearch = field.getValue();
         Optional<TestOccurrence> foundTest = toList.stream().filter(testOccurrence -> testOccurrence.getName().contains(valueToSearch)).findFirst();
         foundTest.ifPresent(testOccurrence -> result.add(new ReportStatus(tc.getId(), transformStatusFromTeamCity(testOccurrence.getStatus()))));
      }
      return result;
   }

   private ExecutionStatus transformStatusFromTeamCity(String statusTeamCity) {
      if (statusTeamCity.equalsIgnoreCase("SUCCESS"))
         return ExecutionStatus.PASSED;
      return ExecutionStatus.FAILED;
   }

   private TestOccurrences getTestsResultsFromTeamCity() {
      TestOccurrences allTests = teamCityClient.getTestsResults();
      if (allTests != null && allTests.getTestOccurrences() != null) {
         logger.message("Test occurrences = " + allTests.getTestOccurrences().size());
      } else {
         logger.message("No test occurrences in TeamCity!");
         return null;
      }
      return allTests;
   }
}
