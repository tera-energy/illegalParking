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
				<h1 class="mt-4">지도 보기</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active"> ${subTitle} > 지도 보기</li>
				</ol>

				<div class="row">

					<div class="card">
						<div class="card-title">
							<div class="row mt-2 ms-2 p-0">
								<div class="col-6 mt-2 align-middle">
									<input class="form-check-input" type="radio" name="searchIllegalType" id="type_all" value="" checked>
									<label class="form-check-label" for="type_all">전체</label>
									<c:forEach items="${IllegalType.values()}" var="type">
										<input class="form-check-input" type="radio" name="searchIllegalType" value="${type}" id="type_${type}">
										<label class="form-check-label" for="type_${type}">${type.value}</label>
									</c:forEach>
								</div>
								<div class="col-6 d-flex justify-content-end">
									<div class="me-3">
										<a class="btn btn-sm btn-outline-success" id="btnAddOverlay">구역추가</a>
										<a class="btn btn-sm btn-outline-dark" id="btnModifyOverlay">구역수정</a>
										<a class="btn btn-sm btn-outline-primary display-none" id="btnSet">저장</a>
										<a class="btn btn-sm btn-outline-danger display-none" id="btnModify">수정</a>
										<a class="btn btn-sm btn-outline-warning display-none" id="btnCancel">취소</a>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body mt-0 pt-0">
								<%--							<div class="map_wrap">--%>
							<div id="drawingMap"></div>
								<%--							</div>--%>
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
		<script src="<%=contextPath%>/resources/js/mapCommon-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/mapSet-scripts.js"></script>
		<script type="text/javascript">
            // 구역 Event Time 적용시간 켜기 / 끄기 함수
            function setTime(_this) {
                let id = _this.id.substring(4).toLowerCase();
                if ($('#' + _this.id).is(':checked')) {
                    $('#' + id + 'StartTimeHour').attr('disabled', false);
                    $('#' + id + 'StartTimeMinute').attr('disabled', false);
                    $('#' + id + 'EndTimeHour').attr('disabled', false);
                    $('#' + id + 'EndTimeMinute').attr('disabled', false);
                } else {
                    $('#' + id + 'StartTimeHour').attr('disabled', true);
                    $('#' + id + 'StartTimeMinute').attr('disabled', true);
                    $('#' + id + 'EndTimeHour').attr('disabled', true);
                    $('#' + id + 'EndTimeMinute').attr('disabled', true);
                }
            }

            $(function () {

                // 주정차 별 구역 조회
                $('input:radio[name=searchIllegalType]').change(async function () {
                    $('#areaSettingModal').offcanvas('hide');
                    if ($.getManagerPolygonsLength() > 0) {
                        if (confirm("저장하지 않은 구역은 삭제됩니다. 검색하시겠습니까?")) {
                            $.removeOverlaysOfManager();
                        } else {
                            $('#type_all').prop('checked', true);
                            return false;
                        }
                    }
                    if (this.value === '') {
                        $('#btnCancel').trigger('click');
                    } else {
                        $('#btnAddOverlay').hide();
                        $('#btnModifyOverlay').hide();
                        $('#btnModify').hide();
                        $('#btnSet').hide();
                        $('#btnCancel').hide();
                    }

                    await $.drawingZoneWithCodes();
                });

                $('#usedFirst').change(function () {
                    setTime(this);
                });

                $('#usedSecond').change(function () {
                    setTime(this);
                });

                //
                $('#btnAddOverlay').click(function () {
                    $.SetMaxLevel($.LEVEL_THREE);
                    $.isModifyArea = false;
                    $.setOverlayType('POLYGON');
                    $(this).hide();
                    $('#btnSet').show();
                    $('#btnCancel').show();
                    $('#btnModify').hide();

                    $('#btnModifyOverlay').hide();
                });

                //
                $('#btnModifyOverlay').click(function () {
                    $.SetMaxLevel($.LEVEL_THREE);
                    $.isModifyArea = true;
                    $(this).hide();

                    $('#btnModify').show();
                    $('#btnCancel').show();
                    $('#btnSet').hide();

                    $('#btnAddOverlay').hide();
                });

                $('#btnCancel').click(function () {
                    if ($.isModifyArea) {
                        $.undoManager();
                    } else {
                        $.cancelDrawing();
                        $.removeOverlaysOfManager();
                    }
                    $.initBtnState();
                });

                $.initBtnState = function () {
                    $.SetMaxLevel($.LEVEL_TEN);
                    $.isModifyArea = false;
                    $('#btnModify').hide();
                    $('#btnCancel').hide();
                    $('#btnSet').hide();

                    $('#btnAddOverlay').show();
                    $('#btnModifyOverlay').show();
                }

                // test .......
                $('#btnSet').click(async function () {
                    let opt = $.getManagerData('set');
					log(opt);
                });

                // 구역 저장 함수
                $('#btnSet').click(async function () {
                	let opt = $.getManagerData('set');
                	if (opt.data.polygon.length === 0) {
                		alert('구역을 지정하시기 바랍니다.');
                		return false;
                	} else {
                		$.initBtnState();

                		// 폴리곤 중심좌표를 구해서 법정동 코드 넣기
                		for (const polygon of opt.data.polygon) {
                			let points = polygon.points;
                			let centroidPoints = centroid(points);
                			// polygon.code = await coordinatesToDongCodeKakaoApi(centroidPoints.x, centroidPoints.y);
                			polygon.code = await coordinatesToDongCodeKakaoApi(centroidPoints.x, centroidPoints.y);
                		}
                		// 데이터 저장
                		let result = $.JJAjaxAsync(opt);
                		if(result.success) {
                			// 지도에 가져온 데이터로 도형들을 그립니다
                			await $.drawingZoneWithCodes();
                			// 생성한 폴리곤 삭제
                			$.removeOverlaysOfManager();
                		}
                	}
                });

                // 구역 수정 함수
                $('#btnModify').click(async function () {

                	let opt = $.getManagerData('modify');

                	let managerPolygon = opt.data.polygon;
                	if (managerPolygon === undefined) {
                		alert('구역을 지정하시기 바랍니다.');
                		return false;
                	} else {
                		if (confirm("수정하시겠습니까?")) {
                			let polygonObj = $.newPolygon(managerPolygon)

                			let points = managerPolygon.points;
                			let centroidPoints = centroid(points);
                			managerPolygon.code = await coordinatesToDongCodeKakaoApi(
                					centroidPoints.x, centroidPoints.y);
                			let result = $.JJAjaxAsync(opt);

                			if (result.success) {
                				$.isDrawPolygonAfterEvent = false;
                				$.removeOverlaysOfManager();
                				polygonObj.polygon.setMap(null);

                				managerPolygon.options = polygonStyle;
                				managerPolygon.type = result.data;
                				managerPolygon.seq = $.clickedPolygon.area.seq;

                				$.displayArea(managerPolygon);
                			}
                		}
                	}
                });

                //폴리곤 삭제 함수
                $('#btnRemove').click(function () {
                    if (confirm("삭제하시겠습니까?")) {
                        let opt = {
                            url: _contextPath + "/zone/remove",
                            data: {
                                'zoneSeq': $('#zoneSeq').val()
                            }
                        };
                        let result = $.JJAjaxAsync(opt);

                        if (result.success) {
                            $.clickedPolygon.clickPolygon.setMap(null);
                        }

                        $('#areaSettingModal').offcanvas('hide');
                        alert("삭제되었습니다.");
                    }
                });

                // 구역 이벤트 설정
                $('#btnModifyEvent').click(function () {
                    if (confirm("설정하시겠습니까?")) {
                        let form = $('#formAreaSetting').serializeObject();
                        form['usedFirst'] = !$('#usedFirst').is(':checked');
                        form['usedSecond'] = !$('#usedSecond').is(':checked');
                        form['firstStartTime'] = $('#firstStartTimeHour').val() + ':' + $('#firstStartTimeMinute').val();
                        form['firstEndTime'] = $('#firstEndTimeHour').val() + ':' + $('#firstEndTimeMinute').val();
                        form['secondStartTime'] = $('#secondStartTimeHour').val() + ':' + $('#secondStartTimeMinute').val();
                        form['secondEndTime'] = $('#secondEndTimeHour').val() + ':' + $('#secondEndTimeMinute').val();

                        let result = $.JJAjaxAsync({
                            url: _contextPath + '/event/addAndModify',
                            data: form
                        });

                        if (result.success) {
                            $.clickedPolygon.clickPolygon.setMap(null);
                            $.beforeClickPolygon = undefined;
                            $.clickedPolygon.clickPolygon.setOptions({
                                strokeWeight: 0,
                            });
                            $.clickedPolygon.area.type = form['illegalType'];
                            $.displayArea($.clickedPolygon.area);
                            $('#areaSettingModal').offcanvas('hide');
                            alert("설정되었습니다.");
                            $.SetMaxLevel($.LEVEL_TEN);
                        } else {
                            alert(result.msg);
                        }
                    }
                });

            });

		</script>
	</stripes:layout-component>

</stripes:layout-render>