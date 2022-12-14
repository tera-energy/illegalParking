$.isModifyArea = false;
$.clickedPolygon = {};
$.drawingMap = {};
$.isDrawPolygonAfterEvent = false;

let CENTER_LATITUDE = 35.02035492064902;
let CENTER_LONGITUDE = 126.79383256393594;
let polygons = []; // polygon 배열
let kakaoEvent = kakao.maps.event;

let manager;
let polygonByAfterEvent;

// Overlay Type 설정 함수
$.setOverlayType = function (type) {
    // 그리기 중이면 그리기를 취소합니다
    $.cancelDrawing();
    // 클릭한 그리기 요소 타입을 선택합니다
    manager.select(kakao.maps.drawing.OverlayType[type]);
}

$.cancelDrawing = function () {
    // 그리기 중이면 그리기를 취소합니다
    manager.cancel();
}

$.undoManager = function () {
    if (manager.getOverlays().polygon.length > 0) {
        manager.remove(manager.getOverlays().polygon[0]);
    }
    $.isDrawPolygonAfterEvent = false;
    $.isModifyArea = false;
    polygonByAfterEvent = undefined;
}

// 생성한 Manager 의 Overlay 삭제 함수
$.removePolygonOfManager = function () {
    let getPolygons = manager.getOverlays().polygon;
    let len = getPolygons.length;
    for (let i = 0; i < len; i++) {
        manager.remove(getPolygons[0]);
    }
}

// 기존의 polygon을 Map 위에 보이기
$.addPolygonOnMap= function(polygon, map) {
    polygon.setMap(map);
    polygon.setOptions($.changeOptionByMouseOut(polygon.type));
}

// Polygon 을 맵에서 삭제하는 함수 (단일 삭제)
$.removePolygonOnMap = function(polygon){
    polygon.setMap(null);
}

// Polygons 삭제 함수 (복수 삭제 - )
function removePolygons() {
    const len = polygons.length;
    for (let i = 0; i < len; i++) {
        $.removePolygonOnMap(polygons[i]);
    }
    polygons = [];
}

// 카카오 맵 이벤트 설정
function setKakaoEvent(opt) {
    kakaoEvent.addListener(opt.target, opt.event, opt.func);
}

// polygon 그리기 함수
function drawPolygon(map, path, style, type) {
    return new kakao.maps.Polygon({
        map: map, // 다각형을 표시할 지도 객체
        path: path,
        strokeColor: style.strokeColor,
        strokeOpacity: style.strokeOpacity,
        strokeStyle: style.strokeStyle,
        strokeWeight: style.strokeWeight,
        fillColor: $.setFillColor(type),
        fillOpacity: style.fillOpacity
    });
}



// PolygonObj 객체 생성
$.getPolygonObj = function (polygon_) {
    let path = $.convertPointsToPath(polygon_.points);
    let style ;
    if ( polygon_.options === undefined) {
        style = polygonStyle
    } else  {
        style = polygon_.options;
    }

    return {
        polygon: drawPolygon($.drawingMap, path, style, polygon_.type),
        path: path
    }
}

// 다각형을 생성하고 이벤트를 등록하는 함수입니다
$.displayPolygon = function (polygon_) {
    let polygonObj = $.getPolygonObj(polygon_);

    let polygon = polygonObj.polygon;
    polygon.type = polygon_.type;
    polygon.seq = polygon_.seq;
    polygon.points = polygon_.points;
    polygon.receiptCnt = polygon_.receiptCnt;

    let path = polygonObj.path;

    // 다각형에 mouseover 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 변경합니다
    // 지역명을 표시하는 커스텀오버레이를 지도위에 표시합니다
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
    // 커스텀 오버레이를 지도에서 제거합니다
    setKakaoEvent({
        target: polygon,
        event: 'mouseout',
        func: function () {
            polygon.setOptions($.changeOptionByMouseOut(polygon.type));
        }
    });

    // 다각형에 클릭 이벤트를 등록합니다
    setKakaoEvent({
        target: polygon,
        event: 'click',
        func: function (mouseEvent) {
            kakao.maps.event.preventMap();

            $.clickedPolygon = polygon;

            if ($.isModifyArea) {
                $.isDrawPolygonAfterEvent = true;
                let managerPolygons = manager.getOverlays().polygon;

                // manager 초기화
                if (managerPolygons.length > 0) {
                    manager.remove(managerPolygons[0]);
                }

                // 생성
                manager.put(kakao.maps.drawing.OverlayType.POLYGON, path);
                $.removePolygonOnMap(polygon);
                polygonByAfterEvent = polygon;
            } else {
                if ($('#btnSet').css('display') === 'none' && (manager._mode === undefined || manager._mode === '')) {
                    let center = $.centroid(polygon.points);
                    $.drawingMap.panTo(new kakao.maps.LatLng(center.y, center.x));
                    $.changeOptionStroke($.clickedPolygon);
                    $.showModal(polygon.seq, 'areaSettingModal');
                }
            }
        }
    });

    if ($.beforeClickPolygon) {
        if (JSON.stringify($.beforeClickPolygon.getPath()) === JSON.stringify(path)) {
            $.changeOptionStroke(polygon);
        }
    }
    if (!$.isModifyArea) {
        polygons.push(polygon);
    }
}

$.initializePolygon = function (codes) {
    let result = $.JJAjaxAsync({
        url: _contextPath + '/zone/gets',
        data: {
            select: SELECT_DONG,
            illegalType: '',
            codes: codes,
            isSetting: true
        }
    });

    if (result.success) {
        $.beforeCodes = codes;

        // 기존의 polygon 삭제
        removePolygons();

        // polygon 표시하기
        for (const polygon of $.getPolygons(result.data)) {
            $.displayPolygon(polygon);
        }
    }
}


$.getManagerPolygonsLength = function () {
    return manager.getOverlays().polygon.length;
}

// 새로 생성한 Polygon 의 데이터 정보
$.getManagerData = function (mode) {
    let managerData = manager.getData();
    if (mode === 'set') {
        return {
            url: _contextPath + "/zone/set",
            data: {
                polygon: managerData[kakao.maps.drawing.OverlayType.POLYGON],
            }
        };
    } else {
        return {
            url: _contextPath + "/zone/modify",
            data: {
                polygon: managerData[kakao.maps.drawing.OverlayType.POLYGON][0],
                seq: $.clickedPolygon.seq,
            }
        };
    }
}

$.SetMaxLevel = function (lev) {
    $.drawingMap.setMaxLevel(lev);
}

// 카카오 초기화
$.initializeKakao = function () {
    $.drawingMap = {
        center: new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE), // 지도의 중심좌표
        level: $.MAP_MIN_LEVEL, // 지도의 확대 레벨
        disableDoubleClickZoom: true
    };

    // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
    $.drawingMap = new kakao.maps.Map(document.getElementById('drawMap'), $.drawingMap);

    let options = { // Drawing Manager를 생성할 때 사용할 옵션입니다
        map: $.drawingMap, // Drawing Manager로 그리기 요소를 그릴 map 객체입니다
        drawingMode: [ // Drawing Manager로 제공할 그리기 요소 모드입니다
            kakao.maps.drawing.OverlayType.POLYGON
        ],
        // 사용자에게 제공할 그리기 가이드 툴팁입니다
        // 사용자에게 도형을 그릴때, 드래그할때, 수정할때 가이드 툴팁을 표시하도록 설정합니다
        guideTooltip: ['draw', 'drag', 'edit'],
        polygonOptions: {
            draggable: true,
            removable: true,
            editable: true,
            strokeColor: '#000000',
            fillColor: '#00afff',
            fillOpacity: 0.5,
            hintStrokeStyle: 'dash',
            hintStrokeOpacity: 0.5
        }
    };

    manager = new kakao.maps.drawing.DrawingManager(options);

    manager.addListener('remove', function (e) {
        if ($.isDrawPolygonAfterEvent) $.addPolygonOnMap(polygonByAfterEvent, $.drawingMap);
    });

    // 지도에 마우스 오른쪽 클릭 이벤트를 등록합니다
    // 선을 그리고있는 상태에서 마우스 오른쪽 클릭 이벤트가 발생하면 그리기를 종료합니다
    setKakaoEvent({
        target: $.drawingMap,
        event: 'rightclick',
        func: function (mouseEvent) {
            if ($.isModifyArea) {
                $.removePolygonOfManager();
            } else {
                // 그리기 중이면 그리기를 취소합니다
                $.cancelDrawing();
            }
        }
    });

    // 맵 더블클릭 이벤트 등록
    setKakaoEvent({
        target: $.drawingMap,
        event: 'dblclick',
        func: function (mouseEvent) {
            if (!$.isModifyArea) {
                $.initBtnState.func($.initBtnState.set);
                $.setOverlayType('POLYGON');
            }
        }
    });

    // 맵 클릭 이벤트 등록
    setKakaoEvent({
        target: $.drawingMap,
        event: 'click',
        func: function (mouseEvent) {
            if ($('#areaSettingModal').hasClass('show')) {
                $.SetMaxLevel($.MAP_MAX_LEVEL);
                $.display.isShow($('#areaSettingModal'), false, 'canvas');
                // $('#areaSettingModal').offcanvas('hide');
            }
            if ($.beforeClickPolygon) {
                $.changeOptionStroke();
            }
        }
    });

    let obj;

    // 중심 좌표나 확대 수준이 변경되면 발생한다.
    setKakaoEvent({
        target: $.drawingMap,
        event: 'idle',
        func: async function () {
            // $('#areaSettingModal').offcanvas('hide');
            // 지도의  레벨을 얻어옵니다
            let level = $.drawingMap.getLevel();

            $('#mapLevel').text(level + '레벨');

            if (level <= $.MAP_MIN_LEVEL && !$.isModifyArea) {
                obj = await $.getDongCodesBounds($.drawingMap);
                // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                if (!obj.uniqueCodesCheck) {
                    $.initializePolygon(obj.codes);
                }
            }
        }
    });

    // 중심 좌표나 확대 수준이 변경되면 발생한다.
    setKakaoEvent({
        target: $.drawingMap,
        event: 'zoom_changed',
        func: async function () {
            if (!$.isModifyArea) {
                // 지도의  레벨을 얻어옵니다
                let level = $.drawingMap.getLevel();
                if (level > $.MAP_MIN_LEVEL) {
                    removePolygons();
                } else {
                    if (level === $.MAP_MIN_LEVEL) {
                        await $.initializePolygon(obj.codes);
                    }
                }
            }
        }
    });
}
