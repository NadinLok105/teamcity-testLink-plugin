<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<l:settingsGroup title="TestLink">
<tr>
        <th><label>Run type:</label></th>
        <td>
            <props:selectProperty name="testLinkPlugin.request.runType">
                <props:option value="REPORT_RESULTS" selected="true">Report results</props:option>
                <props:option value="CREATE_BUILD">Create build</props:option>
            </props:selectProperty>
        </td>
    </tr>
<%--Advanced options start --%>
<tr class="advancedSetting advanced_hidden">
    <th><label>Build name:</label></th>
    <td>
        <props:textProperty name="testLinkPlugin.request.buildName"/>
    </td>
</tr>
<tr class="advancedSetting advanced_hidden">
    <th><label>Project name:</label></th>
    <td>
        <props:textProperty name="testLinkPlugin.request.projectName"/>
    </td>
</tr>
<tr class="advancedSetting advanced_hidden">
    <th><label>Test plan name:</label></th>
    <td>
        <props:textProperty name="testLinkPlugin.request.testPlanName"/>
    </td>
</tr>
<tr class="advancedSetting advanced_hidden">
    <th><label>Platform name:</label></th>
    <td>
        <props:textProperty name="testLinkPlugin.request.platformName"/>
    </td>
</tr>

<%--Advanced options end --%>
</l:settingsGroup>