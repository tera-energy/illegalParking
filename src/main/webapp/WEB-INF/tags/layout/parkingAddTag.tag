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
<%@ attribute name="parkingSeq" type="java.lang.Integer" required="true" %>
<%@ attribute name="path" type="java.lang.String" required="true" %>

<!-- content -->
<main id="parkingAdd">
	<div class="container-fluid px-4">
		<h1 class="mt-4">주차장등록</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > 주차장등록</li>
		</ol>
		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header"><i class="fas fa-pen me-1"></i> 직접 입력</div>
			<div class="card-body">
				<div class="row">

					<form class="row" id="data">
						<div class="row g-2">
							<div class="col-md-2">
								<tags:inputTag id="prkplceNm" title="* 주차장명" placeholder=""/>
							</div>
							<div class="col-md-1">
								<tags:selectTag id="prkplceSe" current="공영" items="공영,민영" title="* 주차장구분"/>
							</div>
							<div class="col-md-1">
								<tags:inputTag id="prkplceType" title="* 주차장유형" placeholder="예> 노외"/>
							</div>
							<div class="col-md-2">
								<tags:selectTag id="prkplceSe" current="미시행" items="시행,미시행" title="* 부제시행구분"/>
							</div>
							<div class="col-md-2">
								<tags:inputTag id="inputManager" title="* 관리기관명" placeholder="예>전라남도 광양시청"/>
							</div>
						</div>

						<div class="row g-2">
							<div class="col-md-2">
								<tags:selectTag id="inputState" current="평일+토요일+일요일" items="평일+토요일+일요일, 평일+토요일, 평일" title="운영요일"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="startTime" title="평일시작시간" begin="0" end="23" timeFormat="00" current="0:00"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="endTime" title="평일종료시간" begin="0" end="23" timeFormat="59" current="23:59"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="satStartTime" title="토요일시작시간" begin="0" end="23" timeFormat="00" current="0:00"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="satEndTime" title="토요일종료시간" begin="0" end="23" timeFormat="59" current="23:59"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="sunStartTime" title="일요일시작시간" begin="0" end="23" timeFormat="00" current="0:00"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="sunEndTime" title="일요일종료시간" begin="0" end="23" timeFormat="59" current="23:59"/>
							</div>
						</div>

						<div class="row g-2">
							<div class="col-md-1">
								<tags:selectTag id="parkingchrgeInfo" current="무료" items="무료, 유료" title="* 요금"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="basicTime" title="주차기본시간" begin="0" end="5" timeFormat="00" current="1:00"/>
							</div>
							<div class="col-md-2">
								<tags:inputTag id="BasicCharge" title="* 주차기본요금" placeholder="예> 1000원"/>
							</div>
							<div class="col-md-1">
								<tags:selectTagWithTime id="addUnitTime" title="추가단위시간" begin="0" end="5" timeFormat="00" current="1:00"/>
							</div>
							<div class="col-md-2">
								<tags:inputTag id="addUnitCharge" title="추가단위요금" placeholder="예> 1000원"/>
							</div>
							<div class="col-md-2">
								<tags:inputTag id="dayCmmtkt" title="1일주차권요금" placeholder="예> 1000원"/>
							</div>
							<div class="col-md-2">
								<tags:inputTag id="monthCmmtkt" title="월정기권요금" placeholder="예> 1000원"/>
							</div>
						</div>

						<div class="row g-2">
							<div class="col-md-2">
								<tags:inputTag id="phoneNumber" title="전화번호" placeholder="XXX-XXXX"/>
							</div>
						</div>

						<div class="row g-2">
							<div class="col-md-12 mb-2">
								<tags:inputTag id="rdnmadr" title="소재지도로명 주소" placeholder="예> 광주광역시 광산구 쌍암동 694-83번지 1층  "/>
							</div>
							<div class="col-md-2">
								<tags:inputTag id="latitude" title="위도" placeholder="XX.XXXXXXXX" option="disabled"/>
							</div>
							<div class="col-md-2">
								<tags:inputTag id="longitude" title="경도" placeholder="XX.XXXXXXXX" option="disabled"/>
							</div>
						</div>
						<div class="row g-3"></div>
						<c:choose>
							<c:when test="${path.equals('parkingAdd')}">
								<div class="col-1">
									<div class="btn-group">
										<a type="submit" class="btn btn-primary" id="register">등록</a>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="col-2">
									<div class="btn-group">
										<a type="submit" class="btn btn-primary" id="modify">수정</a>
										<a class="btn btn-outline-success" id="close">닫기</a>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</form>
				</div>
			</div>
		</div>

		<c:if test="${path.equals('parkingAdd')}">
			<div class="card shadow-sm rounded">
				<div class="card-header"><i class="fas fa-file-excel me-1"></i> 엑셀 파일 업로드</div>
				<div class="card-body">
					<div class="input-group">
						<input type="file" class="form-control" id="excelFile" aria-describedby="inputGroupFileAddon04" aria-label="Upload" name="uploadFile" accept=".xls,.xlsx">
						<button class="btn btn-primary" type="button" id="excelUpload">등록</button>
					</div>
				</div>
			</div>
		</c:if>

	</div>
</main>