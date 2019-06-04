package ru.nadin.tests.plugin.testLink.tasks;

import br.eti.kinoshita.testlinkjavaapi.model.Build;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import ru.nadin.tests.plugin.testLink.Constants;
import ru.nadin.tests.plugin.testLink.client.TestLinkClient;

public class CreateBuildTask extends RootTask {
   private String projectName;
   private String testPlanName;
   private String buildName;

   public CreateBuildTask(BuildRunnerContext buildRunnerContext, BuildProgressLogger logger, TestLinkClient testLinkClient) {
      super(logger, testLinkClient);
      this.projectName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_PROJECT);
      this.testPlanName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_TEST_PLAN);
      this.buildName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_BUILDNAME);
   }

   @Override
   public void run() {
      try {
         TestProject project = testLinkClient.getProject(projectName);
         logger.message("TestLink project name = " + project.getName());
         TestPlan plan = testLinkClient.getProjectTestPlan(project, testPlanName);
         logger.message("TestLink test plan name = " + plan.getName());
         Build newBuild = testLinkClient.createNewBuild(buildName, plan);
         logger.message("New build created = " + newBuild.getName());
      } catch (Exception e) {
         logger.error("Exception during runCreateBuild");
         logger.exception(e);
      }
   }
}
