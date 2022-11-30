// 동기 json to json 통신
$.JJAjaxAsync = function (opt) {
    let result = '';

    if (opt === undefined) {
        return result;
    }

    $.ajax({
        url: opt.url,
        type: 'post',
        async: false,
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(opt.data),
        dataType: "json",
        beforeSend: function (xhr, options) {
            xhr.setRequestHeader('AJAX', true);
        },
        xhr: function () {
            let myXhr = $.ajaxSettings.xhr();
            return myXhr;
        },
        error: function (jqXHR, statusCode, errorThrown) {
            console.log(jqXHR);
            console.log(statusCode);
            console.log(errorThrown);
        },
        success: function (data, statusCode, jqXHR) {
            result = data;
        }
    });

    return result;
}

// 비동기 json to json 통신
$.JJAjaxSync = function (opt) {
    if (opt === undefined) {
        opt.success("");
        return;
    }

    $.ajax({
        url: opt.url,
        type: 'post',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(opt.data),
        dataType: "json",
        beforeSend: function (xhr, options) {
            xhr.setRequestHeader('AJAX', true);
        },
        xhr: function () {
            let myXhr = $.ajaxSettings.xhr();
            return myXhr;
        },
        error: function (jqXHR, statusCode, errorThrown) {
            opt.error(statusCode);
        },
        success: function (data, statusCode, jqXHR) {
            opt.success(data);
        }
    });
}

// 파일 업로드
$.fn.fileUpload = function (opt) {

    let formData = new FormData();

    if (opt.description !== undefined) {
        formData.append("description", opt.description);
    }

    $(this).find(':file').each(function () {
        let key = $(this).attr("name");
        if (key == undefined) {
            return;
        }
        $.each($(this)[0].files, function (index, file) {
            formData.append(key, file);
        });
    });

    $.ajax({
        url: opt.contextPath,
        type: 'post',
        processData: false,
        contentType: false,
        data: formData,
        dataType: "json",
        beforeSend: function (xhr, options) {
            xhr.setRequestHeader('AJAX', true);
        },
        xhr: function () {
            let myXhr = $.ajaxSettings.xhr();
            return myXhr;
        },
        error: function (jqXHR, statusCode, errorThrown) {
            console.log("====================== err =========================");
            console.log(jqXHR.status);
            console.log(statusCode, errorThrown);
            console.log(errorThrown);
            console.log("====================== err =========================");
            alert(" 실패 하였습니다. 상태 코드 : " + jqXHR.status);
        },
        success: function (data, statusCode, jqXHR) {
            console.log("jqXHR.status = ", jqXHR.status);
            console.log("data = ", JSON.stringify(data));
            alert(" 업로드 되었습니다. ");
        }
    });
}

// 데이터 가져오기
$.getData = function (id) {
    let arr = $('#' + id).serializeArray();
    let data = {};
    $(arr).each(function (index, obj) {
        data[obj.name] = obj.value;
    });
    return data;
}

$.formatDateYYYYMMDD = function (date) {
    let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
};

$.formatDateYYYYMMDDhhmm = function (date) {
    let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear(),
        hh = d.getHours(),
        mm = d.getMinutes();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    if (hh.length < 2) hh = '0' + hh;
    if (mm.length < 2) mm = '0' + mm;

    let dataFormat = [year, month, day].join('-')
    let timeFormat = [hh,mm].join(":");
    return dataFormat + " " + timeFormat;
};

// filterTagByButton
$.eventFilterTagByButton = function (id, callback) {
    $('#' + id).find('a').on('click', function () {

        $('#' + id).find('a').each(function () {
            let className = $(this).attr('class');
            if ( className.indexOf("btn-outline-") == -1 ) {
                $(this).removeClass(className);
                className = className.replace('btn-','btn-outline-');
                $(this).addClass(className);
            }
        });

        // 클래스 가져오기
        let className = $(this).attr('class');
        $(this).removeClass(className);
        className = className.replace('btn-outline-', 'btn-' );
        $(this).addClass(className);

        // input 값 넣기
        $('#' + id).find('input').val($(this).attr('id'));

        // 콜백 함수 실행
        if ( callback !== undefined && typeof callback == "function") {
            callback();
        }

    });
}