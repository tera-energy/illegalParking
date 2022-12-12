/******************************
 * 전역 변수
 * ****************************/

// 모바일 여부 확인
$.isMobile = window.location.pathname.includes('api');

let myMarker = {
    imgSrc: '/resources/assets/img/location_shadow.png',
    imgSize: new kakao.maps.Size(50, 50)
};
let markerImg = new kakao.maps.MarkerImage(myMarker.imgSrc, myMarker.imgSize);
let myLocMarker = null;
$.beforeCodes = [];

$.isFirst = true; // 최초 인가 확인
let gpsLatitude = 0.0;
let gpsLongitude = 0.0;
let textOverlays = [];

$.currentMarkerSeq = 0;
$.isClickedByMaker = false;
$.isChangeMaker = false;

$.MAP_MAX_LEVEL = 10;
$.MAP_MIN_LEVEL = 3;

$.beforeClickPolygon = undefined;
let polygonStyle = {
    "draggable": true,
    "removable": true,
    "editable": true,
    "strokeWeight": 0,
    "fillColor": "#000000",
    "fillOpacity": 0.5
};

/******************************
 * 내부 함수
 * ****************************/

class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}

function onSegment(p, q, r) {
    return q.x <= Math.max(p.x, r.x) &&
        q.x >= Math.min(p.x, r.x) &&
        q.y <= Math.max(p.y, r.y) &&
        q.y >= Math.min(p.y, r.y);

}

function orientation(p, q, r) {
    let val = (q.y - p.y) * (r.x - q.x)
        - (q.x - p.x) * (r.y - q.y);

    if (val === 0) {
        return 0; // collinear
    }
    return (val > 0) ? 1 : 2; // clock or counterclock wise
}

function doIntersect(p1, q1, p2, q2) {
    let o1 = orientation(p1, q1, p2);
    let o2 = orientation(p1, q1, q2);
    let o3 = orientation(p2, q2, p1);
    let o4 = orientation(p2, q2, q1);

    // General case
    if (o1 !== o2 && o3 !== o4) {
        return true;
    }

    // Special Cases
    // p1, q1 and p2 are collinear and
    // p2 lies on segment p1q1
    if (o1 === 0 && onSegment(p1, p2, q1)) {
        return true;
    }

    // p1, q1 and p2 are collinear and
    // q2 lies on segment p1q1
    if (o2 === 0 && onSegment(p1, q2, q1)) {
        return true;
    }

    // p2, q2 and p1 are collinear and
    // p1 lies on segment p2q2
    if (o3 === 0 && onSegment(p2, p1, q2)) {
        return true;
    }

    // p2, q2 and q1 are collinear and
    // q1 lies on segment p2q2
    if (o4 === 0 && onSegment(p2, q1, q2)) {
        return true;
    }

    // Doesn't fall in any of the above cases
    return false;
}

//현재위치 마커 커스텀 오버레이
function myLocationMarker(map, position) {
    $.loading(true);
    if ($.isMobile) {
        let locationOverlay = new kakao.maps.CustomOverlay(
            {zIndex: 1, yAnchor: 3});

        let outlineNode = document.createElement('div');
        outlineNode.className = 'outline';

        let img = document.createElement('img');
        img.id = 'location';
        img.src = `/resources/assets/img/location.png`;

        outlineNode.appendChild(img);

        let waves = document.createElement('div');
        waves.className = 'waves';
        outlineNode.appendChild(waves);

        // 커스텀 오버레이를 지도에 표시합니다
        locationOverlay.setPosition(position);
        locationOverlay.setContent(outlineNode)
        locationOverlay.setMap(map);
        myLocMarker = locationOverlay;
    }
    map.setLevel(3);
    map.panTo(position);
    $.loading(false);
}

// 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
function getMarkerInfo(type, data) {
    let dataObj = null;
    if (type === 'parking') {
        dataObj = {
            pkName: data.prkplceNm,
            pkAddr: data.rdnmadr,
            pkPrice: data.parkingchrgeInfo === '유료' ? `기본 ${data.basicTime} 분 | ${data.addUnitTime} 분당 ${data.addUnitCharge}원 추가` : '',
            pkOper: data.parkingchrgeInfo,
            pkCount: data.prkcmprt,
            pkPhone: data.phoneNumber,
            pkLat: data.latitude,
            pkLng: data.longitude
        }
    } else if (type === 'pm') {
        dataObj = {
            pmName: data.pmName,
            pmPrice: data.pmPrice,
            pmOper: data.pmOperOpenHhmm + '~' + data.pmOperCloseHhmm,
            pmModel: data.pmId
        }
    }

    return {
        type: type,
        data: dataObj
    };
}

// 마커 이미지 설정 함수
function setImgOrigin(pmType) {
    let imgSrc;
    let type = 'parking';
    let imageSize = new kakao.maps.Size(70, 77);
    if ($.mapSelected === 'pm') {
        type = 'pm';
        imgSrc = {
            normalOrigin: `/resources/assets/img/${pmType}_off.png`,
            clickOrigin: `/resources/assets/img/${pmType}_on.png`
        }
    } else {
        imageSize = new kakao.maps.Size(70, 45);
        imgSrc = {
            normalOrigin: '/resources/assets/img/panel_off.png',
            clickOrigin: '/resources/assets/img/panel_on.png'
        }
    }

    return {type: type, imgSrc: imgSrc, imgSize: imageSize}
}

// 구역 Event Time 초기화 함수
function initializeEventTime(timeObj) {
    const firstStartTimeHour = '12';
    const firstEndTimeHour = '14';
    const secondStartTimeHour = '20';
    const secondEndTimeHour = '08';

    const usedTypeArr = [
        {
            'usedType' : 'first',
            'used' : timeObj.usedFirst,
            'startTime' : timeObj.firstStartTime,
            'endTime' : timeObj.firstEndTime,
            'startTimeHour': firstStartTimeHour,
            'endTimeHour': firstEndTimeHour
        },
        {
            'usedType' : 'second',
            'used' : timeObj.usedSecond,
            'startTime' : timeObj.secondStartTime,
            'endTime' : timeObj.secondEndTime,
            'startTimeHour': secondStartTimeHour,
            'endTimeHour': secondEndTimeHour
        }
    ]

    for(const data of usedTypeArr) {
        if (data.used) {
            $(`#${data.usedType}StartTimeHour`).val(data.startTimeHour).prop('selected', true);
            $(`#${data.usedType}StartTimeMinute`).val('00').prop('selected', true);

            $(`#${data.usedType}EndTimeHour`).val(data.endTimeHour).prop('selected', true);
            $(`#${data.usedType}EndTimeMinute`).val('00').prop('selected', true);
        } else {
            const startTime = data.startTime.split(':');
            const endTime = data.endTime.split(':');
            $(`#${data.usedType}StartTimeHour`).val(startTime[0]).prop('selected', true);
            $(`#${data.usedType}StartTimeMinute`).val(startTime[1]).prop('selected', true);
            $(`#${data.usedType}EndTimeHour`).val(endTime[0]).prop('selected', true);
            $(`#${data.usedType}EndTimeMinute`).val(endTime[1]).prop('selected', true);
        }
    }
}

// 구역 Event Form 설정 함수
function setEventHtml(data) {
    const $locationType = $('#locationType');
    const $btnModifyEvent = $('#btnModifyEvent');
    const $usedFirst = $('#usedFirst');
    const $usedSecond = $('#usedSecond');

    const timeObj = {
        usedFirst: data.usedFirst,
        usedSecond: data.usedSecond,
        firstStartTime: data.firstStartTime,
        firstEndTime: data.firstEndTime,
        secondStartTime: data.secondStartTime,
        secondEndTime: data.secondEndTime,
    }

    $('#offcanvasRightLabel').text(data.zoneSeq + '번 구역설정');
    if (data.eventSeq === null) {
        timeObj.usedFirst = true;
        timeObj.usedSecond = true;

        $('input:radio[name=illegalType]').eq(0).prop('checked', true);

        $('#locationType option:eq(0)').prop('selected', true);
        $("#name option").remove();
        $.setGroupNames($locationType.val());

        $('.timeSelect').attr('disabled', true);
        $usedFirst.prop('checked', false);
        $usedSecond.prop('checked', false);

        $btnModifyEvent.text('등록');
        $btnModifyEvent.addClass('btn-primary');
        $btnModifyEvent.removeClass('btn-danger');
    } else {
        data.usedFirst === false ? $usedFirst.prop('checked', true) : $usedFirst.prop('checked', false);
        data.usedSecond === false ? $usedSecond.prop('checked', true) : $usedSecond.prop('checked', false);

        $(`input:radio[name=illegalType]:input[value=${data.illegalType}]`).prop('checked', true);

        $('#eventSeq').val(data.eventSeq);

        $locationType.val(data.locationType).prop('selected', true);
        $("#name option").remove();
        $.setGroupNames(data.locationType);
        $('#name').val(data.groupSeq).prop('selected', true);

        $usedFirst.trigger('change');
        $usedSecond.trigger('change');

        $btnModifyEvent.text('수정');
        $btnModifyEvent.addClass('btn-danger');
        $btnModifyEvent.removeClass('btn-primary');
    }

    initializeEventTime(timeObj);
}


/******************************
 * 전역 함수
 * ****************************/

/* 좌표로 동코드 받기 카카오 REST API */
$.async_coordinatesToDongCodeKakaoApi = async function (x, y) {
    let code = '';
    let URL = `https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?input_coord=WGS84&output_coord=WGS84&x=${x}&y=${y}`;
    let opt = {
        headers: {
            Authorization: "KakaoAK 350a9e6cc59932a26806ab0c0b6fdd2e"
        }
    };
    const response = await fetch(URL, opt);
    const data = await response.json();
    data.documents.forEach(region => {
        if (region.region_type === 'B') code = region.code.substr(0, 8).concat('00');
    });
    return code;
}
/* 좌표로 동코드 받기 카카오 REST API end */

/* 폴리곤 중심 좌표 구하기 */
$.centroid = function (points) {
    let i, j, len, p1, p2, f, area, x, y;

    area = x = y = 0;

    for (i = 0, len = points.length, j = len - 1; i < len; j = i++) {
        p1 = points[i];
        p2 = points[j];

        f = p1.y * p2.x - p2.y * p1.x;
        x += (parseFloat(p1.x) + parseFloat(p2.x)) * f;
        y += (parseFloat(p1.y) + parseFloat(p2.y)) * f;
        area += f * 3;
    }

    return {x: x / area, y: y / area};
}

/* */
$.isInside = function (polygon, n, p) {
    polygon.push(polygon[0]);

    if (n < 3) {
        return false;
    }

    let INF = 10000;
    let extreme = new Point(INF, p.y);
    let count = 0, i = 0;

    do {
        let next = (i + 1) % n;
        if (doIntersect(polygon[i], polygon[next], p, extreme)) {
            if (orientation(polygon[i], p, polygon[next]) === 0) {
                return onSegment(polygon[i], p,
                    polygon[next]);
            }

            count++;
        }
        i = next;
    } while (i !== 0);

    return (count % 2 === 1); // Same as (count%2 == 1)
}

//geoLocation API를 활용한 현재 위치를 구하고 지도의 중심 좌표 변경
$.getCurrentPosition = function (map) {
    if (!!myLocMarker) myLocMarker.setMap(null);

    map.panTo(new kakao.maps.LatLng(35.01868444, 126.78284599));
    return;
    // TODO : https 를 이용한 현재 위치 이동 방법
    //        ( teraenergy.p12 파일을 공인 인증서를 이용하여 생성 해야함 )
    // if (navigator.geolocation) {
    //     navigator.geolocation.getCurrentPosition(
    //         async (position) => {
    //             let currentLat = `${position.coords.latitude}`; // y
    //             let currentLng = `${position.coords.longitude}`; // x
    //             // 지도 중심좌표를 접속위치로 변경합니다
    //             map.panTo(new kakao.maps.LatLng(currentLat, currentLng));
    //
    //         },
    //         (error) => {
    //             console.error(error);
    //         },
    //         {
    //             enableHighAccuracy: true, // 위치정보를 가장 높은 정확도로 수신 true, 기본 false, 응답이 느리고 전력소모량 증가
    //             maximumAge: 0,
    //             timeout: Infinity,
    //         }
    //     );
    // } else {
    //     let position = new kakao.maps.LatLng(35.01868444, 126.78284599);
    //     alert("위치 권한을 설정하시기 바랍니다.");
    //     map.panTo(position);
    // }
}

//geoLocation API를 활용한 현재 위치를 구하고 지도의 중심 좌표 변경
$.getMobileCurrentPosition = function (map) {
    if (!!myLocMarker) myLocMarker.setMap(null);
    gpsLatitude = gpsLatitude === 0 ? '35.01868444' : gpsLatitude.toString();
    gpsLongitude = gpsLongitude === 0 ? '126.78284599' : gpsLongitude.toString();
    let gpsPosition = new kakao.maps.LatLng(gpsLatitude, gpsLongitude);
    myLocationMarker(map, gpsPosition);
}

// 꼭지점, 중심좌표의 법정동 코드 가져오기
$.getDongCodesBounds = async function (map) {
    // 맵 구역
    const bounds = map.getBounds();
    // 영역정보의 남서쪽 정보를 얻어옵니다
    const swLatLng = bounds.getSouthWest();
    const south = swLatLng.getLat();
    const west = swLatLng.getLng();

    // 영역정보의 북동쪽 정보를 얻어옵니다
    const neLatLng = bounds.getNorthEast();
    const north = neLatLng.getLat();
    const east = neLatLng.getLng();

    // 동, 서, 남, 북 좌표
    // log(east, west, south, north);
    const center = map.getCenter();

    const latLngs = [{x: east, y: north}, {x: west, y: north}, {x: east, y: south}, {x: west, y: south}, {x: center.getLng(), y: center.getLat()}]
    let codes = [];

    for (const latLng of latLngs) {
        const code = await $.async_coordinatesToDongCodeKakaoApi(latLng.x, latLng.y);
        codes.push(code);
    }
    codes = [...new Set(codes)]


    const uniqueCodesCheck = _.isEmpty(_.xor($.beforeCodes, codes));

    // log('codes : ', codes);
    // log('$.beforeCodes : ', $.beforeCodes);
    // log('_.xor($.beforeCodes, codes) : ', _.xor($.beforeCodes, codes));
    // log('uniqueCodesCheck : ', uniqueCodesCheck);


    return {
        codes: codes,
        uniqueCodesCheck: uniqueCodesCheck
    };
}

// 선과 다각형의 꼭지점 정보를 kakao.maps.LatLng 객체로 생성하고 배열로 반환하는 함수입니다
// GPS 좌표(point)를 kakao Map 좌표(path)로 변환
$.convertPointsToPath = function (points) {
    let len = points.length,
        path = [],
        i = 0;

    for (; i < len; i++) {
        let latlng = new kakao.maps.LatLng(points[i].y, points[i].x);
        path.push(latlng);
    }

    return path;
}

// 다각형에 마우스아웃 이벤트가 발생했을 때 변경할 채우기 옵션입니다
$.changeOptionByMouseOut = function (type) {
    return {
        fillColor: $.setFillColor(type), // 채우기 색깔입니다
        fillOpacity: 0.5 // 채우기 불투명도 입니다
    };
}

// 주정차 타입에 따른 폴리곤 색 구별
$.setFillColor = function (type) {
    let fillColor;
    if (type === 'FIVE_MINUTE') fillColor = '#ff6f00';
    else if (type === 'ILLEGAL') fillColor = '#FF3333';
    else fillColor = '#00afff';

    return fillColor;
}

// 방위각 구하기
$.getAngle = function (center, target) {
    let dx = target.x - center.x;
    let dy = center.y - target.y;
    let deg = Math.atan2(dy, dx) * 180 / Math.PI;

    return (-deg + 390) % 360 | 0;
}

// mobile 인 경우 Overlay 정보 설정 함수
$.setOverlayInfoByMobile = function (data) {
    if ($.isMobile) {
        let obj = getMarkerInfo($.mapSelected, data);
        webToApp.postMessage(JSON.stringify(obj));
    }
}

// 마커 커스텀 오버레이 추가 함수
$.addOverlay = function (data, map, callback) {
    let imgOrigin = setImgOrigin(data.pmType);

    let contentNode = document.createElement('div'); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
    // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
    contentNode.className = 'content_wrap';

    let imgNode = document.createElement('div');
    imgNode.className = 'img_wrap';

    let img = document.createElement('img');

    if ($.currentMarkerSeq === data.parkingSeq) {
        img.src = imgOrigin.imgSrc.clickOrigin;
    } else {
        img.src = imgOrigin.imgSrc.normalOrigin;
    }

    img.width = imgOrigin.imgSize.width;
    img.height = imgOrigin.imgSize.height;
    img.className = 'markerImg';

    imgNode.appendChild(img);

    if ($.mapSelected === 'parking') {
        let text = '현재무료'
        if (data.parkingchrgeInfo === '유료') text = '유료'
        let span = document.createElement('span')

        span.className = 'price-text';
        span.appendChild(document.createTextNode(text));

        if ($.currentMarkerSeq === data.parkingSeq) {
            span.style.color = 'white';
        } else {
            span.style.color = 'black';
        }
        imgNode.appendChild(span);
    }

    imgNode.addEventListener('click', function (event) {

        let src = $(this).children('img:first').attr('src');
        let imgType = src.split("/").pop().replace(".png", "");
        let isOn = imgType.split("_").pop().includes("on");

        if ($.isMobile) webToApp.postMessage(JSON.stringify("click"));

        $.initMarker();

        if (!isOn) {

            this.children[0].src = imgOrigin.imgSrc.clickOrigin;
            if ($.mapSelected === 'parking') {

                if ( $.currentMarkerSeq !== data.parkingSeq) {
                    $.isChangeMaker = true;
                }

                this.children[1].style.color = 'white';
                $.currentMarkerSeq = data.parkingSeq;
            } else {

                if ( $.currentMarkerSeq !== data.pmSeq) {
                    $.isChangeMaker = true;
                }

                $.currentMarkerSeq = data.pmSeq;
            }

            map.panTo(new kakao.maps.LatLng(( data.latitude - 0.000680), data.longitude));
            callback(data);
            $(imgNode).children('img').addClass("active");

            $.isClickedByMaker = true;
        } else {
            if (!$.isMobile) {
                $('.close').trigger('click');
            }
        }
    });

    contentNode.appendChild(imgNode);

    // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
    contentNode.className = 'content_wrap';

    let markerOverlay = new kakao.maps.CustomOverlay({
        clickable: true,
        zIndex: 0,
        position: new kakao.maps.LatLng(data.latitude, data.longitude),
        content: contentNode,
        map: map
    });
    textOverlays.push(markerOverlay);
}

// Marker 초기화 함수
$.initMarker = function () {
    if ($.isClickedByMaker && $('.markerImg').length > 0) {
        $('.markerImg').each(function () {
            if ($(this).attr('class').indexOf('active') > -1) {
                $(this).attr('src', $(this).attr('src').replace('on', 'off'));
                if ($.mapSelected === 'parking') {
                    $(this).parent().children('span').css({
                        "color": "black"
                    });
                }
                $(this).removeClass('active');
                $.currentMarkerSeq = 0;
                $.isClickedByMaker = false;
            }
        });
        $.isChangeMaker = false;
    }
}

// Overlay 초기화 함수
$.initOverlay = function () {
    if ($.isMobile) webToApp.postMessage(JSON.stringify("click"));
    $.currentMarkerSeq = 0;

    if ($.mapSelected !== 'zone') {
        let $markerImg = $('.markerImg');
        if ($.isClickedByMaker && $markerImg.length > 0) {
            $markerImg.each(function () {
                if ($(this).attr('class').indexOf('active') > -1) {
                    $.initMarker();
                }
            });
        }
    }
}

// TextOverlay 삭제 함수
$.removeTextOverlays = function () {
    for (const overlay of textOverlays) {
        overlay.setMap(null);
    }
    textOverlays = [];
}

// 구역 Event 불법주정차 그룹 지정 함수
$.setGroupNames = function (locationType) {

    function getNamesSelectHtml(groups) {
        let html = '';
        for (const group of groups) {
            html += '<option value="' + group.groupSeq + '">' + group.name + '</option>';
        }
        return html;
    }

    let result = $.JJAjaxAsync({
        url: _contextPath + '/event/group/name/get',
        data: {
            locationType: locationType
        }
    });

    if (result.success) {
        let groups = result.data;
        let html = getNamesSelectHtml(groups);
        $('#name').append(html);
    }
}

// 폴리곤 클릭 시 모달 창 오픈
$.showModal = function (seq, id) {
    let result = $.JJAjaxAsync({
        url: _contextPath + '/zone/get',
        data: {
            zoneSeq: seq
        }
    });

    if (result.success) {
        $.SetMaxLevel($.MAP_MIN_LEVEL);
        let data = result.data;
        $('#zoneSeq').val(data.zoneSeq);
        setEventHtml(data);

        $('#'+id).offcanvas('show');
    } else {
        alert(result.msg);
    }
}

//클릭한 구역 테두리 변경 함수
$.changeOptionStroke = function (polygon) {
    if ( polygon !== undefined) {
        polygon.setOptions({
            "strokeColor": '#000000',
            "strokeWeight": 2,
        });

        if ($.beforeClickPolygon) {
            if(JSON.stringify(polygon.getPath()) !== JSON.stringify($.beforeClickPolygon.getPath())) {
                $.beforeClickPolygon.setOptions({
                    "strokeWeight": 0,
                });
            }
        }
        $.beforeClickPolygon = polygon;
    } else if ($.beforeClickPolygon) {
        $.beforeClickPolygon.setOptions({
            "strokeWeight": 0,
        });

        $.beforeClickPolygon = undefined;
    }
}

// 가져온 zone 데이터 카카오 폴리곤 형식으로 변경
$.getPolygons = function(datas) {
    let polygons = [];
    for (let j = 0; j < datas.zonePolygons.length; j++) {
        let pointsPoly = [], obj = {};
        let zonePolygonArr = datas.zonePolygons[j].split(",");
        obj.type = datas.zoneTypes[j];
        obj.seq = datas.zoneSeqs[j];
        obj.receiptCnt = datas.receiptCnts[j];
        for (let i = 0; i < zonePolygonArr.length - 1; i++) {
            let pathPoints = zonePolygonArr[i].split(" ");
            pointsPoly[i] = new Point(pathPoints[0], pathPoints[1]);
            obj.points = pointsPoly;
        }
        // obj.coordinate = 'wgs84';
        // obj.options = polygonStyle;
        polygons.push(obj);
    }
    return polygons;
}

// serialize 맵형태로 변경
$.fn.serializeObject = function () {
    const o = {};
    const a = this.serializeArray();
    $.each(a, function () {
        const name = $.trim(this.name),
            value = $.trim(this.value);

        if (o[name]) {
            if (!o[name].push) {
                o[name] = [o[name]];
            }
            o[name].push(value || '');
        } else {
            o[name] = value || '';
        }
    });
    return o;
};