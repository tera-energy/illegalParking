<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<% String contextPath = request.getContextPath(); %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="typeValues" type="java.lang.Object[]" required="true" %>

<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="false" %>

<!-- content -->
<div class="offcanvas offcanvas-end" id="${id}" tabindex="-1" aria-labelledby="offcanvasRightLabel"
     data-bs-backdrop="false">
    <form id="formAreaSetting" name="formAreaSetting">
        <input type="hidden" id="zoneSeq" name="zoneSeq" value=""/>
        <input type="hidden" id="eventSeq" name="eventSeq" value=""/>


        <div class="offcanvas-header">
            <h5 class="offcanvas-title" id="offcanvasRightLabel">구역설정</h5>
            <button type="button" class="btn-close canvasClose" data-bs-dismiss="offcanvas" aria-label="Close"
                    id="btnCloseCross"></button>
        </div>

        <div class="offcanvas-body">

            <div class="card mb-2">
                <div class="card-header">
                    불법주정차 구역 타입
                </div>
                <div class="card-body">
                    <c:forEach items="${typeValues}" var="typeValue" varStatus="status">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="illegalType" id="zone${status.index}"
                                   value="${typeValue}" ${id eq 'areaViewModal' ? 'disabled' : ''}>
                            <label class="form-check-label" for="zone${status.index}">${typeValue.value}</label>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="card mb-2">
                <div class="card-header">
                    불법주정차 그룹지정
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-6">
                            <select class="form-select" id="locationType"
                                    name="locationType" ${id eq 'areaViewModal' ? 'disabled' : ''}>
                                <c:forEach items="${enumValues}" var="enumValue">
                                    <option value="${enumValue}"
                                            <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-6">
                            <select class="form-select" id="name"
                                    name="name" ${id eq 'areaViewModal' ? 'disabled' : ''}></select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card mb-2 findSelect">
                <div class="card-header">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="" id="usedFirst"
                               name="usedFirst" ${id eq 'areaViewModal' ? 'disabled' : ''}>
                        <label class="form-check-label" for="usedFirst">
                            첫번째 적용시간
                        </label>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row mt-2" id="firstTimeRow">
                        <div class="col-5">
                            <tags:selectTagWithSeperateTime id="firstStartTime" title="시작"/>
                        </div>
                        <div class="col">~</div>
                        <div class="col-5">
                            <tags:selectTagWithSeperateTime id="firstEndTime" title="종료"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="" id="usedSecond"
                               name="usedSecond" ${id eq 'areaViewModal' ? 'disabled' : ''}>
                        <label class="form-check-label" for="usedSecond">
                            두번째 적용시간
                        </label>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row mt-2" id="secondTimeRow">
                        <div class="col-5">
                            <tags:selectTagWithSeperateTime id="secondStartTime" title="시작"/>
                        </div>
                        <div class="col">~</div>
                        <div class="col-5">
                            <tags:selectTagWithSeperateTime id="secondEndTime" title="종료"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="offcanvas-footer">
            <div class="row ms-2">
                <div class="input-group">
                    <c:if test="${id eq 'areaSettingModal'}">
                        <button type="button" class="btn btn-primary" id="btnSetEvent">등록</button>
                    </c:if>
                    <button type="button" class="btn btn-outline-secondary canvasClose" data-bs-dismiss="offcanvas"
                            id="btnClose">${id eq 'areaViewModal' ? '닫기' : '취소'}</button>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="application/javascript">
    const $locationType = $('#locationType');
    const $btnSetEvent = $('#btnSetEvent');
    const $usedFirst = $('#usedFirst');
    const $usedSecond = $('#usedSecond');

    // 구역 Event Time 적용시간 켜기 / 끄기 함수
    function setTimeSelectDisabled(_this) {
        const usedType = _this.id.substring(4).toLowerCase();
        const $id = $('#' + _this.id);

        $('#' + usedType + 'StartTimeHour').attr('disabled', !$id.is(':checked'));
        $('#' + usedType + 'StartTimeMinute').attr('disabled', !$id.is(':checked'));
        $('#' + usedType + 'EndTimeHour').attr('disabled', !$id.is(':checked'));
        $('#' + usedType + 'EndTimeMinute').attr('disabled', !$id.is(':checked'));
    }

    // 구역 Event Time 설정 함수
    function setEventTime(type, startTime, endTime) {
        $(`\${type}StartTimeHour`).val(startTime[0]).prop('selected', true);
        $(`\${type}StartTimeMinute`).val(startTime[1]).prop('selected', true);
        $(`\${type}EndTimeHour`).val(endTime[0]).prop('selected', true);
        $(`\${type}EndTimeMinute`).val(endTime[1]).prop('selected', true);
    }

    // 구역 Event Time 초기화 함수
    function initializeEventTime(obj) {
        setEventTime('#first',
            obj?.firstStartTime ? obj?.firstStartTime.split(':') : ['12', '00'],
            obj?.firstEndTime ? obj?.firstEndTime.split(':') : ['14', '00']
        );
        setEventTime('#second',
            obj?.secondStartTime ? obj?.secondStartTime.split(':') : ['20', '00'],
            obj?.secondEndTime ? obj?.secondEndTime.split(':') : ['08', '00']
        );
    }

    function groupNameRemoveAndSet(locationType){
        $("#name option").remove();
        $.setGroupNames(locationType);
    }

    function btnSetControl($id, is) {
        $id.text(is ? '수정' : '등록');
        $id.addClass(is ? 'btn-danger' : 'btn-primary');
        $id.removeClass(is ? 'btn-primary' : 'btn-danger');
    }

    function initializeEventForm() {
        $('input:radio[name=illegalType]').eq(0).prop('checked', true);

        $('#locationType option:eq(0)').prop('selected', true);
        groupNameRemoveAndSet($locationType.val());

        $('.timeSelect').attr('disabled', true);
        $usedFirst.prop('checked', false);
        $usedSecond.prop('checked', false);

        btnSetControl($btnSetEvent, false);
    }

    function setEventForm(data) {
        data.usedFirst === false ? $usedFirst.prop('checked', true) : $usedFirst.prop('checked', false);
        data.usedSecond === false ? $usedSecond.prop('checked', true) : $usedSecond.prop('checked', false);

        $('input:radio[name=illegalType]:input[value=' + data.illegalType + ']').prop('checked', true);

        $('#eventSeq').val(data.eventSeq);

        $locationType.val(data.locationType).prop('selected', true);
        groupNameRemoveAndSet(data.locationType);
        $('#name').val(data.groupSeq).prop('selected', true);

        setTimeSelectDisabled($usedFirst[0]);
        setTimeSelectDisabled($usedSecond[0]);

        btnSetControl($btnSetEvent, true);
    }


    // 구역 Event Form 설정 함수
    function setEventHtml(data) {
        const timeObj = {
            firstStartTime: data.firstStartTime,
            firstEndTime: data.firstEndTime,
            secondStartTime: data.secondStartTime,
            secondEndTime: data.secondEndTime,
        }

        $('#offcanvasRightLabel').text(data.zoneSeq + '번 구역설정');

        initializeEventTime(timeObj);

        if (data.eventSeq !== null) {
            return setEventForm(data);
        } else {
            return initializeEventForm();
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

    $(function () {
        // 위치 변경 이벤트
        $locationType.on('change', function () {
            let locationType = $(this).val();
            groupNameRemoveAndSet(locationType);
        });

        // x 버튼, 취소 버튼 이벤트 설정
        $('#btnCloseCross, #btnClose').on('click', function () {
            $.SetMaxLevel($.MAP_MAX_LEVEL);
            $.changeOptionStroke();
        });

        // 구역 이벤트 설정
        $btnSetEvent.on('click', function () {

            if ($('#name').val() === null) {
                alert("불법주정차 그룹을 선택하세요.");
                return;
            }

            if (confirm("설정하시겠습니까?")) {
                let form = $('#formAreaSetting').serializeObject();

                form['usedFirst'] = !$('#usedFirst').is(':checked');
                form['usedSecond'] = !$('#usedSecond').is(':checked');
                form['firstStartTime'] = $('#firstStartTimeHour').val() + ':' + $('#firstStartTimeMinute').val();
                form['firstEndTime'] = $('#firstEndTimeHour').val() + ':' + $('#firstEndTimeMinute').val();
                form['secondStartTime'] = $('#secondStartTimeHour').val() + ':' + $('#secondStartTimeMinute').val();
                form['secondEndTime'] = $('#secondEndTimeHour').val() + ':' + $('#secondEndTimeMinute').val();

                let result = $.JJAjaxAsync({
                    url: _contextPath + '/event/addAndModify',
                    data: form
                });

                if (result.success) {
                    $.beforeClickPolygon = undefined;
                    $.clickedPolygon.setOptions({
                        strokeWeight: 0,
                    });
                    $.clickedPolygon.type = form['illegalType'];
                    $.SetMaxLevel($.MAP_MAX_LEVEL);
                    $.removePolygonOnMap($.clickedPolygon);
                    alert("설정되었습니다.");
                    setTimeout(function () {
                        $.addPolygonOnMap($.clickedPolygon, $.drawingMap);
                        $('#areaSettingModal').offcanvas('hide');
                    }, 300);

                } else {
                    alert(result.msg);
                }
            }
        });

        $usedFirst.on('change', function () {
            setTimeSelectDisabled(this);
        });

        $usedSecond.on('change', function () {
            setTimeSelectDisabled(this);
        });

        $(document).keydown(function (event) {
            if (event.keyCode === 27 || event.which === 27) {
                $.changeOptionStroke();
            }
        });

    });

</script>