package ru.nadin.tests.plugin.testLink.runner;

import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nadin.tests.plugin.testLink.Constants;

import java.util.HashMap;
import java.util.Map;

public class TestLinkRunType extends RunType {

   @NotNull
   private final PluginDescriptor pluginDescriptor;
   @NotNull
   private final String editParamsPageName = "editTestLinkRunnerParams.jsp";
   @NotNull
   private final String viewParamsPageName = "viewTestLinkRunnerParams.jsp";

   Map<String, String> defaultProperties = null;

   private AdminSettings pluginSettings;

   public TestLinkRunType(final RunTypeRegistry runTypeRegistry,
                          @NotNull final PluginDescriptor pluginDescriptor,
                          @NotNull final AdminSettings pluginSettings) {
      this.pluginDescriptor = pluginDescriptor;
      this.pluginSettings = pluginSettings;

      runTypeRegistry.registerRunType(this);
   }

   @Override
   public String getDescription() {
      return Constants.RUNNER_DISPLAY_NAME;
   }

   @Override
   public String getDisplayName() {
      return Constants.RUNNER_DISPLAY_NAME;
   }

   @Override
   public String getType() {
      return Constants.RUNNER_TYPE;
   }

   @Nullable
   public Map<String, String> getDefaultRunnerProperties() {
      if (defaultProperties == null) {
         defaultProperties = new HashMap();
      }
      setupDefaultProperties(defaultProperties);
      return defaultProperties;
   }

   private void setupDefaultProperties(Map<String, String> params) {

      if (pluginSettings != null) {
         params.remove(Constants.TESTLINK_URL);
         params.remove(Constants.TESTLINK_CUSTOM_FIELD);
         params.remove(Constants.TESTLINK_USER_KEY);
         params.put(Constants.TESTLINK_USER_KEY, pluginSettings.getTestLinkUserKey());
         params.put(Constants.TESTLINK_CUSTOM_FIELD, pluginSettings.getTestLinkCustomField());
         params.put(Constants.TESTLINK_URL, pluginSettings.getTestLinkUrl());
      }
   }

   @Nullable
   public PropertiesProcessor getRunnerPropertiesProcessor() {
      return new TestLinkRunTypePropertiesProcessor();
   }

   @Nullable
   public String getEditRunnerParamsJspFilePath() {
      return pluginDescriptor.getPluginResourcesPath(editParamsPageName);
   }

   @Override
   public String getViewRunnerParamsJspFilePath() {
      return pluginDescriptor.getPluginResourcesPath(viewParamsPageName);
   }

   public AdminSettings getPluginSettings() {
      return pluginSettings;
   }

   public void setPluginSettings(AdminSettings pluginSettings) {
      this.pluginSettings = pluginSettings;
   }

}
