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
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/mobileMapHtmlLayout.jsp">
	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="row">
				<div class="map_wrap">
					<div id="map"></div>

                    <div id="onOffBtn" class="on-off-toggle-btn">
                        <input type="checkbox" id="toggle" hidden>

                        <label for="toggle" class="toggleSwitch active">
                            <span class="toggleButton"></span>
                        </label>
                    </div>

					<div class="map-control border border-2 bg-white shadow-sm btn-group rounded-pill" role="group">
						<label for="zone" class="mapType btn btn-dark rounded-pill fw-bold"><input type="radio" class="btn-check" name="mapSelect" id="zone" autocomplete="off" checked>불법주차</label>
						<label for="parking" class="mapType btn btn-white fw-bold"><input type="radio" class="btn-check" name="mapSelect" id="parking" autocomplete="off">주차장</label>
						<label for="pm" class="mapType btn btn-white fw-bold"><input type="radio" class="btn-check" name="mapSelect" id="pm" autocomplete="off">모빌리티</label>
					</div>

					<div id="option" class="option-btn">
                        <img src="<%=contextPath%>/resources/assets/img/option.png" alt="옵션슬라이드">
                    </div>

					<div class="notice-board border border-2 bg-white shadow-sm btn-group rounded-3" role="group">
                        <div class="col">
                            <div class="row">
                                <div class="col-2">
                                    <span class="fs-10 fw-bolder py-1"><i class="bi bi-circle-fill px-2" style="color: #FFC527"></i></span>
                                </div>
                                <div class="col-8">
                                    <span class="fs-10 fw-bolder py-1">정차가능</span>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-2">
                                    <span class="fs-10 fw-bolder py-1"><i class="bi bi-circle-fill px-2" style="color: #FF9443"></i></span>
                                </div>
                                <div class="col-9 test1">
                                    <p class="fs-10 fw-bolder">주정차금지</p>
                                    <p class="fs-10 fw-bolder">(탄력허용)</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-2">
                                    <span class="fs-10 fw-bolder py-1"><i class="bi bi-circle-fill px-2" style="color: #E63636"></i></span>
                                </div>
                                <div class="col-8">
                                    <span class="fs-10 fw-bolder py-1">주정차금지</span>
                                </div>

                            </div>
                        </div>
					</div>

					<!-- 지도 확대, 축소 컨트롤 div 입니다 -->
					<div class="custom-control">
						<span id="btnFindMe"><img src="<%=contextPath%>/resources/assets/img/ping.png" alt="내위치"></span>
						<span id="zoomIn"><img src="<%=contextPath%>/resources/assets/img/plus.png" alt="확대"></span>
						<span id="zoomOut"><img src="<%=contextPath%>/resources/assets/img/minus.png" alt="축소"></span>
					</div>

					<div class="msg-bar rounded-pill" id="msgBar">
						<p class="fw-semibold">GPS 설정중...</p>
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
		<script src="<%=contextPath%>/resources/js/mapCommon-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/map-scripts.js"></script>
		<script type="application/javascript">

			let tempLat1 = 0;
            let tempLng1 = 0;

			let tempLat2 = 0;
            let tempLng2 = 0;

			let tempLat3 = 0;
            let tempLng3 = 0;

            let oldBearing = 0;

            function averageXY(oldLat, oldLng, newLat, newLng) {
                if ( oldLat === 0 ) {
                    oldLat = newLat;
				}

                if ( oldLng === 0) {
                    oldLng = newLng;
				}

                return {
                    lat: (oldLat + newLat) / 2,
					lng : (oldLng + newLng) / 2
				}
			}

            function getAngleByCalibration() {

                if (tempLng2 !== 0) {
                    tempLat1 = tempLat2;
                    tempLng1 = tempLng2;
                }

                if ( tempLng3 !== 0 ) {
                    tempLat2 = tempLat3;
                    tempLng2 = tempLng3;
                }

                let avgXY = averageXY(tempLat2, tempLng2, tempLat3, tempLng3);

                let bearing = $.getAngle({
                    x: tempLat1,
                    y: tempLng1
                }, {
                    x: avgXY.lat,
                    y: avgXY.lng
                });

                if ( oldBearing === 0 ) oldBearing = bearing;

                if ( Math.abs( oldBearing - Math.abs(bearing) )  > 10 ) {
                    bearing = ( oldBearing + bearing ) / 2;
                }

                oldBearing = bearing;

                return bearing;
			}

			function appToEvent(key, value) {
				if (key === 'bottomSheet') {
                    if ( value === 'clicked') {
                        if ( $.isClickedByMaker && !$.isChangeMaker) {
                            $.initMarker();
                        }
					} else {
                        if ( $.isClickedByMaker) {
                            $.initMarker();
                        }
                    }
				}
			}

            // INTERFACE : APP TO WEB
            function appToGps(_gpsLatitude, _gpsLongitude) {

                let bearing = getAngleByCalibration();

				$('#location').css({
					'transform': 'rotate(' + (bearing) + 'deg)'
				});

                let position = new kakao.maps.LatLng(_gpsLatitude, _gpsLongitude);
                myLocMarker.setPosition(position);
                setMsgBarForAreaOfCurrentLocation(_gpsLatitude, _gpsLongitude, $.polygons);

                if ($.isFirst) {
                    $.initializeMove(position);
                }

                $.isFirst = false;
                gpsLatitude = _gpsLatitude;
                gpsLongitude = _gpsLongitude;

                tempLat3 = _gpsLatitude;
                tempLng3 = _gpsLongitude;

				$.loading(false);
            }

            // 현재 위치의 구역을 메시지 바에 작성하는 함수
			function setMsgBarForAreaOfCurrentLocation (_gpsLatitude, _gpsLongitude, polygons){

                let divMsgBar = $('.msg-bar');
                if ( polygons == null || polygons.length === 0) {
                    divMsgBar.find('p').text('현재 구역은 주차 가능한 구역입니다.');
                    divMsgBar.css('background-color', '#FFC527B5');
                    return;
				}

				let current_point = new Point(_gpsLongitude,_gpsLatitude);
				let len = polygons.length;
				let illegalType;

                let msg;
                let color;
                let points;

                let isInsideByCurrent = false;
				for (let i = 0; i < len; i++) {
                    points = [];
					polygons[i].points.forEach(function (overlay) {
						let x = overlay.x, y = overlay.y;
						points.push(new Point(x, y));
					})

					if (isInside(points, points.length, current_point)) {
                        illegalType = polygons[i].type;
                        isInsideByCurrent = true;
						break;
					}
				}

                if ( isInsideByCurrent) {
                    if(illegalType === 'ILLEGAL') {
                        msg = '현재 구역은 불법주정차 구역입니다.';
                        color = '#E63636B5'
                    } else if (illegalType === 'FIVE_MINUTE') {
                        msg = '현재 구역은 5분 주차 가능한 구역입니다.';
                        color = '#FF9443B5'
                    }
				} else {
                    msg = '현재 구역은 주차 가능한 구역입니다.';
                    color = '#FFC527B5'
				}

                divMsgBar.find('p').text(msg);
                divMsgBar.css('background-color', color);
			}

            $(function () {

                // 라디오버튼 change 이벤트 설정
                function handleRadioButton(event) {
                    let mapType = $('.mapType');
                    if ($.isMobile) webToApp.postMessage(JSON.stringify("click"));
                    for (const type of mapType) {
                        type.classList.remove("btn-dark");
                        type.classList.remove("rounded-pill");
                        type.classList.add("btn-white");
                    }

                    event.currentTarget.classList.remove("btn-white");
                    event.currentTarget.classList.add("btn-dark");
                    event.currentTarget.classList.add("rounded-pill");
                    $.currentMarkerSeq = 0;
                }

                // 통계 on off 이벤트 함수
                function handleChecked() {
					if($('#toggle').is(':checked')) {
						$.processDongCodesBounds();
					} else {
						$.loading(true);
						$.removeStatisticsOverlays();
						$.loading(false);
					}
				}

                // app 초기화
                function initializeByMobile() {

                    // 불법 구역 통계 정보 보기 / 감추기 이벤트
                    $('.toggleSwitch').on('click', function (){
                        handleChecked();
                        if ($(this).attr('class').indexOf('active') > -1 ) {
                            $(this).removeClass('active');
                        } else {
                            $(this).addClass('active');
						}
					});

					$('#option').on('click', function(){
						alert('준비중입니다.');
					})

                    // 불법주차 / 주차장 / 모빌리티 변경 이벤트
                    $('input:radio[name=mapSelect]').change(function (event) {
                        $.loading(true);
                        let mapType = $('.mapType');
                        for (const type of mapType) {
                            type.addEventListener("change", handleRadioButton);
                        }
                        $.mapSelected = event.target.id;
                        $.changeMapType();
						if(this.id === 'zone') {
							$('#onOffBtn').show();
							$('.notice-board').show();
						}
						else {
							$('#onOffBtn').hide();
                            $('.notice-board').hide();
						}
                        $.loading(false);
                    });

                    // 내위치 찾기 이벤트
                    $('#btnFindMe').on('click', function () {
                        $.findMe();
                    });

                    // 맵의 줌인 이벤트
                    $('#zoomIn').on('click', function () {
                        let level = $.getLevel('in');
                        if (level < 4) {
                            $.processDongCodesBounds();
                        }
                    });

                    // 맵의 줌아웃 이벤트
                    $('#zoomOut').on('click', function () {
                        let level = $.getLevel('out');
                        if (level <= 3) {
                            $.processDongCodesBounds();
                        } else {
                            if ($.mapSelected === 'zone') {
                            	$.removeOverlays();
							} else {
								$.removeTextOverlays();
							}
						}
                    });

                    $.findMe();
                }

                initializeByMobile();
            });

		</script>
	</stripes:layout-component>

</stripes:layout-render>