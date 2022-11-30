<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
  접수 목록
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType" %>

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
		<main id="reportMain">
			<div class="container-fluid px-4">
				<h1 class="mt-4">접수목록</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item text-black-50">${subTitle} > 접수목록</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header bg-dark">
						<i class="text-white fas fa-table me-1"></i>
						<span class="text-white">불법 주정차 신고 정보</span>
					</div>
					<div class="card-body">
						<form class="row mb-3 g-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-3">
								<tags:filterTagByButton id="reportStateType" current="${reportStateType}" enumValues="${ReportStateType.values()}"/>
							</div>

							<div class="col-4"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${ReportFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}"/>
							</div>
						</form>
						<table class="table table-hover table-bordered" id="reportTable">
							<thead>
							<tr class="table-light rounded-3">
								<th scope="col" class="text-center" style="width: 10%">신고자</th>
								<th scope="col" class="text-center" style="width: 10%">차량번호</th>
								<th scope="col" class="text-center" style="width: 42%">위치</th>
								<th scope="col" class="text-center" style="width: 15%">신고일시</th>
								<th scope="col" class="text-center" style="width: 7%">중복회수</th>
								<th scope="col" class="text-center" style="width: 8%">상태</th>
								<th scope="col" class="text-center" style="width: 8%">상세보기</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="report" items="${reports}" varStatus="status">
								<tr>
									<td class="text-center">
										<input type="hidden" value="${report.reportSeq}" id="reportSeq">
											${report.name}
									</td class="text-center">
									<td class="text-center">${report.carNum}</td>
									<td>${report.addr}</td>
									<td class="text-center">
										<fmt:parseDate value="${report.regDt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm"/>
									</td>
									<td class="text-end">${report.overlapCount}</td>
									<td class="text-center">
										<c:choose>
											<c:when test="${report.reportStateType == 'PENALTY'}"><span class="text-danger fw-bold">${report.reportStateType.value}</span></c:when>
											<c:when test="${report.reportStateType == 'EXCEPTION'}"><span class="text-dark fw-bold">${report.reportStateType.value}</span></c:when>
											<c:otherwise><span class="text-success fw-bold">${report.reportStateType.value}</span></c:otherwise>
										</c:choose>
									</td>
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

		<layoutTags:reportSetTag/>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/report/reportList-scripts.js"></script>
		<script type="application/javascript">
            $(function () {

                // 검색
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath + "/reportListByGovernment?" + $('form').serialize();
                }

                // 신고 접수 버튼 설정 함수
                function setBtnReceiptStateType(receiptStateType) {
                    let $btnException = $('#btnException');
                    let $btnPenalty = $('#btnPenalty');
                    let $labelHow = $('#labelHow');

                    let $exception = $btnException.parent();
                    let $penalty = $btnPenalty.parent();

                    $exception.hide();
                    $penalty.hide();

                    $btnException.removeClass();
                    $btnPenalty.removeClass();

                    switch (receiptStateType) {
                        case 'EXCEPTION':
                            $btnException.addClass("btn btn-dark");
                            $btnPenalty.addClass("btn btn-outline-danger");
                            $btnException.attr('disabled', true);
                            $btnPenalty.attr('disabled', true);

                            $labelHow.hide();
                            $exception.show();
                            $penalty.hide();
                            break;
                        case 'PENALTY':
                            $exception.hide();
                            $penalty.show();
                            $btnException.addClass("btn btn-outline-dark");
                            $btnPenalty.addClass("btn btn-danger");
                            $btnException.attr('disabled', true);
                            $btnPenalty.attr('disabled', true);

                            $labelHow.hide();
                            $exception.hide();
                            $penalty.show();
                            break;
                        default:
                            $btnException.addClass("btn btn-outline-dark");
                            $btnPenalty.addClass("btn btn-outline-danger");
                            $btnException.attr('disabled', false);
                            $btnPenalty.attr('disabled', false);

                            $labelHow.show();
                            $exception.show();
                            $penalty.show();
                            break;
                    }

                    $('#btnTemp').hide();
                }

                // 신고 접수 설정 함수
                function setReceiptStateType(key, value) {
                    let text = '';
                    let className = '';
                    switch (value) {
                        case 'COMPLETE':
                            text = "신고접수";
                            className = "text-success";
                            break;
                        case 'PENALTY':
                            text = "과태료대상";
                            className = "text-danger";
                            break;
                        case 'EXCEPTION':
                            text = "신고제외";
                            className = "text-dark";
                            break;
                    }
                    $('#' + key + 'Value').text(text);
                    $('#' + key + 'Value').removeClass();
                    $('#' + key + 'Value').addClass("ms-3 fw-bold " + className);

                    setBtnReceiptStateType(value);
                }

                // 신고 접수 설정 초기화 함수
                function initializeReportSetTag(report) {
                    $.each(report, function (key, value) {
                        if (key.indexOf('reportStateType') > -1) {
                            setReceiptStateType(key, value);
                        } else if (key.indexOf("firstFileName") > -1) {
                            $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/" + value));
                        } else if (key.indexOf("secondFileName") > -1) {
                            $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/" + value));
                        } else if (key === 'firstIllegalType') {
                            if (value === 'ILLEGAL') $('#' + key).text("불법주정차 구역");
                            else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차 구역");
                        } else if (key === 'secondIllegalType') {
                            if (value === 'ILLEGAL') $('#' + key).text("불법주정차 구역");
                            else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차 구역");
                        } else if (key === 'note') {
                            if (report.governmentOfficeName === null) {
                                $('#' + key).val("");
                                if (_role !== 'ADMIN') $('#' + key).show();
                                $('#' + key + 'View').hide();
                            } else {
                                $('#' + key).hide();
                                $('#' + key + 'View').html(value);
                                $('#' + key + 'View').show();
                            }
                        } else if (key === 'regDt' || key === 'firstRegDt' || key === 'secondRegDt') {
                            $('#' + key).text(value.replace('T', ' '));
                        } else if (key === 'reportSeq') {
                            $('#reportSeqSetTag').val(value);
                        } else if (key === 'comments') {
                            if ( _role === "ADMIN" ) {
                                let html = '';
                                for (let i = 0; i < value.length; i++) {
                                    html += '<sapn class="ms-2 bg-white"><i class="ms-2 me-2 far fa-comment-dots"></i> ' + value[i] + '<br></sapn>';
                                }
                                $('#' + key).html(html);
                            } else {
                                $('#' + key).hide();
							}
                        } else {
                            $('#' + key).text(value);
                        }
                    });
                }

                // 신고 접수 설정 타이틀 설정 함수
                function initializeReportSetTagTitle(carNum) {
                    $('#reportSetTitle').text(carNum);
                }

                // 초기화 함수
                function initialize() {

                    // 검색 이벤트 1
                    $('#searchStr').next().on('click', function (event) {
                        search();
                    });

                    // 페이지 번호 검색 이벤트
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

                        search(pageNumber);
                    });

                    // 페이지 사이즈 변경 이벤트
                    $('#paginationSize').on("change", function () {
                        $('#pageSize').val($(this).val());
                        search();
                    });

                    // 신고 등록 표시
                    $('#reportTable tbody tr').on('click', function () {

                        let reportSeqStr = $(this).children("td:eq(0)").find('input').val();
                        let carNum = $(this).children("td:eq(3)").text();

                        let reportSeq = Number.parseInt(reportSeqStr);

                        let result = $.JJAjaxAsync({
                            url: _contextPath + '/get',
                            data: {
                                reportSeq: reportSeq
                            }
                        });

                        if (result.success) {
                            let report = result.data;
                            initializeReportSetTag(report);
                        } else {
                            alert("데이터 요청을 실패 하였습니다. ");
                            return;
                        }

                        initializeReportSetTagTitle(carNum);

                        $('#reportMain').hide();
                        $('#reportSet').show();
                    });

                    // 신고 접수 등록 이벤트
                    $('#btnPenalty, #btnException').on('click', function () {

                        if ($(this).is('disabled')) {
                            return;
                        }

                        if (confirm("등록 하시겠습니까?")) {
                            let reportSeqStr = $('#reportSeqSetTag').val();
                            let reportSeq = Number(reportSeqStr);
                            let note = $('#note').val();
                            let reportStateType = $(this).attr('name');

                            $.JJAjaxSync({
                                url: _contextPath + '/set',
                                data: {
                                    userSeq: _userSeq,
                                    reportSeq: reportSeq,
                                    note: note,
                                    reportStateType: reportStateType
                                },
                                success: function (data) {
                                    if (data.success) {
                                        alert("등록 되었습니다.");
                                        search();
                                    } else {
                                        alert("등록 실패 하였습니다.");
                                    }
                                },
                                err: function (code) {
                                    alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                                }
                            });
                        }
                    });

                    // 신고 접수 등록 설정 닫기 이벤트
                    $('#close').on('click', function () {
                        $('#reportMain').show();
                        $('#reportSet').hide();
                    });

                    $.eventFilterTagByButton('reportStateType', search);

                    // 신고 등록 화면 감추기
                    $('#reportSet').hide();

                    // 관리자 일때 신고 접수 기능 감추기
                    if (_role === 'ADMIN') {
                        $('#note').hide();
                        $('#labelHow').hide();
                        $('#btnException').hide();
                        $('#btnPenalty').hide();
                        $('#noteViewContain').css({
                            "height": "auto"
                        });
                    }
                }

                // 초기화
                initialize();

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>