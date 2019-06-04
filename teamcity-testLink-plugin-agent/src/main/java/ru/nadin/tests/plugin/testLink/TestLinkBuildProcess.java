package ru.nadin.tests.plugin.testLink;

import jetbrains.buildServer.agent.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.nadin.tests.plugin.testLink.client.TeamCityClient;
import ru.nadin.tests.plugin.testLink.client.TestLinkClient;
import ru.nadin.tests.plugin.testLink.exception.InternalException;
import ru.nadin.tests.plugin.testLink.tasks.CreateBuildTask;
import ru.nadin.tests.plugin.testLink.tasks.ReportResultsTask;
import ru.nadin.tests.plugin.testLink.tasks.RootTask;

import java.net.MalformedURLException;

public class TestLinkBuildProcess implements BuildProcess {
   private final BuildProgressLogger logger;
   private RootTask task;
   private RunType runType;

   TestLinkBuildProcess(AgentRunningBuild agentRunningBuild, BuildRunnerContext buildRunnerContext)
           throws MalformedURLException, InternalException {
      this.logger = agentRunningBuild.getBuildLogger();
      setRunnerParameterValues(buildRunnerContext);
      TestLinkClient testLinkClient = new TestLinkClient(agentRunningBuild.getSharedConfigParameters().get(Constants.TESTLINK_URL),
              agentRunningBuild.getSharedConfigParameters().get(Constants.TESTLINK_USER_KEY), logger);
      if (runType.equals(RunType.REPORT_RESULTS)) {
         task = new ReportResultsTask(buildRunnerContext, logger, testLinkClient, new TeamCityClient(agentRunningBuild),
                 agentRunningBuild.getSharedConfigParameters().get(Constants.TESTLINK_CUSTOM_FIELD));
      } else {
         task = new CreateBuildTask(buildRunnerContext, logger, testLinkClient);
      }
   }

   private void setRunnerParameterValues(BuildRunnerContext buildRunnerContext) {
      String runTypeName = buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_RUNTYPE);
      if (StringUtils.isNotEmpty(runTypeName))
         this.runType = RunType.valueOf(buildRunnerContext.getRunnerParameters().get(Constants.BUILD_STEP_PARAMETER_RUNTYPE));
      else
         this.runType = RunType.REPORT_RESULTS;
   }

   public void start() {
      task.run();
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
