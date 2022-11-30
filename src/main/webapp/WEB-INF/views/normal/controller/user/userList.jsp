<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ page import="com.teraenergy.illegalparking.model.dto.user.enums.UserGovernmentFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/navHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<!-- side -->
	<stripes:layout-component name="side">
		<jsp:include page="side.jsp" flush="true"/>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main id="userMain">
			<div class="container-fluid px-4">
				<h1 class="mt-4">사용자</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 관공서</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						관공서 정보
					</div>
					<div class="card-body">
						<form class="row mb-3 g-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-2">
								<a class="btn btn-primary" id="addGovernmentOffice"><i class="fa fa-city"></i> 관공서추가</a>
							</div>
							<div class="col-5"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${UserGovernmentFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}"/>
								<tags:searchTagWithSelect id="searchStr2" searchStr="${searchStr2}" items="${LocationType.values()}"/>
							</div>

						</form>
						<table class="table table-hover table-bordered" id="userTable">
							<thead>
							<tr class="table-light">
								<th scope="col" class="text-center" style="width: 10%">지역</th>
								<th scope="col" class="text-center" style="width: 17%">관공서명</th>
								<th scope="col" class="text-center" style="width: 15%">아이디</th>
								<th scope="col" class="text-center" style="width: 15%">패스워드</th>
								<th scope="col" class="text-center" style="width: 7%">관리그룹</th>
								<th scope="col" class="text-center" style="width: 7%">신고접수건</th>
								<th scope="col" class="text-center" style="width: 7%">대기</th>
								<th scope="col" class="text-center" style="width: 7%">미처리</th>
								<th scope="col" class="text-center" style="width: 7%">처리</th>
								<th scope="col" class="text-center" style="width: 8%">상세보기</th>
							</tr>
							</thead>
							<tbody>
								<c:forEach items="${userGovernmentDtos}" var="userGovernmentDto" varStatus="status">
									<tr seq="${userGovernmentDto.userSeq}">
										<td class="text-center align-middle">
											<input type="hidden" value="${userGovernmentDto.locationType}">
												${userGovernmentDto.locationType.value}
										</td>
										<td>${userGovernmentDto.officeName}</td>
										<td class="text-center align-middle">${userGovernmentDto.userName}</td>
										<td class="text-center align-middle">
											<input type="hidden" value="${userGovernmentDto.password}">
											**************
										</td>
										<td class="text-end align-middle">${userGovernmentDto.groupCount}</td>
										<td class="text-end align-middle">${userGovernmentDto.totalCount}</td>
										<td class="text-end align-middle">${userGovernmentDto.completeCount}</td>
										<td class="text-end align-middle">${userGovernmentDto.exceptionCount}</td>
										<td class="text-end align-middle">${userGovernmentDto.penaltyCount}</td>
										<td class="text-center p-0 align-middle"><a class="btn btn-sm btn-outline-dark m-0">상세보기</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" offsetPage="${offsetPage}" totalPages="${totalPages}" items="10,25,50" pageSize="${pageSize}"/>
					</div>
				</div>
			</div>
		</main>

		<div class="wrap-loading">
			<div style="position: absolute; margin-left: 45%; top: 45%;">
				<div class="row">
					<span>로딩중...</span>
				</div>
				<div class="spinner-border" style="width: 2.5rem; height: 2.5rem; " role="status">
					<span class="visually-hidden"></span>
				</div>
			</div>
		</div>

		<layoutTags:userSetTag items="${LocationType.values()}"/>

		<layoutTags:userAddTag id="userAddModal" enumValues="${LocationType.values()}"/>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/user/userList-scripts.js"></script>
		<script type="application/javascript">

            $(function () {

                // 검색 입력 방식 선택 함수
                function searchSelect(filterColumn) {
                    if (filterColumn === 'RESULT') {
                        $('#searchStrGroup').hide();
                        $('#searchStr2Group').show();
                    } else {
                        $('#searchStrGroup').show();
                        $('#searchStr2Group').hide();
                    }
                }

                // 초기화 함수
                function initialize() {

                    // 정렬 ??
                    $('#orderBy a').on('click', function () {
                        $.search();
                    });

                    // 일반 검색
                    $('#searchStr').next().on('click', function (event) {
                        $.search();
                    });

                    // 타입 검색
                    $('#searchStr2').next().on('click', function (event) {
                        $.search();
                    });

                    // 페이지 이동 이벤트
                    $('#pagination').find("li").on('click', function () {
                        let ul = $(this).parent();
                        let totalSize = ul.children("li").length;
                        if (totalSize <= 3) {
                            return;
                        }

                        let pageNumber;

                        if ($(this).text() === "<") {
                            pageNumber = Number.parseInt(ul.children('.active').text());
                            if (pageNumber == 1) return;
                            pageNumber = pageNumber - 1;
                        } else if ($(this).text() === ">") {
                            pageNumber = Number.parseInt(ul.children('.active').text());
                            let myLocation = $(this).index();
                            let activeLocation = ul.children('.active').index();
                            if (activeLocation == (myLocation - 1)) {
                                return;
                            }
                            pageNumber = pageNumber + 1;
                        } else {
                            pageNumber = Number.parseInt($(this).text());
                        }

                        $.search(pageNumber);
                    });

                    // 페이지 사이즈 변경 이벤트
                    $('#paginationSize').on("change", function () {
                        $('#pageSize').val($(this).val());
                        $.search();
                    });

                    // 사용자 테이블 항목 이벤트
                    $('#userTable tbody tr').on('click', function () {
                        let useSeqStr = $(this).attr('seq');
                        let userSeq = Number.parseInt(useSeqStr);

                        let userGovernmentDto = {
                            userSeq : userSeq,
                            locationType : $(this).children("td:eq(0)").find('input').val(),
                            officeName : $(this).children("td:eq(1)").text(),
                            userName : $(this).children("td:eq(2)").text(),
                            password : $(this).children("td:eq(3)").find('input').val(),
                            totalCount : $(this).children("td:eq(5)").text(),
                            completeCount : $(this).children("td:eq(6)").text(),
                            exceptionCount : $(this).children("td:eq(7)").text(),
                            penaltyCount : $(this).children("td:eq(8)").text()
						}

                        let result = $.JJAjaxAsync({
							url: _contextPath + "/userGroup/gets",
							data: {
                                userSeq : userSeq
							}
						});

                        if (result.success) {
                            userGovernmentDto.userGroupDtos = result.data;
                        }

                        $.initializeUserSetTag(userGovernmentDto);

                        $('#userMain').hide();
                        $('#userSet').show();
                    });

                    $('#filterColumn').find('select[name="filterColumn"]').on('change', function () {
                        searchSelect($(this).val());
                    });

                    $('#modalClose').on('click', function () {
                        $('#userAddModal').hide();
                        $('body').css({
                            'overflow': 'auto'
                        });
                    });

                    $('#addGovernmentOffice').on('click', function () {
                        $('#userAddModal').show();
                        $('body').css({
                            'overflow': 'hidden'
                        });
                    });

                    $('#userAddModal').hide();
                    $('#userSet').hide();
					$.loading.immediatelyStop();
                }

                // 검색 2 숨기기
                $('#searchStr2').hide();

                // 필터에 의한 검색 입력 방식 선택
                searchSelect('${filterColumn}');

                // 초기화
                initialize();
            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>