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
                <a class="nav-link" href="${pageContext.request.contextPath}/area/map" id="side_map">
                    <div class="sb-nav-link-icon"><i class="fas fa-map"></i></div>
                    구역 보기
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/area/mapSet"  id="side_mapSet">
                    <div class="sb-nav-link-icon"><i class="fas fa-edit"></i></div>
                    구역 설정
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/area/groupList"  id="side_groupList">
                    <div class="sb-nav-link-icon"><i class="fas fa-sitemap"></i></div>
                    그룹 관리
                </a>
            </div>
        </div>
    </nav>
</div>