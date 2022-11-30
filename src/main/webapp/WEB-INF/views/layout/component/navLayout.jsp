<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 8:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>

<stripes:layout-definition>
	<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">

		<!-- Navbar Brand-->
		<a class="navbar-brand ps-1" href="${pageContext.request.contextPath}/">
			<img class="ms-2" src="${contextPath}/resources/assets/img/logo.png" style="width: 30px; height: 30px;">
				${mainTitle}
		</a>

		<!-- Sidebar Toggle-->
		<button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0 ms-2" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>

		<ul id="navMenu" class="navbar-nav ms-auto mt-2 mt-lg-0">
			<c:choose>
				<c:when test="${_user.role == 'GOVERNMENT'}">
					<li class="nav-item me-5">
						<a class="nav-link" id="navhome" href="${pageContext.request.contextPath}/home">홈</a>
					</li>
					<li class="nav-item me-5">
						<a class="nav-link" id="navreportByGovernment" href="${pageContext.request.contextPath}/reportByGovernment">신고</a>
					</li>
				</c:when>
				<c:otherwise>
					<li class="nav-item me-5">
						<a class="nav-link" id="navnotice" href="${pageContext.request.contextPath}/notice">공지사항</a>
					</li>
					<li class="nav-item me-5">
						<a class="nav-link" id="navreport" href="${pageContext.request.contextPath}/report">신고</a>
					</li>
					<li class="nav-item me-5">
						<a class="nav-link" id="navuser" href="${pageContext.request.contextPath}/user/userList">사용자</a>
					</li>
					<li class="nav-item me-5">
						<a class="nav-link" id="navarea" href="${pageContext.request.contextPath}/area">불법주정차 구역</a>
					</li>
					<li class="nav-item me-5">
						<a class="nav-link" id="navparking" href="${pageContext.request.contextPath}/parking" tabindex="-1" aria-disabled="true">공영주차장</a>
					</li>
					<li class="nav-item me-5">
						<a class="nav-link" id="navpm" href="${pageContext.request.contextPath}/pm" tabindex="-1" aria-disabled="true">PM</a>
					</li>
					<li class="nav-item me-5">
						<a class="nav-link" id="navcalculate" href="${pageContext.request.contextPath}/calculate" tabindex="-1" aria-disabled="true">결재</a>
					</li>
				</c:otherwise>
			</c:choose>
		</ul>
		<ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown"
				   aria-expanded="false"><i class="fas fa-user fa-fw"></i></a>
				<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
						<%--                    <li><a class="dropdown-item" href="#!">환경설정</a></li>--%>
					<li><a class="dropdown-item" href="#!" id="myInfo">내정보</a></li>
					<li>
						<hr class="dropdown-divider"/>
					</li>
					<li><a class="dropdown-item" href="/logout">Logout</a></li>
				</ul>
			</li>
		</ul>
	</nav>
</stripes:layout-definition>