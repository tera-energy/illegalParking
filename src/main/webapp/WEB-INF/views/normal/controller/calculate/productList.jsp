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
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.product.enums.ProductFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.product.enums.Brand" %>
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
		<main id="productTable">
			<div class="container-fluid px-4">
				<h1 class="mt-4">상품목록</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 상품목록</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						상품 정보
					</div>
					<div class="card-body">
						<form class="row mb-3 g-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-7"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${ProductFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}"/>
								<tags:searchTagWithSelect searchStr="${searchStr2}" items="${Brand.values()}" id="searchStr2"/>
							</div>
						</form>
						<table class="table table-hover table-bordered" id="productTable">
							<thead>
							<tr class="table-light">
								<th scope="col" class="text-center" style="width: 5%">#</th>
								<th scope="col" class="text-center" style="width: 25%">브랜드</th>
								<th scope="col" class="text-center" style="width: 37%">상품명</th>
								<th scope="col" class="text-center" style="width: 10%">구매포인트</th>
								<th scope="col" class="text-center" style="width: 15%">등록일자</th>
								<th scope="col" class="text-center" style="width: 8%">상세보기</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="product" items="${products}" varStatus="status">
								<tr>
									<td class="text-center">${product.productSeq}</td>
									<td class="text-center">${product.brand.value}</td>
									<td class="text-center">${product.name}</td>
									<td class="text-end">
										<fmt:formatNumber value="${product.pointValue}" pattern="#,###"/>
									</td>
									<td class="text-center">
										<fmt:parseDate value="${product.regDt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm"/>
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

		<layoutTags:productAddTag id="productAddTag" currentBrand="${Brand.STARBUGS}" brandItems="${Brand.values()}" title="상품목록"/>
	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/calculate/productList-scripts.js"></script>
		<script type="application/javascript">
            $(function () {

                // 데이터 가져오기
                function getDataByProduct(id) {
                    let arr = $('#' + id).serializeArray();
                    let data = {};
                    $(arr).each(function (index, obj) {
                        data[obj.name] = obj.value;
                    });
                    data.userSeq = _userSeq;
                    data.pointValue = Number(data.pointValue);
                    return data;
                }

                // 검색
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath + "/productList?" + $('form').serialize();
                }

                // 검색 입력 방식 선택 함수
                function searchSelect(filterColumn) {
                    if (filterColumn === 'brand') {
                        $('#searchStrGroup').hide();
                        $('#searchStr2Group').show();
                    } else {
                        $('#searchStrGroup').show();
                        $('#searchStr2Group').hide();
                    }
                }

                // 초기화 함수
                function initialize() {

                    // 타입 검색
                    $('#searchStr').next().on('click', function (event) {
                        search();
                    });

                    $('#searchStr2').next().on('click', function (event) {
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

                    // 페이지 사이즈 변경 이벤트
                    $('#paginationSize').on("change", function (){
                        $('#pageSize').val($(this).val());
                        search();
                    });


                    // 제품 테이블 항목 이벤트
                    $('#productTable tbody tr').on('click', function () {

                        let productSeqStr = $(this).children("td:eq(0)").text();
                        productSeq = Number.parseInt(productSeqStr);

                        let result = $.JJAjaxAsync({
                            url: _contextPath + '/product/get',
                            data: {
                                productSeq: productSeq
                            }
                        });

                        if (result.success) {
                            $.each(result.data, function (key, value) {
                                $('#' + key).val(value);
                            });
                        }

                        $('#productSeq').val(productSeq);
                        $('#productTable').hide();
                        $('#productAddTag').show();
                        $('#brand').trigger('change');
                    });

                    // 등록 이벤트
                    $('#register').on('click', function () {
                        if (confirm("등록 하시겠습니까?")) {
                            let data = getDataByProduct('data');
                            data.thumbnail = $('#brandImg').attr('src').split('/').pop();
                            $.JJAjaxSync({
                                url: _contextPath + "/product/set",
                                data: data,
                                success: function () {
                                    if (confirm(" 등록 되었습니다. \n 계속 등록 하시겠습니까? ")) {
                                        location.href = location.href;
                                    } else {
                                        location.href = _contextPath + '/productList';
                                    }
                                },
                                error: function (code) {
                                    alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                                }
                            });
                        }
                    });

                    // 수정 이벤트
                    $('#modify').on('click', function () {
                        if (confirm("수정 하시겠습니까?")) {
                            let data = getDataByProduct('data');
                            data.productSeq = Number($('#productSeq').val());
                            data.thumbnail = $('#brandImg').attr('src').split('/').pop();
                            $.JJAjaxSync({
                                url: _contextPath + "/product/modify",
                                data: data,
                                success: function (ret) {
                                    if (ret.success) {
                                        alert(" 수정 되었습니다.");
                                        location.href = location.href;
                                    } else {
                                        alert("등록 실패 하였습니다.");
                                    }
                                },
                                error: function (code) {
                                    alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                                }
                            });
                        }
                    });

                    // 닫기 이벤트
                    $('#close').on('click', function () {
                        $('#productTable').show();
                        $('#productAddTag').hide();
                    });

                    //
                    $('#filterColumn').find('select[name="filterColumn"]').on('change', function () {
                        $.searchSelect($(this).val());
                    });

                    searchSelect('${filterColumn}');

                    $('#productAddTag').hide();

                }

                initialize();

            })

		</script>
	</stripes:layout-component>

</stripes:layout-render>