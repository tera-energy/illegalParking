<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pageNumber" type="java.lang.Integer" required="true" %>
<%@ attribute name="begin" type="java.lang.Integer" required="false" %>
<%@ attribute name="end" type="java.lang.Integer" required="false" %>
<%@ attribute name="isBeginOver" type="java.lang.Boolean" required="true" %>
<%@ attribute name="isEndOver" type="java.lang.Boolean" required="true" %>
<%@ attribute name="totalPages" type="java.lang.Integer" required="true" %>
<%@ attribute name="pageSize" type="java.lang.Integer" required="true" %>
<%@ attribute name="items" type="java.lang.Object" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<!-- Pagination-->
<nav aria-label="Pagination">
	<hr class="my-0"/>
	<div class="row">
		<div class="col-1">
			<div class="my-2">
				<tags:selectTag id="paginationSize" items="${items}" current="${pageSize}"/>
			</div>
		</div>
		<div class="col">
			<ul class="pagination justify-content-center my-4 me-5 pe-5" id="pagination">
				<li class="page-item"><a class="page-link"><</a></li>
<%--				<c:if test="${isBeginOver}">--%>
<%--					<li class="page-item ms-2 me-2">...</li>--%>
<%--				</c:if>--%>
				<c:choose>
					<c:when test="${ totalPages != null && totalPages <= 3}">
						<c:forEach var="item" begin="1" end="${totalPages}" varStatus="status">
							<li class="page-item <c:if test="${pageNumber == item}">active</c:if>" aria-current="page"><a class="page-link">${item}</a></li>
						</c:forEach>
					</c:when>
					<c:when test="${ totalPages > 3 && (totalPages - pageNumber) < 3 }">
						<c:forEach var="item" begin="${totalPages-2}" end="${totalPages}" varStatus="status">
							<li class="page-item <c:if test="${pageNumber == item}">active</c:if>" aria-current="page"><a class="page-link">${item}</a></li>
						</c:forEach>
					</c:when>
					<c:when test="${ totalPages > 3 && (totalPages - pageNumber) >= 3 }">
						<c:forEach var="item" begin="${pageNumber}" end="${pageNumber+2}" varStatus="status">
							<li class="page-item <c:if test="${pageNumber == item}">active</c:if>" aria-current="page"><a class="page-link">${item}</a></li>
						</c:forEach>
					</c:when>
					<c:otherwise>${totalPages}</c:otherwise>
				</c:choose>

				<c:if test="${isEndOver}">
					<li class="page-item ms-2 me-2">...</li>
				</c:if>
				<li class="page-item"><a class="page-link">></a></li>
			</ul>
		</div>
	</div>
</nav>