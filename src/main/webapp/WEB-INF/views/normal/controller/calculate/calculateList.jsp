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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateFilterColumn" %>
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
		<main>
			<div class="container-fluid px-4">
				<h1 class="mt-4">결재목록</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 결재목록</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						결재 정보
					</div>
					<div class="card-body">
						<form class="row mb-3 g-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-7"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${CalculateFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}" />
							</div>
						</form>
						<table class="table table-hover table-bordered">
							<thead>
							<tr class="table-light">
								<th scope="col" class="text-center" style="width: 5%">#</th>
								<th scope="col" class="text-center" style="width: 7%">사용자</th>
								<th scope="col" class="text-center" style="width: 8%">추가포인트</th>
								<th scope="col" class="text-center" style="width: 27%">신고</th>
								<th scope="col" class="text-center" style="width: 8%">사용포인트</th>
								<th scope="col" class="text-center" style="width: 20%">상품</th>
								<th scope="col" class="text-center" style="width: 10%">현재포인트</th>
								<th scope="col" class="text-center" style="width: 15%">일자</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="calculate" items="${calculates}" varStatus="status">
								<tr>
									<td class="text-center">${calculate.calculateSeq}</td>
									<td class="text-center">${calculate.userName}</td>
									<c:choose>
										<c:when test="${calculate.pointType == 'PLUS'}">
											<td class="text-end text-primary">
												<fmt:formatNumber value="${calculate.eventPointValue}" pattern="#,###" />
											</td>
											<td class="text-center">${calculate.locationType.value} (으)로부터 포상금이 제공 되었습니다. </td>
											<td></td>
											<td></td>
										</c:when>
										<c:otherwise>
											<td></td>
											<td class="text-center">-</td>
											<td class="text-end text-danger">
												<fmt:formatNumber value="${calculate.eventPointValue}" pattern="#,###" />
											</td>
											<td class="text-center">${calculate.productName}</td>
										</c:otherwise>
									</c:choose>
									<td class="text-end">
										<fmt:formatNumber value="${calculate.currentPointValue}" pattern="#,###" />
									</td>
									<td class="text-center">
										<fmt:parseDate value="${calculate.regDt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm"/>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" offsetPage="${offsetPage}" totalPages="${totalPages}" items="10,25,50" pageSize="${pageSize}"/>
					</div>
				</div>
			</div>
		</main>
	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/calculate/calculateList-scripts.js"></script>
		<script type="application/javascript">
            $(function (){

                // 검색 함수
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath  + "/calculateList?" + $('form').serialize();
                }

                // 초기화 함수
                function initialize() {

                    $('#search').on('click', function (event) {
                        search();
                    });

                    $('#pagination').find("li").on('click', function () {
                        let ul = $(this).parent();
                        let totalSize = ul.children("li").length;
                        if (totalSize <= 3) {
                            return;
                        }
                        let pageNumber;
                        if ($(this).text() === "<") {
                            pageNumber = Number.parseInt(ul.children('.active').text());
                            if ( pageNumber == 1) return;
                            pageNumber = pageNumber - 1;

                        } else if ($(this).text() === ">") {
                            pageNumber = Number.parseInt(ul.children('.active').text());
                            let myLocation = $(this).index();
                            let activeLocation = ul.children('.active').index();
                            if ( activeLocation == (myLocation-1) ) {
                                return;
                            }
                            pageNumber = pageNumber + 1;
                        } else {
                            pageNumber = Number.parseInt($(this).text());
                        }

                        search(pageNumber);
                    });

                    $('#paginationSize').on("change", function (){
                        $('#pageSize').val($(this).val());
                        search();
                    });

                }

                // 초기화 실행
                initialize();

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>