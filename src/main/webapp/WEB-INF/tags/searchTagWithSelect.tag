<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2022-09-19
  Time: 오후 3:23
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="searchStr" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<%@ attribute name="option" type="java.lang.String" required="false" %>
<%@ attribute name="items" type="java.lang.Object" required="true" %>

<c:if test="${title != null}">
  <label for="${id}" class="form-label">${title}</label>
</c:if>
<div class="input-group" id="${id}Group">
  <select id="${id}" class="form-select" name="${id}" ${option}>
    <c:forEach var="item" items="${items}" varStatus="status">
      <option value="${item}" <c:if test="${searchStr eq item}">selected</c:if> >${item.value}</option>
    </c:forEach>
  </select>
  <a class="btn btn-outline-primary" id="search">검색</a>
</div>