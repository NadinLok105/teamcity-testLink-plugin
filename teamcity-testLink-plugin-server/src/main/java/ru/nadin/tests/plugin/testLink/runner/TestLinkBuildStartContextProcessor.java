package ru.nadin.tests.plugin.testLink.runner;

import jetbrains.buildServer.ExtensionHolder;
import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nadin.tests.plugin.testLink.Constants;

public class TestLinkBuildStartContextProcessor implements BuildStartContextProcessor {
   private Logger logger = LoggerFactory.getLogger(TestLinkBuildStartContextProcessor.class);

   private AdminSettings pluginSettings;
   private ExtensionHolder extensionHolder;

   public TestLinkBuildStartContextProcessor(@NotNull final AdminSettings pluginSettings, @NotNull ExtensionHolder extensionHolder) {
      this.pluginSettings = pluginSettings;
      this.extensionHolder = extensionHolder;
   }

   public void updateParameters(@NotNull BuildStartContext buildStartContext) {
      logger.info("Add Shared Parameter user creds to build context");
      buildStartContext.addSharedParameter(Constants.TESTLINK_USER_KEY, validateValue(pluginSettings.getTestLinkUserKey()));
      buildStartContext.addSharedParameter(Constants.TESTLINK_CUSTOM_FIELD, validateValue(pluginSettings.getTestLinkCustomField()));
      buildStartContext.addSharedParameter(Constants.TESTLINK_URL, validateValue(pluginSettings.getTestLinkUrl()));
   }

   private String validateValue(String value) {
      if (value == null) {
         logger.warn("User credential contains NULL value, check your credentials in 'Administration -> TestLink' page");
         return "";
      }
      return value;
   }

   public void register() {
      extensionHolder.registerExtension(BuildStartContextProcessor.class, this.getClass().getName(), this);
   }
}
