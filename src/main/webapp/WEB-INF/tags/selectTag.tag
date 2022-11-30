<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="true" %>
<%@ attribute name="option" type="java.lang.String" required="false" %>
<%@ attribute name="items" type="java.lang.Object" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>

<c:if test="${title != null}">
    <label for="${id}" class="form-label">${title}</label>
</c:if>
<select id="${id}" class="form-select" name="${id}" ${option}>
    <c:forEach var="item" items="${items}" varStatus="status">
        <option value="${item}" <c:if test="${current eq item}">selected</c:if> >${item}</option>
    </c:forEach>
</select>