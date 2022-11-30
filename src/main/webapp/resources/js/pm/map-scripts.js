$(function () {

    // 지도 div
    let mapContainer = document.getElementById('map');
    let map;
    let kakaoEvent = kakao.maps.event;
    let pmOverlay = new kakao.maps.CustomOverlay({zIndex:1, yAnchor: 3 });

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;

    // 카카오 맵 이벤트 설정
    function setKakaoEvent(opt) {
        kakaoEvent.addListener(opt.target, opt.event, opt.func);
    }

    function getsPm(codes) {
        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        let result = $.JJAjaxAsync({
            url: _contextPath + '/gets',
            data: {
                codes: codes
            }
        });

        if (result.success) {
            $.removeTextOverlays();
            result.data.forEach(function (data) {
                $.addOverlay(data, map, displayPmInfo);
            });
            $.beforeCodes = codes;
        }
    }

    // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
    function displayPmInfo (pm) {
        let contentNode = document.createElement('div'); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
        // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
        contentNode.className = 'parkingInfo_wrap';

        let infoNode = document.createElement('div');
        infoNode.className = 'parkingInfo';

        let title = document.createElement('div');
        title.className = 'title';
        title.appendChild(document.createTextNode(pm.pmName));

        let closeBtn = document.createElement('div');
        closeBtn.className = 'close';
        closeBtn.onclick = function () {
            pmOverlay.setMap(null);
        };
        title.appendChild(closeBtn);

        let jibun = document.createElement('span');
        jibun.className = 'jibun';
        jibun.appendChild(document.createTextNode(pm.pmId));
        title.appendChild(jibun);
        infoNode.appendChild(title);

        let addr = document.createElement('span');
        addr.className = 'addr';
        addr.appendChild(document.createTextNode(`대여료 : ${pm.pmPrice}원`));
        infoNode.appendChild(addr);

        let price = document.createElement('span');
        price.className = 'price';
        price.appendChild(document.createTextNode(`운영시간 : ${pm.pmOperOpenHhmm} ~ ${pm.pmOperCloseHhmm}`));
        infoNode.appendChild(price);

        contentNode.appendChild(infoNode);
        let after = document.createElement('div');
        after.className = 'after';
        contentNode.appendChild(after);

        pmOverlay.setPosition(new kakao.maps.LatLng(pm.latitude, pm.longitude));
        pmOverlay.setContent(contentNode)
        pmOverlay.setMap(map);
    }

    // 카카오 초기화
    function initializeKakao() {
        map = {
            center: new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE), // 지도의 중심좌표
            level: 3, // 지도의 확대 레벨
            disableDoubleClickZoom: true
        };

        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        map = new kakao.maps.Map(mapContainer, map);

        setKakaoEvent({
            target: map,
            event: 'click',
            func: function (mouseEvent) {
                pmOverlay.setMap(null);
                $.initOverlay();
            }
        });

        // 확대수준이 변경되거나 지도가 이동했을때 타일 이미지 로드가 모두 완료되면 발생
        setKakaoEvent({
            target: map,
            event: 'idle',
            func: async function () {
                // 지도의  레벨을 얻어옵니다
                let level = map.getLevel();

                if (level < 4) {
                    let obj = await $.getDongCodesBounds(map);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    if (!obj.uniqueCodesCheck) {
                        await getsPm(obj.codes);
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
                    pmOverlay.setMap(null);
                    $.removeTextOverlays();
                } else {
                    let obj = await $.getDongCodesBounds(map);
                    if (level === 3) {
                        await getsPm(obj.codes);
                    } else {
                        if (!obj.uniqueCodesCheck) {
                            await getsPm(obj.codes);
                        }
                    }
                }
            }
        });

        setKakaoEvent({
            target: map,
            event: 'dragend',
            func: function () {
                pmOverlay.setMap(null);
                $.initOverlay();
            }
        });

        $.getCurrentPosition(map);

    }

    initializeKakao();



});
