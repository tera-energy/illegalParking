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
        <%@ include file="/WEB-INF/views/reference/head.jsp"%>

        <link href="<%=contextPath%>/resources/css/area/map-styles.css" rel="stylesheet"/>
        <link href="<%=contextPath%>/resources/css/parking/map-styles.css" rel="stylesheet"/>
        <body>

            <stripes:layout-component name="nav" />

            <div id="layoutSidenav">

                <stripes:layout-component name="side"/>

                <div id="layoutSidenav_content">

                    <stripes:layout-component name="contents"/>

                    <stripes:layout-component name="footer" />

                </div>
            </div>

            <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=55cc3405408d4cfbef37c3a93a5422c4&libraries=services,clusterer,drawing"></script>
            <stripes:layout-component name="javascript"/>

        </body>
    </html>
</stripes:layout-definition>