$.search = function (pageNumber) {
    if (pageNumber === undefined) {
        $('#pageNumber').val("1");
    } else {
        $('#pageNumber').val(pageNumber);
    }
    location.href = _contextPath + "/userList?" + $('form').serialize();
}

// 신고 접수 건 파이 차트
$.drawPieChart = function(opt) {
    let options = {
        animationEnabled: true,
        // title: {
        //     text: "신고 건수"
        // },
        data: [{
            type: "doughnut",
            innerRadius: "40%",
            showInLegend: true,
            legendText: "{label}",
            indexLabel: "{label}",
            dataPoints: [
                {label: "대기", y: opt.completeCount},
                {label: "미처리", y: opt.exceptionCount},
                {label: "처리", y: opt.penaltyCount}
            ]
        }]
    };

    function draw() {
        $("#chartContainer").css("height", "300px").css("width", "500px");
        $("#chartContainer").CanvasJSChart(options);
        $('.canvasjs-chart-credit').hide();
    };

    setTimeout(() => {
        draw();
    }, 200);

}

// 관리 그룹 추가 이름 설정
$.setUserGroupNames = function (locationType) {
    function getNamesSelectHtml(names) {
        let html = '';
        for (let i = 0; i < names.length; i++) {
            html += '<option value="' + names[i] + '">' + names[i] + '</option>';
        }
        return html;
    }

    let result = $.JJAjaxAsync({
        url: _contextPath + '/userGroup/group/name/get',
        data: {
            locationType: locationType
        }
    });

    if (result.success) {
        let names = result.data;
        let html = getNamesSelectHtml(names);
        $('#name').append(html);
    }
}

// 관리 그룹 리스트 추가
$.addUserGroupList = function (userGroupDto) {

    let createHtml = function (userGroupDto) {
        let html = '';
        html +='<li class="nav-item">';
        html +='    <input type="hidden" value="' + userGroupDto.userGroupSeq  +  '">'
        html +='    <a class="nav-link" href="#">' + userGroupDto.name + '<i class="text-danger fa fa-times"></i></a>'
        html +='</li>'

        return html;
    }

    $('#addUserGroupNav').append(createHtml(userGroupDto));
}

// 관리 그룹 리스트 이벤트 연결 함수
$.bindUserGroupNavEvent = function (){

    $('#addUserGroupNav a').on('click', function (){
        if ( confirm("삭제 하시겠습니까") ) {

            let userGroupSeq = $(this).parent().find('input').val();

            let result = $.JJAjaxAsync({
                url: _contextPath + "/userGroup/remove",
                data: {
                    userGroupSeq: userGroupSeq
                }
            });

            if (result.success) {
                $(this).parent().remove();
                $.setStatics();
            } else {
                alert("삭제를 실패 하였습니다.");
            }
        }
    });
}

// userSetTag 의 통계 정보 초기화 함수
$.initializeStatics = function (opt) {
    $('#totalCount').text(opt.totalCount + " 건");           // 총 신고 접수 건수
    $('#completeCount').text(opt.completeCount + " 건");     // 대기 건수
    $('#exceptionCount').text(opt.exceptionCount + " 건");   // 미처리 건수
    $('#penaltyCount').text(opt.penaltyCount + " 건");       // 처리 건수

    // 차트 ( 대기 / 미처리 / 처리 건수 )
    $.drawPieChart(opt);
}

// 사용자 설정 태그 초기화 함수
$.initializeUserSetTag = function (opt) {
    $('#userSeq').val(opt.userSeq);
    $('#officeName').val(opt.officeName);
    $('#locationType').val(opt.locationType);
    $('#userName').val(opt.userName);
    $('#password').val(opt.password);

    $.initializeStatics(opt);

    let userGroupDtos = opt.userGroupDtos;

    if ( userGroupDtos != undefined) {
        for ( let i = 0; i < userGroupDtos.length; i++) {
            $.addUserGroupList(userGroupDtos[i]);
        }
    }

    // 관리 그룹 리스트 이벤트 연결
    $.bindUserGroupNavEvent();
}

// 통계 데이터 설정
$.setStatics = function () {
    $.loading.start();
    let staticResult = $.JJAjaxAsync({
        url: _contextPath + "/../report/statics/get",
        data: {
            userSeq: $('#userSeq').val()
        }
    });

    if (staticResult.success ) {
        $.initializeStatics(staticResult.data);
    }

    $.loading.stop();
}

// 로딩 이미지 화면 설정 함수
$.loading = {
    start: function(){
        $('.wrap-loading').show();
    } ,
    immediatelyStop: function (){
        $('.wrap-loading').hide();
    },
    stop: function() {
        setTimeout(function () {
            $('.wrap-loading').hide();
        }, 500);
    }
}