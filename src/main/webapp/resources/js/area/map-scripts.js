$(function () {
    $.polygons = [];

    // $.isMobile = false;
    $.CENTER_LATITUDE = 35.02035492064903;
    $.CENTER_LONGITUDE = 126.79383256393595;
    $.mapSelected = 'zone';

    let url = '';

    let overlays = []               // 지도에 그려진 도형을 담을 배열
    let statisticsOverlays = [];    // 통계 자료 배열

    let mapContainer = document.getElementById('map');
    let map;
    let kakaoEvent = kakao.maps.event;

    // 폴리곤 그리기
    function drawingPolygons(polygons) {
        $.removeOverlays();
        // 지도에 영역데이터를 폴리곤으로 표시합니다
        for (const element of polygons) {
            $.polygons.push(element);
            $.displayArea(element);
        }
    }

    // 카카오 맵 이벤트 설정
    function setKakaoEvent(opt) {
        kakaoEvent.addListener(opt.target, opt.event, opt.func);
    }

    // zone 초기화 함수
    function initializeZone(opt) {
        let result = $.JJAjaxAsync(opt);

        if (result.success) {
            zones.zonePolygons = result.data.zonePolygons;
            zones.zoneSeqs = result.data.zoneSeqs;
            zones.zoneTypes = result.data.zoneTypes;
            zones.receiptCnts = result.data.receiptCnts;
        }
    }

    // 카카오 초기화
    function initializeKakao() {
        map = {
            center: new kakao.maps.LatLng($.CENTER_LATITUDE, $.CENTER_LONGITUDE), // 지도의 중심좌표
            level: 3, // 지도의 확대 레벨
            disableDoubleClickZoom: true
        };

        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        map = new kakao.maps.Map(mapContainer, map);
        if ($.isMobile) map.setZoomable(false);

        setKakaoEvent({
            target: map,
            event: 'click',
            func: function (mouseEvent) {
                if($.beforeClickPolygon) {
                    $('#areaViewModal').offcanvas('hide');
                    $.changeOptionStroke();
                }
                $.initOverlay();
            }
        });

        let obj;

        // 확대수준이 변경되거나 지도가 이동했을때 타일 이미지 로드가 모두 완료되면 발생
        setKakaoEvent({
            target: map,
            event: 'idle',
            func: async function () {
                // 지도의  레벨을 얻어옵니다
                let level = map.getLevel();

                if (level <= 3) {
                    obj = await $.getDongCodesBounds(map);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    if (!obj.uniqueCodesCheck) {
                        if ($.mapSelected === 'zone') {
                            $.drawingZone(obj.codes);
                        } else {
                            await $.getsMarker(obj.codes);
                        }
                    }
                }
            }
        });

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: map,
            event: 'zoom_changed',
            func: async function () {
                // 지도의  레벨을 얻어옵니다
                let level = map.getLevel();

                if (level > 3) {
                    if ($.mapSelected === 'zone') $.removeOverlays();
                } else {
                    if (level === 3) {
                        if ($.mapSelected === 'zone') {
                            $.drawingZone(obj.codes);
                        } else {
                            await $.getsMarker(obj.codes);
                        }
                    }
                }
            }
        });

        setKakaoEvent({
            target: map,
            event: 'dragend',
            func: function () {
                $('#areaViewModal').offcanvas('hide');
                $.initOverlay();
            }
        });

    }

    // 아래 지도에 그려진 도형이 있다면 모두 지웁니다
    $.removeOverlays = function () {
        let len = overlays.length;
        for (let i = 0; i < len; i++) {
            if (statisticsOverlays.length > 0) {
                statisticsOverlays[i].setMap(null);
            }
            overlays[i].setMap(null);
        }
        statisticsOverlays = [];
        overlays = [];
    }

    // statisticsOverlays 삭제 함수
    $.removeStatisticsOverlays = function () {
        for (const overlay of statisticsOverlays) {
            overlay.setMap(null);
        }
        statisticsOverlays = [];
    }

    // 다각형을 생상하고 이벤트를 등록하는 함수입니다
    $.displayArea = function (area) {
        let path = $.pointsToPath(area.points);
        let style = area.options;

        // 다각형을 생성합니다
        let polygon = new kakao.maps.Polygon({
            map: map, // 다각형을 표시할 지도 객체
            path: path,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: $.setFillColor(area.type),
            fillOpacity: style.fillOpacity
        });

        if (!$.isMobile) {
            // 다각형에 mouseover 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 변경합니다
            setKakaoEvent({
                target: polygon,
                event: 'mouseover',
                func: function (mouseEvent) {
                    polygon.setOptions({
                        fillColor: '#EFFFED',   // 채우기 색깔입니다
                        fillOpacity: 0.8        // 채우기 불투명도 입니다
                    });
                }
            });

            // 다각형에 mouseout 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 원래색으로 변경합니다
            setKakaoEvent({
                target: polygon,
                event: 'mouseout',
                func: function () {
                    polygon.setOptions($.changeOptionByMouseOut(area.type));
                }
            });

            setKakaoEvent({
                target: polygon,
                event: 'click',
                func: function () {
                    kakao.maps.event.preventMap();

                    $.changeOptionStroke(polygon);

                    let center = centroid(area.points);
                    let centerLatLng = new kakao.maps.LatLng(center.y, center.x);
                    map.panTo(centerLatLng);
                    $('#areaViewModal').offcanvas('show');
                    $('.timeSelect').attr('disabled', true);
                    $.showModal(area.seq);
                }
            });
            if($.beforeClickPolygon) {
                if (JSON.stringify($.beforeClickPolygon.getPath()) === JSON.stringify(path)) {
                    $.changeOptionStroke(polygon);
                }
            }
        }

        let cnt = area.receiptCnt === undefined ? 0 : area.receiptCnt;
        let balloonImg = 'balloon_orange.png';
        if (area.type === 'ILLEGAL') balloonImg = 'balloon_red.png'

        if (!$('#toggle').is(':checked')) {
            let customOverlay = new kakao.maps.CustomOverlay({
                position: path[0],
                content: `<div class="balloon-image"><img src="/resources/assets/img/${balloonImg}" alt="불법주정차"/><span class="balloon-text">${cnt}</span></div>`,
                map: map
            });
            statisticsOverlays.push(customOverlay);
        }

        overlays.push(polygon);
    }

    // 동코드를 이용해서 zone 그리기 함수
    $.drawingZone = function (codes) {
        let searchIllegalType = $('input:radio[name=searchIllegalType]:checked').val();
        if (searchIllegalType === undefined) {
            searchIllegalType = '';
        }

        let select = SELECT_TYPE_AND_DONG;
        if (searchIllegalType === '') select = SELECT_DONG;
        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회

        let result = $.JJAjaxAsync({
            url: _contextPath + '/zone/gets',
            data: {
                select: select,
                illegalType: searchIllegalType,
                codes: codes,
                isSetting: false,
            }
        });
        if(result.success) {
            $.beforeCodes = codes;
            drawingPolygons($.getPolygonData(result.data));
        }
    }

    // 로딩 이미지 화면 설정 함수
    $.loading = function (isState) {
        if (isState) {
            $('.wrap-loading').show();
        } else {
            setTimeout(function () {
                $('.wrap-loading').hide();
            }, 500);
        }
    }

    // Marker 정보 가져오기 함수
    $.getsMarker = async function (codes) {
        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        let result = $.JJAjaxAsync({
            url: url,
            data: {
                codes: codes
            }
        });

        if (result.success) {
            $.removeTextOverlays();
            result.data.forEach(function (data) {
                $.addOverlay(data, map, $.setOverlayInfoByMobile);
            });
        }
    }

    // 불법주차 / 주차장 / 모빌리티 타입 변경 함수
    $.changeMapType = function () {
        if ($.mapSelected === 'zone') {
            $('#msgBar').removeClass('display-none');
            $.removeTextOverlays();
        } else if ($.mapSelected === 'parking') {
            $('#msgBar').addClass('display-none');
            $.removeOverlays();
            $.removeTextOverlays();
            url = _contextPath + '/parking/gets';
        } else if ($.mapSelected === 'pm') {
            $('#msgBar').addClass('display-none');
            $.removeOverlays();
            $.removeTextOverlays();
            url = _contextPath + '/pm/gets';
        }
        let level = map.getLevel();
        if(level < 4) $.processDongCodesBounds();
    }

    $.findMe = function () {
        if ($.isMobile) $.getMobileCurrentPosition(map);
        else $.getCurrentPosition(map);
    }

    // 동코드 처리 함수
    $.processDongCodesBounds = async function () {
        $.loading(true);
        if ($.mapSelected === 'zone') {
            $.drawingZone((await $.getDongCodesBounds(map)).codes);
        } else {
            $.getsMarker((await $.getDongCodesBounds(map)).codes);

        }
        $.loading(false);
    };

    // level 찾기 함수
    $.getLevel = function (zoomState) {
        if (zoomState === 'out') {
            map.setLevel(map.getLevel() + 1);
        } else {
            map.setLevel(map.getLevel() - 1);
        }
        return map.getLevel();
    }

    // 초기 화면 이동 함수
    $.initializeMove = function (position) {
        map.panTo(position);
    }

    $.changeIllegalType = async function () {
        $.drawingZone((await $.getDongCodesBounds(map)).codes);
    }

    // 초기화 실행
    initializeKakao();

});
