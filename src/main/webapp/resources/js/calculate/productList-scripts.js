// 데이터 가져오기
 $.getDataByProduct = function(id) {
    let arr = $('#' + id).serializeArray();
    let data = {};
    $(arr).each(function (index, obj) {
        data[obj.name] = obj.value;
    });
    data.userSeq = _userSeq;
    data.pointValue = Number(data.pointValue);
    return data;
}
