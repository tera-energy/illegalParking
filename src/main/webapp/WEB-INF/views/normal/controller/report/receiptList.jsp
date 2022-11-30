<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
  신고 목록
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType" %>

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
				<h1 class="mt-4">신고목록</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 신고목록</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						불법 주정차 신고 정보
					</div>
					<div class="card-body">
						<form class="row mb-3 g-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-3">
								<tags:filterTagByButton id="receiptStateType" current="${receiptStateType}" enumValues="${ReceiptStateType.values()}"/>
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
							<tr class="table-light">
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
							<c:forEach var="receipt" items="${receipts}" varStatus="status">
								<tr>
									<td class="text-center" >
										<input type="hidden" value="${receipt.receiptSeq}" id="reportSeq">
										${receipt.name}
									</td class="text-center">
									<td class="text-center">${receipt.carNum}</td>
									<td>${receipt.addr}</td>
									<td class="text-center" >
										<fmt:parseDate value="${receipt.regDt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td class="text-end" >${receipt.overlapCount}</td>
									<td class="text-center" >
										<c:choose>
											<c:when test="${receipt.receiptStateType == 'EXCEPTION'}"><span class="text-danger fw-bold">${receipt.receiptStateType.value}</span></c:when>
											<c:when test="${receipt.receiptStateType == 'FORGET'}"><span class="text-dark fw-bold">${receipt.receiptStateType.value}</span></c:when>
											<c:otherwise><span class="text-success fw-bold">${receipt.receiptStateType.value}</span></c:otherwise>
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

		<layoutTags:receiptSetTag/>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/report/receiptList-scripts.js"></script>
		<script type="application/javascript">
			$(function (){

                // 검색 함수
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath + "/receiptList?" + $('form').serialize();
                }

                // 상세 페이지 타이틀 설정 함수
                function initializeReportSetTagTitle(carNum) {
                    $('#reportSetTitle').text(carNum);
                }

                // 신고 목록 초기화 함수
                function initializeReceiptSetTag(report) {
                    $.each(report, function (key, value) {
                        if (key.indexOf('receiptStateType') > -1) {
                            if (value === 'OCCUR') {
                                $('#' + key + 'View').text("신고발생");
                            } else if (value === 'FORGET')  {
                                $('#' + key + 'View').text("신고종료");
                            } else if (value === 'EXCEPTION') {
                                $('#' + key + 'View').text("신고제외");
                            }
                        } else if (key.indexOf("firstFileName") > -1) {
                            if ( value !== null) $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/" + value));
                        } else if (key.indexOf("secondFileName") > -1) {
                            if ( value !== null) $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/" + value));
                        } else if (key === 'firstIllegalType') {
                            if (value === 'ILLEGAL') $('#' + key).text("불법주정차 구역");
                            else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차 구역");
                        } else if (key === 'secondIllegalType') {
                            if (value === 'ILLEGAL') $('#' + key).text("불법주정차 구역");
                            else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차 구역");
                        } else if( key === 'regDt' || key === 'firstRegDt' || key === 'secondRegDt' ) {
                            $('#' + key).text(value == null ? '' : value.replace('T', ' '));
                        } else if ( key === 'comments') {
                            let  html = '';
                            for ( let i =0; value.length > i; i++) {
                                html += '<sapn class="ms-2 bg-white"><i class="ms-2 me-2 far fa-comment-dots"></i> ' + value[i] + '<br></sapn>';
                            }
                            $('#' + key).html(html);
                        } else {
                            $('#' + key).text(value);
                        }
                    });

                }

				// 초기화 함수
                function initialize() {

                    // 검색 이벤트
                    $('#searchStr').next().on('click', function (event) {
                        search();
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

                        search(pageNumber);
                    });

                    // 페이지 개수 변경 이벤트
                    $('#paginationSize').on("change", function () {
                        $('#pageSize').val($(this).val());
                        search();
                    });

                    // 신고 등록 상세보기 표시 이벤트
                    $('#reportTable tbody tr').on('click', function () {

                        let reportSeqStr = $(this).children("td:eq(0)").find('input').val();
                        let carNum = $(this).children("td:eq(1)").text();
                        let receiptSeq = Number.parseInt(reportSeqStr);

                        let result = $.JJAjaxAsync({
                            url: _contextPath + '/receipt/get',
                            data: {
                                receiptSeq: receiptSeq
                            }
                        });

                        if (result.success) {
                            let receipt = result.data;
                            initializeReceiptSetTag(receipt);
                        } else {
                            alert("데이터 요청을 실패 하였습니다. ");
                            return;
                        }

                        initializeReportSetTagTitle(carNum);

                        $('#reportMain').hide();
                        $('#reportSet').show();
                    });

                    // 상세보기 닫기 이벤트
                    $('#close').on('click', function () {

                        initializeReceiptSetTag({
                            "receiptSeq": '',
                            "name": '',
                            "carNum": '',
                            "overlapCount": '',
                            "regDt": '',
                            "addr": '',
                            "receiptStateType": '',
                            "firstFileName": null,
                            "firstRegDt": '',
                            "firstAddr": '',
                            "firstIllegalType": '',
                            "secondFileName": null,
                            "secondRegDt": '',
                            "secondAddr": '',
                            "secondIllegalType": '',
                            "comments": [
                                ''
                            ],
                            "governmentOfficeName": ''
						});

                        $('#reportMain').show();
                        $('#reportSet').hide();
                    });

                    // 신고 등록 화면 감추기 이벤트
                    $('#reportSet').hide();

                    // 신고접수 / 신고제외 / 과태료대상 버튼 이벤트 설정 이벤트
                    $.eventFilterTagByButton('receiptStateType', search);

                }

                // 초기화
                initialize();

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>