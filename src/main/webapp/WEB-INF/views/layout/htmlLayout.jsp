<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 8:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>

<stripes:layout-definition>
    <!DOCTYPE html>
    <html>
    <%@ include file="/WEB-INF/views/reference/head.jsp" %>

    <body>

    <div id="layoutError">
        <div id="layoutError_content">
            <stripes:layout-component name="contents"/>
        </div>
        <div id="layoutError_footer">
            <stripes:layout-component name="footer" />
        </div>
    </div>

    <stripes:layout-component name="javascript"/>

    </body>
    </html>
</stripes:layout-definition>