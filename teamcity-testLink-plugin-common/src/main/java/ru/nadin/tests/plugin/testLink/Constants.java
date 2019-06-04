package ru.nadin.tests.plugin.testLink;

public interface Constants {
   String RUNNER_DISPLAY_NAME = "TestLink";
   String RUNNER_TYPE = "TestLink";
   String TESTLINK_USER_KEY = "testLinkUserKey";
   String TESTLINK_CUSTOM_FIELD = "testLinkCustomField";
   String TESTLINK_URL = "testLinkUrl";
   String PROPERTIES_FILE = "/testLinkUser.properties";

   String TESTLINK_URL_END = "/lib/api/xmlrpc/v1/xmlrpc.php";
   String TEAMCITY_URL_PARAMETER_NAME = "serverUrl";
   String TEAMCITY_USERID_PARAMETER_NAME = "teamcity.auth.userId";
   String TEAMCITY_PASSWORD_PARAMETER_NAME = "teamcity.auth.password";
   String TEAMCITY_REST_API_TESTOCCURANCES = "/httpAuth/app/rest/testOccurrences?locator=build:";

   String BUILD_STEP_PARAMETER_PROJECT = "testLinkPlugin.request.projectName";
   String BUILD_STEP_PARAMETER_TEST_PLAN = "testLinkPlugin.request.testPlanName";
   String BUILD_STEP_PARAMETER_PLATFORM = "testLinkPlugin.request.platformName";
   String BUILD_STEP_PARAMETER_RUNTYPE = "testLinkPlugin.request.runType";
   String BUILD_STEP_PARAMETER_BUILDNAME = "testLinkPlugin.request.buildName";
}
