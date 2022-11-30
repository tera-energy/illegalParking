// 그룹 추가 팝업창 닫기
$.closeGroupAdd = function() {
    $('#locationType').val("");
    $('#name').val("");

    $('#modalGroupAdd').hide();
    $('body').css({
        'overflow': 'auto'
    });
};

// 그룹 이벤트 팝업창 닫기
$.closeGroupEvent = function() {
    $('#limit').val("");
    $('#value').val("");
    $('#startDate').val("");
    $('#endDate').val("");

    $('#modalGroupEvent').hide();
    $('body').css({
        'overflow': 'auto'
    });
};
