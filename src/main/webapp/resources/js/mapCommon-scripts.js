// serialize 맵형태로 변경
$.fn.serializeObject = function(){
    const o = {};
    const a = this.serializeArray();
    $.each(a, function() {
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

/******************************
 * 폴리곤 내 포함 여부 체크 start
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

function isInside(polygon, n, p) {

    polygon.push(polygon[0]);

    // There must be at least 3 vertices in polygon[]
    if (n < 3) {
        return false;
    }

    let INF = 10000;

    // Create a point for line segment from p to infinite
    let extreme = new Point(INF, p.y);

    // Count intersections of the above line
    // with sides of polygon
    let count = 0, i = 0;
    do {
        let next = (i + 1) % n;

        // Check if the line segment from 'p' to
        // 'extreme' intersects with the line
        // segment from 'polygon[i]' to 'polygon[next]'
        if (doIntersect(polygon[i], polygon[next], p, extreme)) {
            // If the point 'p' is collinear with line
            // segment 'i-next', then check if it lies
            // on segment. If it lies, return true, otherwise false
            if (orientation(polygon[i], p, polygon[next]) === 0) {
                return onSegment(polygon[i], p,
                    polygon[next]);
            }

            count++;
        }
        i = next;
    } while (i !== 0);

    // Return true if count is odd, false otherwise
    return (count % 2 === 1); // Same as (count%2 == 1)
}
/*폴리곤 내 포함 여부 체크 end*/


/*폴리곤 중심 좌표 구하기*/
function centroid(points) {
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

    return {x : x / area, y : y / area};
}
/*폴리곤 중심 좌표 구하기 end*/

/* 좌표로 동코드 받기 카카오 REST API */
async function coordinatesToDongCodeKakaoApi(x, y, stat){
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

// function coordinatesToDongCodeKakaoApi(x, y, stat){
//     let geocoder = new kakao.maps.services.Geocoder();
//     let callback = function (result, status) {
//         log(result)
//         if (status === kakao.maps.services.Status.OK) {
//             log('지역 명칭 : ' + `${result[0].region_1depth_name} ${result[0].region_2depth_name} ${result[0].region_3depth_name} ${result[0].region_4depth_name}`);
//             log('행정구역 코드 : ' + result[0].code);
//
//             log(x);
//
//             if(stat === 'first') beforeCode = result[0].code;
//             else currentCode = result[0].code;
//         }
//     };
//     geocoder.coord2RegionCode(x, y, callback);
//
//     if (stat === 'first') return beforeCode;
//     else return currentCode;
// }
/* 좌표로 동코드 받기 카카오 REST API end */

let myMarker = {
    imgSrc: '/resources/assets/img/location_shadow.png',
    imgSize: new kakao.maps.Size(50,50)
};
let markerImg = new kakao.maps.MarkerImage(myMarker.imgSrc, myMarker.imgSize);
let myLocMarker = null;
let beforeCodes = [];
let isFirst = true; // 최초 인가 확인
let gpsLatitude = 0.0;
let gpsLongitude = 0.0;
let bearing = 0;

//geoLocation API를 활용한 현재 위치를 구하고 지도의 중심 좌표 변경
$.getCurrentPosition = function(map) {
    if(!!myLocMarker) myLocMarker.setMap(null);

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            async (position) => {
                let currentLat = `${position.coords.latitude}`; // y
                let currentLng = `${position.coords.longitude}`; // x
                // 지도 중심좌표를 접속위치로 변경합니다
                let currentPosition = new kakao.maps.LatLng(currentLat, currentLng);
                log(currentPosition, typeof(currentLat));

                myLocationMarker(map, currentPosition);

            },
            (error) => {
                console.error(error);
            },
            {
                enableHighAccuracy: true, // 위치정보를 가장 높은 정확도로 수신 true, 기본 false, 응답이 느리고 전력소모량 증가
                maximumAge: 0,
                timeout: Infinity,
            }
        );
    } else {
        let position = new kakao.maps.LatLng(35.01868444, 126.78284599);
        alert("위치 권한을 설정하시기 바랍니다.");
        map.panTo(position);
    }
}

//geoLocation API를 활용한 현재 위치를 구하고 지도의 중심 좌표 변경
$.getMobileCurrentPosition = function(map) {
    if(!!myLocMarker) myLocMarker.setMap(null);
    gpsLatitude = gpsLatitude === 0 ? '35.01868444' : gpsLatitude.toString();
    gpsLongitude = gpsLongitude === 0 ? '126.78284599' : gpsLongitude.toString();
    let gpsPosition = new kakao.maps.LatLng(gpsLatitude, gpsLongitude);
    myLocationMarker(map, gpsPosition);
}

//현재위치 마커 커스텀 오버레이
function myLocationMarker(map, position){
    let locationOverlay = new kakao.maps.CustomOverlay({zIndex:1, yAnchor: 3 });

    let outlineNode = document.createElement('div');
    outlineNode.className = 'outline';

    let img = document.createElement('img');
    img.id = 'location';
    img.src = `/resources/assets/img/location.png`;
    // img.style.transform = 'rotate(' + bearing + 'deg)';

    outlineNode.appendChild(img);

    let waves = document.createElement('div');
    waves.className = 'waves';
    outlineNode.appendChild(waves);

    // 커스텀 오버레이를 지도에 표시합니다
    locationOverlay.setPosition(position);
    locationOverlay.setContent(outlineNode)
    locationOverlay.setMap(map);
    myLocMarker = locationOverlay;
    map.panTo(position);
}


// for (var j in markerArray){
//     var marker = new daum.maps.Marker({
//         map: map,
//         position: markerArray[j].latlng,
//         title : markerArray[j].title,
//         image : iconImage
//     });
// $(marker.Na).css('transform','');
// $(marker.Na).css('transform','rotateZ('+markerArray[j].azimuth+'deg)');
// array.push(marker);



$.gpsPoint = function(x, y, _bearing) {
    gpsLatitude = x;
    gpsLongitude = y;
    bearing = _bearing;
    $('#test').val(x + "," + y + " :: " + (typeof x));

    log('webview', gpsLatitude, gpsLongitude);
}

// 꼭지점, 중심좌표의 법정동 코드 가져오기
$.getDongCodesBounds = async function (map){
    let uniqueCodesCheck = false;

    // 맵 구역
    let bounds = map.getBounds();
    // 영역정보의 남서쪽 정보를 얻어옵니다
    let swLatLng = bounds.getSouthWest();
    let south = swLatLng.getLat();
    let west = swLatLng.getLng();

    // 영역정보의 북동쪽 정보를 얻어옵니다
    let neLatLng = bounds.getNorthEast();
    let north = neLatLng.getLat();
    let east = neLatLng.getLng();

    // 동, 서, 남, 북 좌표
    // log(east, west, south, north);
    let center = map.getCenter();

    let latLngs = [{x: east, y: north}, {x: west, y: north}, {x: east, y: south}, {x: west, y: south}, {x: center.getLng(), y: center.getLat()}]
    let codes = [];

    for (const latLng of latLngs) {
        let code = await coordinatesToDongCodeKakaoApi(latLng.x, latLng.y);
        codes.push(code);
    }
    codes = [...new Set(codes)]

    // log('codes : ', codes);
    // log('beforeCodes : ', beforeCodes);
    // log('uniqueCodesCheck : ', uniqueCodesCheck);

    uniqueCodesCheck = _.isEmpty(_.xor(beforeCodes, codes));

    return {
        codes: codes,
        uniqueCodesCheck: uniqueCodesCheck
    };
}

// 선과 다각형의 꼭지점 정보를 kakao.maps.LatLng객체로 생성하고 배열로 반환하는 함수입니다
$.pointsToPath = function (points) {
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
$.changeOptionByMouseOut = function (area) {
    return {
        fillColor: $.setFillColor(area), // 채우기 색깔입니다
        fillOpacity: 0.5 // 채우기 불투명도 입니다
    };
}

// 주정차 타입에 따른 폴리곤 색 구별
$.setFillColor = function(area) {
    let fillColor;
    if (area.type === 'FIVE_MINUTE') fillColor = '#ff6f00';
    else if (area.type === 'ILLEGAL') fillColor = '#FF3333';
    else fillColor = '#00afff';

    return fillColor;
}

// 방위각 구하기
$.getBearing = function (src_lat, src_lng, dest_lat, dest_lng) {
    let src_lat_rad = src_lat * ( Math.PI / 180);
    let src_lng_rad = src_lng * ( Math.PI / 180);
    let dest_lat_rad = dest_lat * ( Math.PI / 180);
    let dest_lng_rad = dest_lng * ( Math.PI / 180);

    // 각도 거리
    let rad_distance = 0;
    rad_distance = Math.acos(Math.sin(src_lat_rad) *  Math.sin(dest_lat_rad) + Math.cos(src_lat_rad) *  Math.cos(dest_lat_rad) * Math.cos(src_lng_rad - dest_lng_rad));

    // 목적지 이동 방향
    let rad_bearing = Math.acos((Math.sin(dest_lat_rad) - Math.sin(src_lat_rad) * Math.cos(rad_distance)) / (Math.cos(src_lat_rad) * Math.sin(rad_distance)));

    let true_bearing = 0;
    true_bearing = rad_bearing * ( 180 / Math.PI);
    if ( Math.sin(dest_lng_rad - src_lng_rad) < 0) {
        true_bearing = 360 - true_bearing;
    }

    return true_bearing;
}

$.getAngle = function (center, target) {
    let dx = target.x - center.x;
    let dy = center.y - target.y ;
    let deg = Math.atan2( dy , dx ) * 180 / Math.PI;

    return (-deg + 450) % 360 | 0;
}