<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2022-09-07
  Time: 오전 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
            <div class="nav">
                <div class="sb-sidenav-menu-heading">${subTitle}</div>
                <a class="nav-link" href="${pageContext.request.contextPath}/user/userList" id="side_userList">
                    <div class="sb-nav-link-icon"><i class="fas fa-building"></i></div>
                    관공서
                </a>
<%--                <a class="nav-link" href="${pageContext.request.contextPath}/reporter/reporterList" id="side_reporterList">--%>
<%--                    <div class="sb-nav-link-icon"><i class="fas fa-table"></i></div>--%>
<%--                    신고자--%>
<%--                </a>--%>
<%--                <a class="nav-link" href="${pageContext.request.contextPath}/car/carList" id="side_carList">--%>
<%--                    <div class="sb-nav-link-icon"><i class="fas fa-table"></i></div>--%>
<%--                    차량등록--%>
<%--                </a>--%>
            </div>
        </div>
    </nav>
</div>