<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ include file="/include.jsp" %>
<script type="text/javascript">
    function sendReqSave() {
        var testLinkUserKey = $('testLinkUserKey').value;
        var testLinkCustomField = $('testLinkCustomField').value;
        var testLinkUrl = $('testLinkUrl').value;

        var testLinkSuccessMessage = document.getElementById("testLinkSuccessMessage");
        var testLinkWarningMessage = document.getElementById("testLinkWarningMessage");
        var testLinkErrorMessage = document.getElementById("testLinkErrorMessage");
        testLinkSuccessMessage.hide();
        testLinkWarningMessage.hide();
        testLinkErrorMessage.hide();


        BS.ajaxRequest($('TestLinkAdminPageForm').action, {
            parameters: 'testLinkUserKey=' + testLinkUserKey
                    + '&testLinkCustomField=' + testLinkCustomField
                    + '&testLinkUrl=' + testLinkUrl,
            onComplete: function (transport) {
                if (transport.responseText) {
                    try {
                        var xml = jQuery.parseXML(transport.responseText);
                        var result = xml.getElementsByTagName('result')[0];
                        if ("testLinkSuccessMessage" == result.id) {
                            setValue(testLinkSuccessMessage, result.textContent);
                        } else if ("testLinkWarningMessage" == result.id) {
                            setValue(testLinkWarningMessage, result.textContent);
                        } else {
                            setValue(testLinkErrorMessage, result.textContent);
                        }
                    } catch (err) {
                        setValue(testLinkErrorMessage, err);
                    }
                }
            }
        });
    }

    function setValue(resultSpan, value) {
        resultSpan.show();
        resultSpan.textContent = value;
    }
</script>



<h3>Set TestLink configuration:</h3>

<form id="TestLinkAdminPageForm" action="/saveUserKeys/" method="post" onSubmit="">
    <div>
        <table class="runnerFormTable">
            <tr>
                <td><label>User API Key:</label></td>
                <td><input type="text" id="testLinkUserKey" name="testLinkUserKey" value="<c:out value="${testLinkUserKey}"/>"/>
                </td>
            </tr>
            <tr>
                <td><label>TestLink URL:</label></td>
                <td><input type="text" id="testLinkUrl" name="testLinkUrl" value="<c:out value="${testLinkUrl}"/>"/>
                </td>
            </tr>
            <tr>
                <td><label>Custom field name:</label></td>
                <td><input type="text" id="testLinkCustomField" name="testLinkCustomField" value="<c:out value="${testLinkCustomField}"/>"/>
                </td>
            </tr>
        </table>
    </div>
    <div class="saveButtonsBlock">
        <input type="button" name="submitTestLinkAdminPageForm" value="Save" onclick="return sendReqSave();"
               class="btn btn_primary submitButton"/>
        <span class="icon_success icon16 successMessage" id="testLinkSuccessMessage" style="display:none;"></span>
        <div class="icon_before icon16 attentionComment" id="testLinkWarningMessage" style="display:none;"></div>
        <span class="icon_error icon16 errorMessage" id="testLinkErrorMessage" style="display:none;"></span>
    </div>
</form>