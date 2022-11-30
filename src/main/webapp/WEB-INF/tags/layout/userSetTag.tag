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
<%@ taglib prefix="layoutTags" tagdir="/WEB-INF/tags/layout" %>
<% String contextPath = request.getContextPath(); %>

<%@ attribute name="items" type="java.lang.Object" required="true" %>

<!-- content -->
<main id="userSet">
	<div class="container-fluid px-4">

		<h1 class="mt-4">사용자</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > 관공서</li>
		</ol>

		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header">
				<div class="row">
					<div class="col-9">
						<i class="fas fa-edit"></i> 관공서 상세 정보
					</div>
					<div class="col-3 d-flex justify-content-end">
						<a class="btn btn-close" id="userClose"></a>
					</div>
				</div>
			</div>
			<form id="userForm">
				<input type="hidden" name="userSeq" id="userSeq">
				<div class="row mt-2 mb-2">
					<div class="col-1 d-flex justify-content-lg-end">
						<label class="mt-2">관공서명 : </label>
					</div>
					<div class="col-3">
						<input class="form-control" id="officeName" name="name" disabled>
					</div>
				</div>
				<div class="row mb-2">
					<div class="col-1 d-flex justify-content-lg-end"><label class="mt-2">지역 : </label></div>
					<div class="col-3">
						<tags:selectTagWithType id="locationType" current="" items="${items}" option="disabled"/>
					</div>
				</div>
				<div class="row mb-2">
					<div class="col-1 d-flex justify-content-lg-end"><label class="mt-2">아이디 : </label></div>
					<div class="col-3">
						<input class="form-control" id="userName" name="userName">
					</div>
				</div>
				<div class="row mb-2">
					<div class="col-1 d-flex justify-content-lg-end"><label class="mt-2">패스워드 : </label></div>
					<div class="col-3">
						<div class="input-group">
							<input class="form-control" type="password" id="password" name="password">
							<div id="eye" style="position:absolute; left:87%; margin: 8px; z-index: 99999;"><i class="fa fa-eye-slash fa-lg"></i></div>
						</div>
					</div>
				</div>
			</form>

			<div class="row mb-3">
				<div class="col-4 d-flex justify-content-lg-end">
					<a class="btn btn-primary" id="userModify"><i class="fa fa-cog"></i> 정보수정</a>
				</div>
			</div>

			<div class="row mb-2">
				<div class="col-1 mt-2 d-flex justify-content-lg-center">
					<label>관리 그룹</label>
				</div>
				<div class="col-2 d-flex justify-content-lg-start">
					<a class="btn btn-outline-primary" id="userGroupAdd"><i class="fas fa-plus"></i> 그룹추가</a>
				</div>
			</div>

			<nav class="navbar navbar-expand-lg navbar-light bg-light ms-3 me-3 mb-2">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0" id="addUserGroupNav"></ul>
			</nav>

			<div class="row">
				<div class="col-8 d-flex justify-content-lg-center">
					<div id="chartContainer"></div>
				</div>
				<div class="col-4">
					<div class="row mt-5">
						<div class="col-6">
							<table class="table table-bordered shadow-sm">
								<tbody>
								<tr>
									<td class="bg-light">
										<div class="d-flex justify-content-lg-end fw-bold text-secondary"> 총 신고접수</div>
									</td>
									<td class="bg-light bg-gradient">
										<div class=" fw-bold text-primary"><span id="totalCount"></span></div>
									</td>
								</tr>
								<tr>
									<td class="bg-light bg-gradient">
										<div class="d-flex justify-content-lg-end fw-bold text-secondary"> 대기</div>
									</td>
									<td class="bg-light bg-gradient">
										<div class=" fw-bold text-primary"><span id="completeCount"></span></div>
									</td>
								</tr>
								<tr>
									<td class="bg-light bg-gradient">
										<div class="d-flex justify-content-lg-end fw-bold text-secondary"> 미처리</div>
									</td>
									<td class="bg-light bg-gradient">
										<div class=" fw-bold text-primary"><span id="exceptionCount"></span></div>
									</td>
								</tr>
								<tr>
									<td class="bg-light bg-gradient">
										<div class="d-flex justify-content-lg-end fw-bold text-secondary"> 처리</div>
									</td>
									<td class="bg-light bg-gradient">
										<div class="fw-bold text-primary"><span id="penaltyCount"></span></div>
									</td>
								</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
</main>
<layoutTags:userGroupAddTag id="userGroupAddTag" enumValues="${items}" current=""/>

<script src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
<script type="application/javascript">
    $(function () {

        let setPasswoardUi = function () {
            let $eyeIcon = $('#eye').children();
            let className = $eyeIcon.attr('class');
            if (className.indexOf('fa-eye-slash') > -1) {
                $eyeIcon.attr('class', className.replace('fa-eye-slash', 'fa-eye'));
                $('#password').attr('type',"text");
            } else {
                $eyeIcon.attr('class', className.replace('fa-eye', 'fa-eye-slash'));
                $('#password').attr('type','password');
            }
        };

        $('#eye').on('click', function (){
            setPasswoardUi();
        });

        // 정보 수정 이벤트
        $('#userModify').on('click', function () {

            if (confirm("정보 수정 하시겠습니까 ?")) {
                let getData = function (id) {
                    let arr = $('#' + id).serializeArray();
                    let data = {};
                    $(arr).each(function (index, obj) {
                        data[obj.name] = obj.value;
                    });
                    return data;
                }

                let result = $.JJAjaxAsync({
                    url: _contextPath + '/modify',
                    data: getData('userForm')
                });

                if (result.success) {
                    alert("수정 되었습니다.");
                } else {
                    alert(result.msg);
                }

            }
        });

        // 관리 그룹 추가 이벤트
        $('#userGroupAdd').on('click', function () {
            $('#userGroupAddTag').show();
        });

        // 관리 그룹 삭제 이벤트
        $('#addUserGroupNav a').on('click', function () {
            if (confirm("삭제 하시겠습니까")) {
                $(this).parent().remove();
            }
        });

        // 상세 정보 닫기 이벤트
        $('#userClose').on('click', function () {
            $('#userMain').show();
            $('#userSet').hide();
            $.search();
        });

        // 그룹 추가 태그 숨기기
        $('#userGroupAddTag').hide();

    });
</script>

