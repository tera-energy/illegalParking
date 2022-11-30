// 공지사항 검색
$.search = function (pageNumber) {
    if (pageNumber === undefined) {
        $('#pageNumber').val("1");
    } else {
        $('#pageNumber').val(pageNumber);
    }
    location.href = _contextPath + "/noticeList?" + $('form').serialize();
};

// 공지사항 작성 닫기
$.closeNoticeSet = function () {
    $('#noticeMain').show();
    $('#noticeSet').hide();
};

// 공지사항 작성 열기
$.openNoticeSet = function () {
    $('#noticeMain').hide();
    $('#noticeSet').show();
};

// 공지사항 보기 닫기
$.closeNoticeView = function () {
    $('#noticeMain').show();
    $('#noticeView').hide();
};

// 공지사항 보기 열기
$.openNoticeView = function () {
    $('#noticeMain').hide();
    $('#noticeView').show();

    $.setDivHeight('contentView', 450);
};

// 공지사항 보기 함수
$.initializeNoticeView = function (opt) {
    $('#noticeSeqView').val(opt.noticeSeq);
    $('#subjectView').val(opt.subject);
    $('#noticeTypeView').val(opt.noticeType);
    $('#noticeTypeValueView').text(opt.noticeTypeValue);
    $('#regDtView').val(opt.regDt);
    let content = '<div id="contentView" name="content">' + opt.html + '</div>';
    $('#contentView').replaceWith(content);
}

// 높이 조절
$.setDivHeight = function (id, max) {
    let objSet = $('#' + id);

    if (objSet.height() < max) {
        objSet.css({
            "height": "450px"
        });
    }
}