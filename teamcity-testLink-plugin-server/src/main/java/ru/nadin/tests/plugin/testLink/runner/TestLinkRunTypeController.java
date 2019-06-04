package ru.nadin.tests.plugin.testLink.runner;

import jetbrains.buildServer.controllers.AjaxRequestProcessor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import ru.nadin.tests.plugin.testLink.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestLinkRunTypeController extends BaseController {

   private Logger logger = LoggerFactory.getLogger(TestLinkRunTypeController.class);
   private final WebControllerManager myManager;
   private AdminSettings mainSettings;


   /**
    * @param manager
    */
   public TestLinkRunTypeController(@NotNull AdminSettings mainSettings, final WebControllerManager manager) {
      this.mainSettings = mainSettings;
      this.myManager = manager;
   }

   public AdminSettings getMainSettings() {
      return mainSettings;
   }

   public void setMainSettings(AdminSettings mainSettings) {
      this.mainSettings = mainSettings;
   }

   public void register() {
      myManager.registerController("/saveUserKeys/", this);
   }

   @Override
   protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
      new AjaxRequestProcessor().processRequest(request, response, new AjaxRequestProcessor.RequestHandler() {
         public void handleRequest(@NotNull final HttpServletRequest request, final @NotNull HttpServletResponse response,
                                   @NotNull final Element xmlResponse) {
            try {
               doAction(request, xmlResponse);
            } catch (Exception e) {
               logger.error("Cannot save TestLink configuration", e);
               addResultElement(xmlResponse, "testLinkErrorMessage", "Cannot save TestLink configuration " + getMessageWithNested(e));
            }
         }
      });

      return null;
   }

   private void doAction(final HttpServletRequest request, Element xmlResponse) throws IOException {
      String testLinkUserKey = request.getParameter(Constants.TESTLINK_USER_KEY);
      String testLinkCustomField = request.getParameter(Constants.TESTLINK_CUSTOM_FIELD);
      String testLinkUrl = request.getParameter(Constants.TESTLINK_URL);
      String validationWarnings = getValidationWarnings(testLinkUserKey, testLinkCustomField, testLinkUrl);
      if (!StringUtils.isEmpty(validationWarnings)) {
         addResultElement(xmlResponse, "testLinkWarningMessage", validationWarnings);
      } else {
         mainSettings.setTestLinkUserKey(testLinkUserKey);
         mainSettings.setTestLinkCustomField(testLinkCustomField);
         mainSettings.setTestLinkUrl(testLinkUrl);
         mainSettings.saveProperties();
         addResultElement(xmlResponse, "testLinkSuccessMessage", "Configuration saved successfully!");
      }
   }

   private String getValidationWarnings(String testLinkUserKey, String testLinkCustomField, String testLinkUrl) {
      String result = "";

      if (StringUtils.isEmpty(testLinkUserKey) || StringUtils.isEmpty(testLinkCustomField) || StringUtils.isEmpty(testLinkUrl)) {
         result += "Please, fill all fields with valid data before saving";
      }

//        if (result.isEmpty()) {
//            BzmServerUtils utils = new BzmServerUtils(testLinkUser, testLinkPassword, testLinkUrl);
//            try {
//                User user = User.getUser(utils);
//                assert user.getId() != null;
//            } catch (Exception e) {
//                logger.info("Invalid user credentials or/and server url, please check it: " + e.getMessage(), e);
//                result += "Invalid user credentials or/and server url, please check it ("  + e.getMessage() + ")";
//            }
//        }

      return result;
   }

   public void addResultElement(Element xmlResponse, String resultId, String resultContent) {
      Element resultElement = new Element("result");
      resultElement.setAttribute("id", resultId);
      resultElement.addContent(resultContent);
      xmlResponse.addContent(resultElement);
   }

   static private String getMessageWithNested(Throwable e) {
      String result = e.getMessage();
      Throwable cause = e.getCause();
      if (cause != null) {
         result += " Caused by: " + getMessageWithNested(cause);
      }
      return result;
   }
}
