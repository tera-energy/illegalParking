<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="column" type="java.lang.String" %>
<%@ attribute name="direction" type="java.lang.String" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<div id="${id}" class="filterColumn">
	<c:if test="${title != null}">
		<label for="${id}" class="form-label">${title}</label>
	</c:if>
	<div class="input-group">
		<select class="form-select" name="filterColumn">
			<c:forEach items="${enumValues}" var="enumValue">
				<option value="${enumValue}" <c:if test="${enumValue eq column}">selected</c:if>>${enumValue.value}</option>
			</c:forEach>
		</select>
	</div>
</div>