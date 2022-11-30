$(function () {

    $.isModifyArea = false;

    $.LEVEL_TEN = 10;
    $.LEVEL_THREE = 3;

    $.clickedPolygon = {};

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;

    // Drawing Manager로 도형을 그릴 지도 div
    let drawingMapContainer = document.getElementById('drawingMap');

    let drawingMap;

    let overlays = [] // 지도에 그려진 도형을 담을 배열
    let kakaoEvent = kakao.maps.event;

    // 위에 작성한 옵션으로 Drawing Manager를 생성합니다
    let manager;

    // Overlay Type 설정 함수
    $.setOverlayType = function(type) {
        // 그리기 중이면 그리기를 취소합니다
        $.cancelDrawing();
        // 클릭한 그리기 요소 타입을 선택합니다
        manager.select(kakao.maps.drawing.OverlayType[type]);
    }

    $.cancelDrawing = function() {
        // 그리기 중이면 그리기를 취소합니다
        manager.cancel();
    }

    $.undoManager = function() {
        if($.isModifyArea) {
            if(manager.getOverlays().polygon.length > 0) {
                manager.remove(manager.getOverlays().polygon[0]);
            }
            $.isDrawPolygonAfterEvent = false;
            $.isModifyArea = false;
        }
    }


    // 생성한 Manager 의 Overlay 삭제 함수
    $.removeOverlaysOfManager = function() {
        let getPolygons = manager.getOverlays().polygon;
        let len = getPolygons.length;
        for (let i = 0; i < len; i++) {
            manager.remove(getPolygons[0]);
        }
    }

    // Overlay 삭제 함수
    function removeOverlays() {
        let len = overlays.length, i = 0;
        for (; i < len; i++) {

            overlays[i].setMap(null);
        }
        overlays = [];
    }

    // 폴리곤 그리기
    function drawingPolygons(polygons) {
        removeOverlays();
        // 지도에 영역데이터를 폴리곤으로 표시합니다
        for (const element of polygons) {
            $.displayArea(element);
        }
    }

    // 카카오 맵 이벤트 설정
    function setKakaoEvent(opt) {
        kakaoEvent.addListener(opt.target, opt.event, opt.func);
    }

    // 폴리곤 객체 생성
    $.newPolygon = function(area) {
        let path = $.pointsToPath(area.points);
        let style = area.options;

        // 다각형을 생성합니다
        return {
            polygon: new kakao.maps.Polygon({
                map: drawingMap, // 다각형을 표시할 지도 객체
                path: path,
                strokeColor: style.strokeColor,
                strokeOpacity: style.strokeOpacity,
                strokeStyle: style.strokeStyle,
                strokeWeight: style.strokeWeight,
                fillColor: $.setFillColor(area.type),
                fillOpacity: style.fillOpacity
            }),
            path: path
        }
    }

    // 다각형을 생상하고 이벤트를 등록하는 함수입니다
    $.displayArea = function(area) {
        let polygonObj = $.newPolygon(area)

        let polygon = polygonObj.polygon;
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
                    polygon.setOptions($.changeOptionByMouseOut(area.type));
                }
            });

        // 다각형에 클릭 이벤트를 등록합니다
        setKakaoEvent({
                target: polygon,
                event: 'click',
                func: function (mouseEvent) {
                    kakao.maps.event.preventMap();

                    $.clickedPolygon = {area: area, clickPolygon: polygon};

                    if($.isModifyArea) {
                        $.isDrawPolygonAfterEvent = true;
                        let managerOverlay = manager.getOverlays().polygon[0];
                        if(managerOverlay !== undefined) {
                            $.cancelDrawing();
                            manager.remove(managerOverlay);
                        }
                        polygon.setMap(null);
                        manager.put(kakao.maps.drawing.OverlayType.POLYGON, path);

                        areaByAfterEvent = area;
                        polygonByAfterEvent = polygon;
                    } else {
                        if (manager._mode === undefined || manager._mode === '') {
                            $('#areaSettingModal').offcanvas('show');
                            let center = centroid(area.points);
                            drawingMap.panTo(new kakao.maps.LatLng(center.y,center.x));
                            $.showModal(area.seq);
                        }
                        $.changeOptionStroke($.clickedPolygon.clickPolygon);
                    }
                }
            });

        if($.beforeClickPolygon) {
            if (JSON.stringify($.beforeClickPolygon.getPath()) === JSON.stringify(path)) {
                $.changeOptionStroke(polygon);
            }
        }
        overlays.push(polygon);
    }

    $.drawingZone = function(codes) {
        let searchIllegalType = $('input:radio[name=searchIllegalType]:checked').val();
        let select = SELECT_TYPE_AND_DONG;
        if (searchIllegalType === '') select = SELECT_DONG;
        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        let result = $.JJAjaxAsync({
            url: _contextPath + '/zone/gets',
            data: {
                select: select,
                illegalType: searchIllegalType,
                codes: codes,
                isSetting: true
            }
        })
        if(result.success) {
            $.beforeCodes = codes;
            drawingPolygons($.getPolygonData(result.data));
        }
    }

    let areaByAfterEvent;
    let polygonByAfterEvent;
    $.isDrawPolygonAfterEvent = false;

    function drawPolygonAfterEvent() {
        polygonByAfterEvent.setMap(drawingMap);
        polygonByAfterEvent.setOptions($.changeOptionByMouseOut(areaByAfterEvent.type));
    }

    // 카카오 초기화
    function initializeKakao() {
        drawingMap = {
            center: new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE), // 지도의 중심좌표
            level: 3, // 지도의 확대 레벨
            disableDoubleClickZoom: true
        };

        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        drawingMap = new kakao.maps.Map(drawingMapContainer, drawingMap);

        let options = { // Drawing Manager를 생성할 때 사용할 옵션입니다
            map: drawingMap, // Drawing Manager로 그리기 요소를 그릴 map 객체입니다
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

        manager.addListener('remove', function(e) {
           if ($.isDrawPolygonAfterEvent) drawPolygonAfterEvent();
        });

        // 지도에 마우스 오른쪽 클릭 이벤트를 등록합니다
        // 선을 그리고있는 상태에서 마우스 오른쪽 클릭 이벤트가 발생하면 그리기를 종료합니다
        setKakaoEvent({
            target: drawingMap,
            event: 'rightclick',
            func: function (mouseEvent) {
                if ($.isModifyArea) {
                    $.undoManager();
                } else {
                    // 그리기 중이면 그리기를 취소합니다
                    $.cancelDrawing();
                    $.removeOverlaysOfManager();
                }
                $.initBtnState();
            }
        });

        // 맵 더블클릭 이벤트 등록
        setKakaoEvent({
            target: drawingMap,
            event: 'dblclick',
            func: function (mouseEvent) {
                if(!$.isModifyArea) {
                    $('#btnAddOverlay').hide();
                    $('#btnModifyOverlay').hide();
                    $('#btnSet').show();
                    $('#btnModify').hide();
                    $('#btnCancel').show();
                    $('#areaSettingModal').offcanvas('hide');
                    $.setOverlayType('POLYGON');
                }
            }
        });

        // 맵 클릭 이벤트 등록
        setKakaoEvent({
            target: drawingMap,
            event: 'click',
            func: function (mouseEvent) {
                $.SetMaxLevel($.LEVEL_TEN);
                $('#areaSettingModal').offcanvas('hide');

                if($.beforeClickPolygon) {
                    $.changeOptionStroke();
                }
            }
        });

        let obj;

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: drawingMap,
            event: 'idle',
            func: async function () {
                // $('#areaSettingModal').offcanvas('hide');
                // 지도의  레벨을 얻어옵니다
                let level = drawingMap.getLevel();

                $('#mapLevel').text(level + '레벨');

                if(level <= 3 && !$.isModifyArea) {
                    obj = await $.getDongCodesBounds(drawingMap);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    if(!obj.uniqueCodesCheck) {
                        $.drawingZone(obj.codes);
                    }
                }
            }
        });

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: drawingMap,
            event: 'zoom_changed',
            func: async function () {
                if(!$.isModifyArea) {
                    // 지도의  레벨을 얻어옵니다
                    let level = drawingMap.getLevel();
                    if (level > 3) {
                        removeOverlays();
                    } else {
                        if (level === 3) {
                            await $.drawingZone(obj.codes);
                        }
                    }
                }
            }
        });

    }

    // 초기화
    function initialize() {
        initializeKakao();
        $.getManagerPolygonsLength = function () {
            return manager.getOverlays().polygon.length;
        }

        $.getManagerData = function(mode){
            // Drawing Manager에서 그려진 데이터 정보를 가져옵니다
            let managerData = manager.getData();
            let url;
            let data;
            if(mode === 'set') {
                url = "/zone/set";
                data = {
                    polygon: managerData[kakao.maps.drawing.OverlayType.POLYGON],
                }
            } else {
                url = "/zone/modify";
                data = {
                    polygon: managerData[kakao.maps.drawing.OverlayType.POLYGON][0],
                    seq: $.clickedPolygon.area.seq,
                }
            }
            return {
                url: _contextPath + url,
                data: data,
            };
        }

        $.drawingZoneWithCodes = async function() {
            let codes = (await $.getDongCodesBounds(drawingMap)).codes;
            await $.drawingZone(codes);
        }

        $.SetMaxLevel = function(lev) {
            drawingMap.setMaxLevel(lev);
        }

        $.getCurrentPosition(drawingMap);

    }
    //
    initialize();
});
