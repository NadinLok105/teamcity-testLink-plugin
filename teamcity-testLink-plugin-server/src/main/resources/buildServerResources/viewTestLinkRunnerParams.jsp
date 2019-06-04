<%@ page import="ru.nadin.tests.plugin.testLink.RunType" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
        Run type: <strong>
        <% if (propertiesBean.getProperties().get("testLinkPlugin.request.runType")!=null) {%>
            <%= RunType.valueOf(propertiesBean.getProperties().get("testLinkPlugin.request.runType")).getName() %>
        <%}else{%>
            <%= RunType.REPORT_RESULTS.getName() %>
        <%} %>
        </strong>
</div>
<% if (propertiesBean.getProperties().get("testLinkPlugin.request.buildName")!=null) {%>
            <div class="parameter">
                    Build name: <strong><props:displayValue name="testLinkPlugin.request.buildName"/></strong>
                </div>
        <%} %>
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
