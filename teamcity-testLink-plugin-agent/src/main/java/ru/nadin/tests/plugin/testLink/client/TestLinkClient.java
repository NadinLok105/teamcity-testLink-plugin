package ru.nadin.tests.plugin.testLink.client;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import jetbrains.buildServer.agent.BuildProgressLogger;
import org.apache.commons.lang.StringUtils;
import ru.nadin.tests.plugin.testLink.data.ReportStatus;
import ru.nadin.tests.plugin.testLink.exception.InternalException;
import ru.nadin.tests.plugin.testLink.Constants;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestLinkClient {
   private TestLinkAPI client;
   private final BuildProgressLogger logger;

   public TestLinkClient(String url, String devKey, BuildProgressLogger logger) throws MalformedURLException, InternalException {
      this.logger = logger;
      if (checkIfNullAdminSettings(url, Constants.TESTLINK_URL) || checkIfNullAdminSettings(devKey, Constants.TESTLINK_USER_KEY))
         throw new InternalException("TestLink main settings are null!!!");
      url = url + Constants.TESTLINK_URL_END;
      URL testlinkURL = new URL(url);
      client = new TestLinkAPI(testlinkURL, devKey);
   }

   private boolean checkIfNullAdminSettings(Object param, String name) {
      if (param == null || param.toString().isEmpty()) {
         logger.error("TestLink administrative settings are empty!!! Field = " + name);
         return true;
      }
      return false;
   }

   public TestProject getProject(String projectName) {
      TestProject[] projects = client.getProjects();
      if (StringUtils.isNotEmpty(projectName)) {
         for (TestProject p : projects) {
            if (p.getName().contains(projectName))
               return p;
         }
      }
      return projects[0];
   }

   public TestPlan getProjectTestPlan(TestProject project, String testPlanName) {
      TestPlan[] plans = client.getProjectTestPlans(project.getId());
      List<TestPlan> activePlans = new ArrayList();
      for (TestPlan p : plans) {
         if (p.isActive())
            activePlans.add(p);
      }
      if (StringUtils.isNotEmpty(testPlanName)) {
         Optional<TestPlan> foundPlan = activePlans.stream().filter(plan -> plan.getName().contains(testPlanName)).findFirst();
         if (foundPlan.isPresent())
            return foundPlan.get();
      }
      return activePlans.get(0);
   }

   public Platform getPlatform(TestPlan plan, String platformName) {
      Platform[] platforms = client.getTestPlanPlatforms(plan.getId());
      if (StringUtils.isNotEmpty(platformName)) {
         for (Platform p : platforms) {
            if (p.getName().contains(platformName))
               return p;
         }
      }
      return platforms[0];
   }

   public Build getLatestBuild(TestPlan plan) {
      Build build = client.getLatestBuildForTestPlan(plan.getId());
      return build;
   }

   public TestCase[] getAutomatedTestCasesFromTestPlan(TestPlan plan, Build build) {
      TestCase[] cases = client.getTestCasesForTestPlan(plan.getId(), null, build.getId(),
              null, null, null, null,
              null, ExecutionType.AUTOMATED, null, null);
      return cases;
   }

   public TestCase getTestCaseById(Integer id) {
      return client.getTestCase(id, null, null);
   }

   public CustomField getCustomFieldFromTestCase(TestCase testCase, TestProject project, String fieldName) {
      return client.getTestCaseCustomFieldDesignValue(testCase.getId(), null, testCase.getVersion(),
              project.getId(), fieldName, null);
   }

   public void sendResultsToTestLink(List<ReportStatus> reportStatuses, TestPlan plan, Build build, Platform platform) {
      for (ReportStatus rs : reportStatuses) {
         client.reportTCResult(rs.getTestCaseId(), null, plan.getId(), rs.getStatus(),
                 build.getId(), null, null, null, null, platform.getId(),
                 null, null, false);
      }
   }

   public Build createNewBuild(String name, TestPlan plan) {
      return client.createBuild(plan.getId(), name, "Created from TeamCity runner");
   }


}
