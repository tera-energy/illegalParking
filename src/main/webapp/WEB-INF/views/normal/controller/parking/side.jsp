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
                <a class="nav-link active" href="${pageContext.request.contextPath}/parking/map" id="side_map">
                    <div class="sb-nav-link-icon"><i class="fas fa-map"></i></div>
                    위치 보기
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/parking/parkingList" id="side_parkingList">
                    <div class="sb-nav-link-icon"><i class="fas fa-parking"></i></div>
                    주차장 목록
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/parking/parkingAdd" id="side_parkingAdd">
                    <div class="sb-nav-link-icon"><i class="fas fa-edit"></i></div>
                    주차장 등록
                </a>
            </div>
        </div>
    </nav>
</div>