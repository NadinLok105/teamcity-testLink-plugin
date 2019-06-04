<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
        Run type: <strong>
        <% if (propertiesBean.getProperties().get("testLinkPlugin.request.runType")!=null) {%>
            <%= ru.vetrf.tests.plugin.testLink.RunType.valueOf(propertiesBean.getProperties().get("testLinkPlugin.request.runType")).getName() %>
        <%}else{%>
            <%= ru.vetrf.tests.plugin.testLink.RunType.REPORT_RESULTS.getName() %>
        <%} %>
        </strong>
</div>

<% if (propertiesBean.getProperties().get("testLinkPlugin.request.projectName")!=null) {%>
            <div class="parameter">
                    Project name: <strong><props:displayValue name="testLinkPlugin.request.projectName"/></strong>
                </div>
        <%} %>
<% if (propertiesBean.getProperties().get("testLinkPlugin.request.testPlanName")!=null) {%>
            <div class="parameter">
                    Project name: <strong><props:displayValue name="testLinkPlugin.request.testPlanName"/></strong>
                </div>
        <%} %>
<% if (propertiesBean.getProperties().get("testLinkPlugin.request.platformName")!=null) {%>
            <div class="parameter">
                    Project name: <strong><props:displayValue name="testLinkPlugin.request.platformName"/></strong>
                </div>
        <%} %>
