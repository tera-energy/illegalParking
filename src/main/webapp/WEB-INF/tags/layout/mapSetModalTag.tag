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
<%@ attribute name="typeValues" type="java.lang.Object[]" required="true" %>

<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="false" %>

<!-- content -->
<div class="offcanvas offcanvas-end" id="${id}" tabindex="-1" aria-labelledby="offcanvasRightLabel" data-bs-backdrop="false">
	<form id="formAreaSetting" name="formAreaSetting">
		<input type="hidden" id="zoneSeq" name="zoneSeq" value=""/>
		<input type="hidden" id="eventSeq" name="eventSeq" value=""/>


		<div class="offcanvas-header">
			<h5 class="offcanvas-title" id="offcanvasRightLabel">구역설정</h5>
			<button type="button" class="btn-close canvasClose" data-bs-dismiss="offcanvas" aria-label="Close"></button>
		</div>

		<div class="offcanvas-body">

			<div class="card mb-2">
				<div class="card-header">
					불법주정차 구역 타입
				</div>
				<div class="card-body">
					<c:forEach items="${typeValues}" var="typeValue" varStatus="status">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="illegalType" id="zone${status.index}" value="${typeValue}" ${id eq 'areaViewModal' ? 'disabled' : ''}>
							<label class="form-check-label" for="zone${status.index}">${typeValue.value}</label>
						</div>
					</c:forEach>
				</div>
			</div>

			<div class="card mb-2">
				<div class="card-header">
					불법주정차 그룹지정
				</div>
				<div class="card-body">
					<div class="row">
						<div class="col-6">
							<select class="form-select" id="locationType" name="locationType" ${id eq 'areaViewModal' ? 'disabled' : ''}>
								<c:forEach items="${enumValues}" var="enumValue">
									<option value="${enumValue}" <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-6">
							<select class="form-select" id="name" name="name" ${id eq 'areaViewModal' ? 'disabled' : ''}></select>
						</div>
					</div>
				</div>
			</div>

			<div class="card mb-2 findSelect">
				<div class="card-header">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" value="" id="usedFirst" name="usedFirst" ${id eq 'areaViewModal' ? 'disabled' : ''}>
						<label class="form-check-label" for="usedFirst">
							첫번째 적용시간
						</label>
					</div>
				</div>
				<div class="card-body">
					<div class="row mt-2" id="firstTimeRow">
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="firstStartTime" title="시작" />
						</div>
						<div class="col">~</div>
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="firstEndTime" title="종료"/>
						</div>
					</div>
				</div>
			</div>

			<div class="card">
				<div class="card-header">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" value="" id="usedSecond" name="usedSecond" ${id eq 'areaViewModal' ? 'disabled' : ''}>
						<label class="form-check-label" for="usedSecond">
							두번째 적용시간
						</label>
					</div>
				</div>
				<div class="card-body">
					<div class="row mt-2" id="secondTimeRow">
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="secondStartTime" title="시작" />
						</div>
						<div class="col">~</div>
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="secondEndTime" title="종료"/>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="offcanvas-footer">
			<div class="row ms-2">
				<div class="input-group">
					<c:if test="${id eq 'areaSettingModal'}">
						<button type="button" class="btn btn-primary" id="btnModifyEvent">등록</button>
					</c:if>
					<button type="button" class="btn btn-outline-secondary canvasClose" data-bs-dismiss="offcanvas" id="btnClose">${id eq 'areaViewModal' ? '닫기' : '취소'}</button>
				</div>
			</div>
		</div>
	</form>
</div>
<script type="application/javascript">
	$(function (){
        // 위치 변경 이벤트
		$('#locationType').on('change', function (){
			$("#name option").remove();
           let locationType = $(this).val();
            $.setGroupNames(locationType);
		});

		$('.canvasClose').click(function(){
			$.SetMaxLevel($.LEVEL_TEN);
			$.changeOptionStroke();
		});

		$(document).keydown(function(event) {
			if ( event.keyCode === 27 || event.which === 27 ) {
				$.changeOptionStroke();
			}
		});

	});

</script>