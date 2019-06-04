package ru.nadin.tests.plugin.testLink.runner;

import jetbrains.buildServer.controllers.admin.AdminPage;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import ru.nadin.tests.plugin.testLink.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ConfPage extends AdminPage {
   @NotNull
   private final PagePlaces pagePlaces;
   @NotNull
   private final PluginDescriptor descriptor;
   @NotNull
   private AdminSettings mainSettings;

   public ConfPage(@NotNull PagePlaces pagePlaces, @NotNull PluginDescriptor descriptor, String includeUrl) {
      super(pagePlaces);
      this.pagePlaces = pagePlaces;
      this.descriptor = descriptor;

      setPluginName(Constants.RUNNER_DISPLAY_NAME);
      setTabTitle(Constants.RUNNER_DISPLAY_NAME);
      setIncludeUrl(descriptor.getPluginResourcesPath(includeUrl));
      register();
   }

   public AdminSettings getMainSettings() {
      return mainSettings;
   }

   public void setMainSettings(AdminSettings mainSettings) {
      this.mainSettings = mainSettings;
   }

   public String getGroup() {
      return Constants.RUNNER_TYPE;
   }

   @Override
   public boolean isVisible() {
      return true;
   }

   @Override
   public void fillModel(Map<String, Object> model, HttpServletRequest request) {
      super.fillModel(model, request);
      if (mainSettings != null) {
         model.put(Constants.TESTLINK_USER_KEY, mainSettings.getTestLinkUserKey());
         model.put(Constants.TESTLINK_CUSTOM_FIELD, mainSettings.getTestLinkCustomField());
         model.put(Constants.TESTLINK_URL, mainSettings.getTestLinkUrl());
      }
   }

   @Override
   public String getPluginName() {
      return Constants.RUNNER_TYPE;
   }

}
