TeamCity plugin to report tests results to TestLink.

Build step runner - TestLink.
Two features:
- Create new build in TestLink during deploying new version of your project;
- Report test results to TestLink.

In TeamCity Administration -> Integrations -> TestLink set the url to your TestLink, DevKey and a custom field name.
You can get devKey on Settings page in TestLink. Custom field must be created in TestLink for Test Cases.
It used to identify your test case with test, that was executed in TeamCity's build.
For example you can add custom field named "ClassName" and fill it with java class names of tests.

For reporting tests results you need to add new parameter to your TeamCity build:
name - env.serverUrl
value - %teamcity.serverUrl%