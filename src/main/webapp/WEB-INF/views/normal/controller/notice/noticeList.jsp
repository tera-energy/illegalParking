<%--
  Created by IntelliJ IDEA.
  User:
  Date: 2022-10-17
  Time: 오전 8:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<% String contextPath = request.getContextPath(); %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.notice.enums.NoticeFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.notice.enums.NoticeType" %>
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
		<main id="noticeMain">
			<div class="container-fluid px-4">
				<h1 class="mt-4">공지사항</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 공지사항</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						공지 사항 정보
					</div>
					<div class="card-body">
						<form class="row mb-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-7 d-flex justify-content-lg-start">
								<a class="btn btn-primary" id="write">
									<i class="fas fa-edit me-1"></i> 글작성
								</a>
							</div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${NoticeFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}"/>
								<tags:searchTagWithSelect id="searchStr2" searchStr="${searchStr2}" items="${NoticeType.values()}"/>
							</div>
						</form>
						<table class="table table-hover table-bordered" id="noticeTable">
							<thead>
							<tr class="table-light">
								<th scope="col" class="text-center" style="width: 5%;">분류</th>
								<th scope="col" class="text-center" style="width: 20%;">제목</th>
								<th scope="col" class="text-center" style="width: 50%;">내 용</th>
								<th scope="col" class="text-center" style="width: 10%;">등록일</th>
								<th scope="col" class="text-center" style="width: 7%;">등록자</th>
								<th scope="col" class="text-center" style="width: 8%;">상세보기</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${notices}" var="notice" varStatus="status">
								<tr seq="${notice.noticeSeq}">
									<td class="text-center align-middle">
										<input type="hidden" value="${notice.noticeType}">
										<c:choose>
											<c:when test="${notice.noticeType == 'DISTRIBUTION'}"><span class="fw-bold text-primary">${notice.noticeType.value}</span></c:when>
											<c:otherwise><span class="fw-bold">${notice.noticeType.value}</span></c:otherwise>
										</c:choose>

									</td>
									<td class="text-center align-middle">
										<input type="hidden" value="${notice.subject}">
										<c:choose>
											<c:when test="${fn:length(notice.subject) gt 25}">
												<c:out value="${fn:substring(notice.subject, 0, 24)}"></c:out>...
											</c:when>
											<c:otherwise>
												<c:out value="${notice.subject}"></c:out>
											</c:otherwise>
										</c:choose>
									</td>
									<td colspan=" align-middle">
										<input type="hidden" value="${notice.html}">
										<c:choose>
											<c:when test="${fn:length(notice.content) gt 55}">
												<c:out value="${fn:substring(notice.content, 0, 54)}"></c:out>...
											</c:when>
											<c:otherwise>
												<c:out value="${notice.content}"></c:out>
											</c:otherwise>
										</c:choose>
									</td>
									<td class="text-center align-middle">
										<fmt:parseDate value="${notice.regDt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm"/>
									</td>
									<td class="text-center align-middle">${notice.user.name}</td>
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

		<layoutTags:noticeSetTag items="${NoticeType.values()}"/>

		<layoutTags:noticeViewTag/>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/notice/noticeList-scripts.js"></script>
		<script type="application/javascript">
            $(function () {

                // 검색 입력 방식 선택 함수
                function searchSelect(filterColumn) {
                    if (filterColumn === 'NOTICETYPE') {
                        $('#searchStrGroup').hide();
                        $('#searchStr2Group').show();
                    } else {
                        $('#searchStrGroup').show();
                        $('#searchStr2Group').hide();
                    }
                }

                // 초기화
                function initialize() {

                    // 검색 이벤트 1
                    $('#searchStr').next().on('click', function (event) {
                        $.search();
                    });

                    // 검색 이벤트 2
                    $('#searchStr2').next().on('click', function (event) {
                        $.search();
                    });

                    // 필터 변경 이벤트
                    $('#filterColumn').find('select').on('change', function () {
                        searchSelect($(this).val());
                    });

                    // 글작성 열기 이벤트
                    $('#write').on('click', function () {

                        $.openNoticeSet();
                    });

                    // 공지 사항 테이블 항목 이벤트
                    $('#noticeTable tbody tr').on('click', function () {

                        let data = {
                            noticeSeq: $(this).attr('seq'),
                            noticeType: $(this).children("td:eq(0)").find('input').val(),
                            noticeTypeValue: $(this).children("td:eq(0)").text().trim(),
                            subject: $(this).children("td:eq(1)").find('input').val(),
                            html: $(this).children("td:eq(2)").find('input').val(),
                            regDt: $(this).children("td:eq(3)").text().trim()
                        }

                        $.initializeNoticeView(data)

                        $.openNoticeView();
                    });

                    // 페이지 번호 이벤트
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

                    // 필터에 의한 검색 입력 방식 선택
                    searchSelect('${filterColumn}');

                    // 글작성 / 글보기 숨김
                    $('#noticeSet').hide();
                    $('#noticeView').hide();
                }

                // 초기화
                initialize();

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>