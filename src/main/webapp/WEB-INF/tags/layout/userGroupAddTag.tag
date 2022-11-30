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
	<div class="userGroupAddModal_body">
		<div class="row mb-4 border-bottom">
			<div class="col-10 fw-bold d-flex justify-content-center"><h3>관리그룹추가</h3></div>
			<div class="col-2 d-flex justify-content-end">
				<a class="btn btn-close text-reset" aria-label="Close" id="closeUserGroupAdd"></a>
			</div>
		</div>

		<form id="userGroupAddForm" class=" mb-4">
			<div class="row mb-2">
				<div class="col-4 d-flex flex-row-reverse"><span>그룹위치</span></div>
				<div class="col-8 d-flex flex-row">
					<select class="form-select" id="userGroupLocationType" name="locationType">
						<c:forEach items="${enumValues}" var="enumValue">
							<option value="${enumValue}" <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-4 d-flex flex-row-reverse">그룹명</div>
				<div class="col-8 d-flex flex-row">
					<select class="form-select" id="name" name="name"></select>
				</div>
			</div>
		</form>

		<div class="row mb-3">
			<div class="input-group d-flex justify-content-end me-2">
				<a class="btn-primary btn" id="createUserGroupAdd">추가</a>
			</div>
		</div>
	</div>
</div>

<script type="application/javascript">
    $(function () {

        // 관리 그룹 추가 태그 닫기 함수
        function closeUseGroupAddTag() {
            $('#userGroupLocationType').val('SEOUL');
            $('#userGroupLocationType').trigger('change');
            $('#name').find('option').remove();
            $('#userGroupAddTag').hide();
        }

        // 관리 그룹 타입 변경 이벤트
        $('#userGroupLocationType').on('change', function () {
            let locationType = $(this).val();
            $.setUserGroupNames(locationType);
        });

        // 그룹 추가 팝업창 닫기 이벤트
        $('#closeUserGroupAdd').on('click', function () {
            closeUseGroupAddTag();
        });




        // 관리 그룹 추가 이벤트
        $('#createUserGroupAdd').on('click', function () {
            let userGroup = $.getData('userGroupAddForm');
            if (userGroup.name === undefined) {
                alert("그룹명이 존재 하지 않습니다.");
                return;
            }

            let result = $.JJAjaxAsync({
                url: _contextPath + "/userGroup/set",
                data: {
                    userSeq: $('#userSeq').val(),
                    locationType: userGroup.locationType,
                    name: userGroup.name
                }
            });

            if (result.success) {
                $.addUserGroupList(result.data);
                $.setStatics();
                $.bindUserGroupNavEvent();
            } else {
                alert("등록 실패 하였습니다. \n" + result.msg);
            }

            closeUseGroupAddTag();

        });
    });
</script>