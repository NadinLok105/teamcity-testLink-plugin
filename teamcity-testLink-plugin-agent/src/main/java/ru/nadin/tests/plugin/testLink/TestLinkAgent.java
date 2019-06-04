package ru.nadin.tests.plugin.testLink;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.NotNull;

public class TestLinkAgent implements AgentBuildRunner {

   private AgentBuildRunnerInfo runnerInfo;
   private BuildProcess buildProcess;

   @NotNull
   public BuildProcess createBuildProcess(@NotNull AgentRunningBuild agentRunningBuild, @NotNull BuildRunnerContext buildRunnerContext) throws RunBuildException {
      try {
         buildProcess = new TestLinkBuildProcess(agentRunningBuild, buildRunnerContext);
      } catch (Exception e) {
         throw new RunBuildException("Failed to create TestLink build", e);
      }
      return buildProcess;
   }

   @NotNull
   public AgentBuildRunnerInfo getRunnerInfo() {
      runnerInfo = new AgentBuildRunnerInfo() {
         @NotNull
         public String getType() {
            return "TestLink";
         }

         public boolean canRun(@NotNull BuildAgentConfiguration buildAgentConfig) {
            return true;
         }
      };

      return runnerInfo;
   }
}
