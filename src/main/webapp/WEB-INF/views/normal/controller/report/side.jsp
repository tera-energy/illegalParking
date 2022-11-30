<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

				<c:choose>
					<c:when test="${_user.role == 'GOVERNMENT'}">
						<a class="nav-link" href="${pageContext.request.contextPath}/report/reportListByGovernment" id="side_reportListByGovernment">
							<div class="sb-nav-link-icon"><i class="fas fa-envelope"></i></div>
							접수목록
						</a>
					</c:when>
					<c:otherwise>
						<a class="nav-link" href="${pageContext.request.contextPath}/report/receiptList" id="side_receiptList">
							<div class="sb-nav-link-icon"><i class="fas fa-upload"></i></div>
							신고목록
						</a>
						<a class="nav-link" href="${pageContext.request.contextPath}/report/reportList" id="side_reportList">
							<div class="sb-nav-link-icon"><i class="fas fa-envelope"></i></div>
							접수목록
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</nav>
</div>