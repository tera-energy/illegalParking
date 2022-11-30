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
<% String contextPath = request.getContextPath(); %>
<%@ page import="com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn" %>
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
		<main id="parkingTable">
			<div class="container-fluid px-4">
				<h1 class="mt-4">주차장목록</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 주차장목록</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						주차장 정보
					</div>
					<div class="card-body">
						<form class="row mb-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-7"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${ParkingFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}" />
							</div>
						</form>
						<table class="table table-hover table-bordered" id="tableList">
							<thead>
							<tr class="table-light">
								<th scope="col" class="text-center" style="width: 5%">#</th>
								<th scope="col" class="text-center" style="width: 20%">주차장명</th>
								<th scope="col" class="text-center" style="width: 7%">요금</th>
								<th scope="col" class="text-center" style="width: 15%">운행요일</th>
								<th scope="col" class="text-center" style="width: 8%">평일시간</th>
								<th scope="col" class="text-center" style="width: 37%">주소</th>
								<th scope="col" class="text-center" style="width: 8%">상세보기</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="parking" items="${parkings}" varStatus="status">
								<tr>
									<td class="text-center" >${parking.parkingSeq}</td>
									<td>${parking.prkplceNm}</td>
									<td class="text-center">${parking.parkingchrgeInfo}</td>
									<td class="text-center">${parking.operDay}</td>
									<td>${parking.weekdayOperOpenHhmm} ~ ${parking.weekdayOperColseHhmm}</td>
									<td>
										<c:choose>
											<c:when test="${ parking.rdnmadr == ''}">${parking.lnmadr}</c:when>
											<c:otherwise>${parking.rdnmadr}</c:otherwise>
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

		<layoutTags:parkingAddTag parkingSeq="1" path="parkingList"/>
	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/parking/parkingList-scripts.js"></script>
		<script type="application/javascript">
            $(function () {

                let parkingSeq ;

                // 검색 함수
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath + "/parkingList?" + $('form').serialize();
                }

                // form 데이터 가져오기 함수
                function getData(){
                    let arr = $('#data').serializeArray();
                    let data = {};
                    $(arr).each(function(index, obj){
                        data[obj.name] = obj.value;
                    });
                    return data;
                }

                // 초기화 함수
                function initialize() {
                    // 검색 이벤트
                    $('#search').on('click', function (event) {
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

                    $('#paginationSize').on("change", function () {
                        $('#pageSize').val($(this).val());
                        search();
                    });

                    // 주차장 정보 테이블 컬럼 이벤트
                    $('#tableList tbody tr').on('click', function () {
                        let parkingSeqStr = $(this).children("td:eq(0)").text();
                        parkingSeq = Number.parseInt(parkingSeqStr);

                        let result = $.JJAjaxAsync({
                            url: _contextPath + '/get',
                            data: {
                                parkingSeq: parkingSeq
                            }
                        });

                        if ( result.success) {
                            $.each(result.data, function (key, value) {
                                $('#' + key).val(value);
                                if (key == "rdnmadr") {
                                    $('#rdnmadr').val(result.lnmadr);
                                }
                            });
                        }

                        $('#parkingTable').hide();
                        $('#parkingAdd').show();
                    });

                    // 수정 이벤트
                    $('#modify').on('click', function (){

                        let data = getData();
                        data.parkingSeq = parkingSeq;

                        if ( confirm("등록 하시겠습니까?") ) {
                            $.JJAjaxSync({
                                url: _contextPath + '/set',
                                data: data,
                                success: function (){
                                    if ( confirm(" 계속 등록 하시겠습니까? " ) ) {
                                        location.href = location.href;
                                    } else {
                                        location.href = _contextPath + '/parkingList';
                                    }
                                } ,
                                err: function (code){
                                    alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                                }
                            });
                        } else {
                            log(getData());
                        }
                    });

                    // 주자창 상세보기 닫기 이벤트
                    $('#close').on('click', function () {
                        $('#parkingTable').show();
                        $('#parkingAdd').hide();
                    });

                    // 주차장 상세보기 화면 숨기기
                    $('#parkingAdd').hide();

                }

                // 초기화
                initialize();

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>