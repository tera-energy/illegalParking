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
<%@ attribute name="productSeq" type="java.lang.Integer" required="false" %>
<%@ attribute name="currentBrand" type="java.lang.String" required="true" %>
<%@ attribute name="brandItems" type="java.lang.Object" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="true" %>

<!-- content -->
<main id="${id}">
	<div class="container-fluid px-4">
		<h1 class="mt-4">결재목록</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > ${title}</li>
		</ol>
		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header"><i class="fas fa-pen me-1"></i> 직접 입력</div>
			<div class="card-body">
				<div class="row">
					<input hidden type="text" id="productSeq" >
					<form id="data">
						<img src="http://49.50.166.205:8090/americano.jpg" id="brandImg" style="width: 300px; height: 300px;">
						<div class="g-3 mb-4">
							<br/>
							<div class="row mb-2">
								<div class="col-4">
									<tags:selectTag id="brand" current="${current}" items="${brandItems}" title="브랜드"/>
								</div>
							</div>
							<div class="row  mb-2">
								<div class="col-4">
									<tags:inputTag id="name" title="제품명"/>
								</div>
							</div>
							<div class="row  mb-2">
								<div class="col-4">
									<tags:inputTag id="pointValue" title="구매포인트"/>
								</div>
							</div>

						</div>

						<c:choose>
							<c:when test="${id.equals('productAddTag')}">
								<div class="btn-group">
									<a id="modify" class="btn btn-primary">수정</a>
									<a id="close" class="btn btn-outline-secondary">닫기</a>
								</div>
							</c:when>
							<c:otherwise>
								<a id="register" class="btn btn-primary">등록</a>
							</c:otherwise>
						</c:choose>

					</form>
				</div>
			</div>
		</div>
	</div>
</main>

<script type="application/javascript">
	$(function (){
        // 브랜드 변경 이벤트 ( 제품 이미지 변경 )
        $('#brand').on('change', function () {
            switch ($(this).val()) {
                case "STARBUGS":
                    $('#brandImg').attr('src', "http://49.50.166.205:8090/americano.jpg");
                    break;
                case "BASKINROBBINS":
                    $('#brandImg').attr('src', "http://49.50.166.205:8090/icecreamCup.png");
                    break;
            }
        });
    });
</script>