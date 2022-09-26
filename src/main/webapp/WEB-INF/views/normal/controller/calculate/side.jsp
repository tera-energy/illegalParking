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
				<a class="nav-link" href="${pageContext.request.contextPath}/calculate/calculateList" id="side_calculateList">
					<div class="sb-nav-link-icon"><i class="fas fa-calculator"></i></div>
					결재목록
				</a>

				<div class="sb-sidenav-menu-heading">제품</div>
				<a class="nav-link" href="${pageContext.request.contextPath}/calculate/productList" id="productList">
					<div class="sb-nav-link-icon"><i class="fas fa-calculator"></i></div>
					상품목록
				</a>
				<a class="nav-link" href="${pageContext.request.contextPath}/calculate/productAdd" id="productAdd">
					<div class="sb-nav-link-icon"><i class="fas fa-fill-drip"></i></div>
					상품등록
				</a>
			</div>

		</div>

	</nav>

</div>