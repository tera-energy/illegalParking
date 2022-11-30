<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/htmlLayout.jsp">

    <!-- content -->
    <stripes:layout-component name="contents">
        <main>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-6">
                        <div class="text-center mt-4">
                            <img class="mb-4 img-error" src="<%=contextPath%>/resources/assets/img/error-404-monochrome.svg"/>
                            <p class="lead">This requested URL was not found on this server.</p>
                            <p class="lead">${msg}</p>
                            <a href="#" id="back">
                                <i class="fas fa-arrow-left me-1"></i>
                                Return to Back
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </stripes:layout-component>

    <!-- footer -->
    <stripes:layout-component name="footer">
        <stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
    </stripes:layout-component>

    <!-- javascript -->
    <stripes:layout-component name="javascript"></stripes:layout-component>

</stripes:layout-render>