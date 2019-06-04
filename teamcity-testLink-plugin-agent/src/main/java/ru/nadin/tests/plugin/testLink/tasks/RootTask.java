package ru.nadin.tests.plugin.testLink.tasks;

import jetbrains.buildServer.agent.BuildProgressLogger;
import ru.nadin.tests.plugin.testLink.client.TestLinkClient;

public abstract class RootTask implements TestLinkTask {
   final BuildProgressLogger logger;
   TestLinkClient testLinkClient;

   RootTask(BuildProgressLogger logger,TestLinkClient testLinkClient) {
      this.logger = logger;
      this.testLinkClient = testLinkClient;
   }
}
