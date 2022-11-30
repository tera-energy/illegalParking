<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="option" type="java.lang.String" required="false" %>
<%@ attribute name="value" type="java.lang.Object" required="false" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<%@ attribute name="placeholder" type="java.lang.String" required="false" %>

<c:if test="${title != null}">
	<label for="${id}" class="form-label">${title}</label>
</c:if>
<div class="input-group align-content-center">
	<input id="${id}" type="password" class="form-control" name="${id}" value="${value}" ${option} placeholder="${placeholder}">
	<div id="eye" style="position:absolute; left:90%; margin: 8px; z-index: 99999;"><i class="fa fa-eye-slash fa-lg"></i></div>
</div>