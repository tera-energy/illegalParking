<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<% String contextPath = request.getContextPath(); %>

<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="false" %>

<!-- content -->
<div class="modal" id="${id}">
	<div class="userModal_body">
		<form id = "userAddForm">
			<div class="row mb-4 border-bottom">
				<div class="col-10 d-flex justify-content-center fw-bold"><h3>관공서추가</h3></div>
				<div class="col-2 d-flex justify-content-end">
					<a class="btn btn-close" aria-label="Close" id="modalClose"></a>
				</div>
			</div>
			<div class="row mb-2">
				<div class="col-4 d-flex flex-row-reverse">
					<span class="mt-2 ">관공서명</span>
				</div>
				<div class="col-8 d-flex flex-row">
					<input type="text" class="form-control" id="name" name="name" placeholder="관공서명을 입력하세요.">
				</div>
			</div>
			<div class="row mb-2">
				<div class="col-4 d-flex flex-row-reverse">
					<span class="mt-2 ">지역</span>
				</div>
				<div class="col-8 d-flex flex-row">
					<select class="form-select" id="locationType" name="locationType">
						<c:forEach items="${enumValues}" var="enumValue">
							<option value="${enumValue}" <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="row mb-2">
				<div class="col-4 d-flex flex-row-reverse">
					<span class="mt-2">아이디</span>
				</div>
				<div class="col-8 d-flex flex-row">
					<input type="text" class="form-control" id="userName" name="userName" placeholder="아이디를 입력하세요.">
				</div>
			</div>
			<div class="row mb-2">
				<div class="col-4 d-flex flex-row-reverse">
					<span class="mt-2">패스워드</span>
				</div>
				<div class="col-8 d-flex flex-row">
					<input type="text" class="form-control" id="password" name="password" placeholder="패스워드를 입력하세요.">
				</div>
			</div>
		</form>
		<div class="row mb-2">
			<div class="input-group d-flex justify-content-end">
				<a class="btn-primary btn" id="createUser">생성</a>
			</div>
		</div>
	</div>
</div>
<script type="application/javascript">
    $(function () {
        // 관공서 사용자 추가
        $('#createUser').on('click', function () {
            if (!confirm("등록하시겠습니까?")) {
                return;
			}

            let getData = function (id) {
                let arr = $('#'+id).serializeArray();
                log(arr);
                let data = {};
                $(arr).each(function (index, obj) {
                    data[obj.name] = obj.value;
                });
                return data;
            }

            let result = $.JJAjaxAsync({
                url: _contextPath + "/set",
            	data: getData('userAddForm')
            });

            if ( result.success == true) {
                $.search();
            } else {
            	alert(result.msg);
            }

        });
    });
</script>
