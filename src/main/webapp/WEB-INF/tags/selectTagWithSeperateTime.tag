<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<%@ attribute name="hour" type="java.lang.Integer" required="false" %>
<%@ attribute name="minute" type="java.lang.Integer" required="false" %>
<%@ attribute name="option" type="java.lang.String" required="false" %>

<c:if test="${title !=null}">
    <label for="${id}" class="form-label">${title}</label>
</c:if>
<div class="input-group">
    <input id="${id}" name="${id}" type="hidden">
    <select id="${id}Hour" class="form-select timeSelect" ${option}>
        <c:forEach begin="0" end="23" varStatus="status">
            <c:choose>
                <c:when test="${status.index < 10}">
                    <option value="0${status.index}"
                            <c:if test="${hour == status.index}">selected</c:if> > 0${status.index}</option>
                </c:when>
                <c:otherwise>
                    <option value="${status.index}" <c:if test="${hour == status.index}">selected</c:if>>${status.index}</option>
                </c:otherwise>
            </c:choose>

        </c:forEach>
    </select>
    <div class="ms-2 me-2">:</div>
    <select id="${id}Minute" class="form-select timeSelect" ${option}>
        <c:forEach begin="0" end="59" varStatus="status">
            <c:choose>
                <c:when test="${status.index < 10}">
                    <option value="0${status.index}">0${status.index}</option>
                </c:when>
                <c:otherwise>
                    <option value="${status.index}" <c:if test="${minute == status.index}">selected</c:if>>${status.index}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
</div>
<script type="application/javascript">

    function getTimeBySelectSeperateTime() {
        log($('#${id}Hour').val())
      log(${id})
        return $('#${id}Hour').val() + ":" + $('#${id}Minute').val();
    }

    function setTimeBySelectSeperateTime() {
        $('#' + '${id}').val(getTimeBySelectSeperateTime());
    }

    $('#' + '${id}' +'Hour').on('change', function () {
        log($('#' + '${id}' +'Hour').val());
      log(this)

      setTimeBySelectSeperateTime();
        log(getTimeBySelectSeperateTime());
    });

    $('#' + '${id}' +'Minute').on('change', function () {
        setTimeBySelectSeperateTime();
        log(getTimeBySelectSeperateTime());
    });

</script>