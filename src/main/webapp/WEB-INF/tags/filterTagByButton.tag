<%@ tag import="org.springframework.data.domain.Sort" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="true" %>
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<div class="input-group" id="${id}">
    <input type="hidden" name="${id}" value="${current}">
    <c:forEach var="enumValue" items="${enumValues}" varStatus="status">
        <c:choose>
            <c:when test="${status.count == 1}">
                <a class="rounded-start btn btn-<c:if test="${current != enumValue}">outline-</c:if>success" id="${enumValue}">${enumValue.value}</a>
            </c:when>
            <c:when test="${status.count == 2}">
                <a class="btn btn-<c:if test="${current != enumValue}">outline-</c:if>danger" id="${enumValue}">${enumValue.value}</a>
            </c:when>
            <c:when test="${status.count == 3}">
                <a class="rounded-end btn btn-<c:if test="${current != enumValue}">outline-</c:if>dark" id="${enumValue}">${enumValue.value}</a>
            </c:when>
        </c:choose>
    </c:forEach>
</div>