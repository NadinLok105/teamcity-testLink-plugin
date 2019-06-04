package ru.nadin.tests.plugin.testLink;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import jetbrains.buildServer.agent.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.nadin.tests.plugin.testLink.data.ReportStatus;
import ru.nadin.tests.plugin.testLink.data.TestOccurrence;
import ru.nadin.tests.plugin.testLink.data.TestOccurrences;
import ru.nadin.tests.plugin.testLink.client.TeamCityClient;
import ru.nadin.tests.plugin.testLink.client.TestLinkClient;
import ru.nadin.tests.plugin.testLink.exception.InternalException;

import java.net.MalformedURLException;
import java.util.*;

public class TestLinkBuildProcess implements BuildProcess {
   private final BuildProgressLogger logger;
   private TeamCityClient teamCityClient;
   private TestLinkClient testLinkClient;
   private String customFieldName;
   private String projectName;
   private String testPlanName;
   private String platformName;
   private RunType runType;
   private String buildNumber;

   public TestLinkBuildProcess(AgentRunningBuild agentRunningBuild, BuildRunnerContext buildRunnerContext) throws MalformedURLException, InternalException {
      this.logger = agentRunningBuild.getBuildLogger();
      this.teamCityClient = new TeamCityClient(agentRunningBuild);
      this.testLinkClient = new TestLinkClient(agentRunningBuild.getSharedConfigParameters().get(Constants.TESTLINK_URL),
              agentRunningBuild.getSharedConfigParameters().get(Constants.TESTLINK_USER_KEY), logger);
      this.customFieldName = agentRunningBuild.getSharedConfigParameters().get(Constants.TESTLINK_CUSTOM_FIELD);
      this.buildNumber = agentRunningBuild.getBuildNumber();
      setRunnerParameterValues(buildRunnerContext);
   }

   private void setRunnerParameterValues(BuildRunnerContext buildRunnerContext) {
      String runTypeName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_RUNTYPE);
      if (StringUtils.isNotEmpty(runTypeName))
         this.runType = RunType.valueOf(buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_RUNTYPE));
      else
         this.runType = RunType.REPORT_RESULTS;
      this.projectName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_PROJECT);
      this.testPlanName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_TEST_PLAN);
      this.platformName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_PLATFORM);
   }

   public void start() {
      if (runType.equals(RunType.REPORT_RESULTS))
         runReportResults();
      else
         runCreateBuild();
   }

   private void runReportResults() {
      TestOccurrences results = getTestsResultsFromTeamCity();
      if (results != null)
         forwardToTestLink(results);
   }

   private void runCreateBuild() {
      try {
         TestProject project = testLinkClient.getProject(projectName);
         logger.message("TestLink project name = " + project.getName());
         TestPlan plan = testLinkClient.getProjectTestPlan(project, testPlanName);
         logger.message("TestLink test plan name = " + plan.getName());
         Build newBuild = testLinkClient.createNewBuild(buildNumber, plan);
         logger.message("New build created = " + newBuild.getName());
      } catch (Exception e) {
         logger.error("Exception during runCreateBuild");
         logger.exception(e);
      }
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
      List<ReportStatus> result = new ArrayList();
      for (TestCase tc : TCList) {
         TestCase fullTC = testLinkClient.getTestCaseById(tc.getId());
         CustomField field = testLinkClient.getCustomFieldFromTestCase(fullTC, project, customFieldName);
         String valueToSearch = field.getValue();
         Optional<TestOccurrence> foundTest = toList.stream().filter(testOccurrence -> testOccurrence.getName().contains(valueToSearch)).findFirst();
         if (foundTest.isPresent()) {
            result.add(new ReportStatus(tc.getId(), transformStatusFromTeamCity(foundTest.get().getStatus())));
         }
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

   public boolean isInterrupted() {
      return false;
   }

   public boolean isFinished() {
      return false;
   }

   public void interrupt() {
      logger.message("Interrupt TestLink build step");
   }

   @NotNull
   public BuildFinishedStatus waitFor() {
      return null;
   }
}
