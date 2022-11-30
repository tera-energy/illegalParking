$(function (){

    // 데이터 가져오기
    function getData(){
        let arr = $('#data').serializeArray();
        let data = {};
        $(arr).each(function(index, obj){
            data[obj.name] = obj.value;
        });
        return data;
    }

    function initialize(){

        // 엑셀 등록
        $('#excelUpload').on('click', function () {
            $('#excelFile').parent().fileUpload({
                contextPath: location.host + "/files/parking"
            });
        });

        // 엑셀 양식 다운로드
        $('#excelDownload').on('click', function () {
            $('#inputGroupFile04').parent().fileUpload({
                contextPath: location.host + "/files/parking"
            });
        });

        // 단일 등록
        $('#register').on('click', function (){
            if ( confirm("등록 하시겠습니까?") ) {
                // $.JJAjaxSync({
                //     url: _contextPath + '/set',
                //     data: getData(),
                //     success: function (){
                //         if ( confirm(" 계속 등록 하시겠습니까? " ) ) {
                //             location.href = location.href;
                //         } else {
                //             location.href = _contextPath + '/parkingList';
                //         }
                //     } ,
                //     err: function (code){
                //         alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                //     }
                // });
            } else {
                log(getData());
            }
        });

    }

    initialize();

});