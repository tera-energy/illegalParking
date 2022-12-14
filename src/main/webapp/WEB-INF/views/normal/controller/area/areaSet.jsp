<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layoutTags" tagdir="/WEB-INF/tags/layout" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/navMapHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<stripes:layout-component name="side">
		<jsp:include page="side.jsp" flush="true"/>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="container-fluid px-4">
				<h1 class="mt-4">구역 설정</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active"> ${subTitle} > 구역 설정</li>
				</ol>

				<div class="row">

					<div class="card">
						<div class="card-title">
							<div class="row mt-2 ms-2 p-0">
								<div class="col-6 mt-2 align-middle">
									<div id="searchIllegalTypeWrap">
										<input class="form-check-input" type="radio" name="searchIllegalType" id="type_all" value="" checked>
										<label class="form-check-label" for="type_all">전체</label>
										<c:forEach items="${IllegalType.values()}" var="type">
											<input class="form-check-input" type="radio" name="searchIllegalType" value="${type}" id="type_${type}">
											<label class="form-check-label" for="type_${type}">${type.value}</label>
										</c:forEach>
									</div>
								</div>
								<div class="col-6 d-flex justify-content-end">
									<div class="me-3">
										<a class="btn btn-sm btn-outline-success" id="btnAddArea">구역추가</a>
										<a class="btn btn-sm btn-outline-dark" id="btnModifyArea">구역수정</a>
										<a class="btn btn-sm btn-outline-primary" id="btnSet">저장</a>
										<a class="btn btn-sm btn-outline-danger" id="btnModify">수정</a>
										<a class="btn btn-sm btn-outline-warning" id="btnCancel">취소</a>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body mt-0 pt-0">
							<div id="drawMap"></div>
						</div>
					</div>

					<p class="text-danger fs-10 m-0 mt-1 mb-1" id="postscript"><i class="fas fa-exclamation-triangle"></i> 20m : 1레벨, 50m : 3레벨, 3레벨 이하 구역이 표시. 현재 [ <span class="text-primary fw-bold" id="mapLevel">3레벨</span> ]</p>

					<layoutTags:mapSetModalTag id="areaSettingModal" typeValues="${IllegalType.values()}" enumValues="${LocationType.values()}" current=""/>

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
		<script src="<%=contextPath%>/resources/js/map-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/areaSet-scripts.js"></script>
		<script type="text/javascript">
            // 구역 Event Time 적용시간 켜기 / 끄기 함수
            function setTimeSelectDisabled(_this) {
                const usedType = _this.id.substring(4).toLowerCase();
                const $timeTags = [
                    $('#' + usedType + 'StartTimeHour'),
                    $('#' + usedType + 'StartTimeMinute'),
                    $('#' + usedType + 'EndTimeHour'),
                    $('#' + usedType + 'EndTimeMinute')
                ];

                for (const $timeTag of $timeTags) {
                    if ($('#' + _this.id).is(':checked')) {
                        $timeTag.attr('disabled', false);
                    } else {
                        $timeTag.attr('disabled', true);
                    }
                }
            }

            //폴리곤의 중심좌표를 구하고 그 좌표로 동 코드 가져오기
            $.getCodeByCenterPointsOfPolygon = async function(points) {
                let centerPoints = $.centroid(points);
                return await $.async_coordinatesToDongCodeKakaoApi(centerPoints.x, centerPoints.y);
            }

            $(function () {
                const $btnAddArea = $('#btnAddArea');
                const $btnModifyArea = $('#btnModifyArea');
                const $btnSet = $('#btnSet');
                const $btnModify = $('#btnModify');
                const $btnCancel = $('#btnCancel');

                // kakao 초기화
                $.initializeKakao();

                //
                $.getCurrentPosition($.drawingMap);

                $.initBtnState = {
                    set: "set",
                    modify: "modify",
                    init: "init",
                    func: function (mode) {

                        switch (mode) {
                            case $.initBtnState.set:
                                $.setOverlayType('POLYGON');
                                $.SetMaxLevel($.MAP_MIN_LEVEL);
                                $.isModifyArea = false;
                                $btnSet.show();
                                $btnCancel.show();
                                $btnModifyArea.hide();
                                $btnAddArea.hide();
                                $btnModify.hide();
                                break;
                            case $.initBtnState.modify:
                                $.SetMaxLevel($.MAP_MIN_LEVEL);
                                $.isModifyArea = true;
                                $btnSet.hide();
                                $btnCancel.show();
                                $btnModifyArea.hide();
                                $btnAddArea.hide();
                                $btnModify.show();
                                break;
                            case $.initBtnState.init:
                                $.SetMaxLevel($.MAP_MAX_LEVEL);
                                $.isModifyArea = false;
                                $btnSet.hide();
                                $btnCancel.hide();
                                $btnModifyArea.show();
                                $btnAddArea.show();
                                $btnModify.hide();
                                break;
                        }

                    }
                };

                // 불법주정차 구역 선택 이벤트 설정
                $('input:radio[name=searchIllegalType]').on('change', async function () {
                    if ($.getManagerPolygonsLength() > 0) {
                        if (confirm("저장하지 않은 구역은 삭제됩니다. \n검색하시겠습니까?")) {
                            $.removePolygonOfManager();
                        } else {
                            $('#type_all').prop('checked', true);
                            return false;
                        }
                    }

                    $('#areaSettingModal').offcanvas('hide');
                    $.changeOptionStroke();
                    $.cancelDrawing();

                    if ($(this).val() === '') {
                        $.initBtnState.func($.initBtnState.init);
                    } else {
                        $btnAddArea.hide();
                        $btnModifyArea.hide();
                        $btnModify.hide();
                        $btnSet.hide();
                        $btnCancel.hide();
                    }

                    await $.initializePolygon((await $.getDongCodesBounds($.drawingMap)).codes);
                });

                $('#usedFirst').on('change', function () {
                    setTimeSelectDisabled(this);
                });

                $('#usedSecond').on('change', function () {
                    setTimeSelectDisabled(this);
                });

                // 구역 추가 버튼 이벤트
                $btnAddArea.on('click', function () {
                    $.initBtnState.func($.initBtnState.set);
                });

                // 구역 저장 버튼 이벤트
                $btnSet.on('click', async function () {
                    let opt = $.getManagerData('set');

                    if (opt.data.polygon.length <= 0) {
                        alert('구역을 지정하시기 바랍니다.');
                        return false;
                    } else {
                        // 폴리곤 중심좌표를 구해서 법정동 코드 넣기
                        for (const polygon of opt.data.polygon) {
                            let points = polygon.points;
                            if (points.length < 3) {
                                alert('구역지정은 3개 이상의 좌표가 있어야합니다.')
                                return;
                            }
                            ;
                            polygon.code = await getCodeByCenterPointsOfPolygon(points);

                        }
                        // 데이터 저장
                        let result = $.JJAjaxAsync(opt);
                        if (result.success) {
                            $.initBtnState.func($.initBtnState.init);
                            // 지도에 가져온 데이터로 도형들을 그립니다
                            await $.initializePolygon((await $.getDongCodesBounds($.drawingMap)).codes);
                            // 생성한 폴리곤 삭제
                            $.removePolygonOfManager();
                        }
                    }
                });

                // 구역 수정 버튼 이벤트
                $btnModifyArea.on('click', function () {
                    $.initBtnState.func($.initBtnState.modify);
                });

                // 구역 수정 함수
                $btnModify.on('click', async function () {
                    const opt = $.getManagerData('modify');
                    let managerPolygon = opt.data.polygon;
                    if (managerPolygon.length <= 0) {
                        alert('구역을 지정하시기 바랍니다.');
                        return false;
                    } else {
                        if (confirm("수정하시겠습니까?")) {
                            managerPolygon.code = await getCodeByCenterPointsOfPolygon(managerPolygon.points);
                            const result = $.JJAjaxAsync(opt);

                            if (result.success) {

                                $.isDrawPolygonAfterEvent = false;
                                $.removePolygonOfManager();

                                managerPolygon.options = polygonStyle;
                                managerPolygon.type = result.data.illegalType;
                                managerPolygon.seq = result.data.zoneSeq;

                                $.displayPolygon(managerPolygon);
                            }
                        }
                    }
                });

                $btnCancel.click(function () {
                    if ($.isModifyArea) {
                        $.undoManager();
                    } else {
                        $.cancelDrawing();
                        $.removePolygonOfManager();
                    }
                    $.initBtnState.func($.initBtnState.init);
                });

                //폴리곤 삭제 함수
                $('#btnRemove').on('click', function () {
                    if (confirm("삭제하시겠습니까?")) {
                        let opt = {
                            url: _contextPath + "/zone/remove",
                            data: {
                                'zoneSeq': $('#zoneSeq').val()
                            }
                        };
                        let result = $.JJAjaxAsync(opt);

                        if (result.success) {
                            $.clickedPolygon.setMap(null);
                        }

                        $('#areaSettingModal').offcanvas('hide');
                        alert("삭제되었습니다.");
                    }

                });

                $('#searchIllegalTypeWrap').hide();
                $btnSet.hide();
                $btnModify.hide();
                $btnCancel.hide();
            });

		</script>
	</stripes:layout-component>

</stripes:layout-render>