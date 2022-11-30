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
<%@ taglib prefix="layoutTags" tagdir="/WEB-INF/tags/layout" %>
<% String contextPath = request.getContextPath(); %>

<!-- content -->
<main id="noticeView">
	<div class="container-fluid px-4">

		<h1 class="mt-4">공지사항</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > 공지사항</li>
		</ol>

		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header">
				<div class="row">
					<div class="col-9">
						<i class="fas fa-pen"></i> 공지 사항
					</div>
					<div class="col-3 d-flex justify-content-end">
						<a class="btn btn-close" id="noticeViewClose"></a>
					</div>
				</div>
			</div>

			<div class="card-body">
				<form id="noticeViewForm">
					<div class="row">
						<div class="col-12 me-0 border-end-0 pe-0">
							<input type="hidden" id="noticeSeqView" name="noticeSeq">
							<input type="hidden" id="noticeTypeView" name="noticeType">
							<div class="input-group">
								<span class="input-group-text text-primary" id="noticeTypeValueView" style="border: 0px; background: #ffffff;"></span>
								<input type="text" id="subjectView" name="subject" class="form-control d-flex justify-content-lg-start" style="border: 0px; background: #ffffff;" disabled>
							</div>
						</div>
					</div>
					<div class="row mb-3 mt-0 pb-2 border-bottom">
						<div class="col ms-5 ps-4">
							<input type="text" id="regDtView" name="regDtView" class="h6" style="border: 0px; background: #ffffff; color: #999999; font-size: 11px;" disabled>
						</div>
					</div>
					<div class="row mb-2">
						<div class="col ms-1">
							<div id="contentView" name="content"></div>
						</div>
					</div>
					<div class="row">
						<div class="col-12 d-flex justify-content-lg-start">
							<div class="input-group">
								<a class="btn btn-primary" id="modify">수정</a>
								<a class="btn btn-danger" id="remove">삭제</a>
								<a class="btn btn-outline-dark" id="close">닫기</a>
							</div>
						</div>
					</div>
				</form>
			</div>

		</div>

	</div>
</main>

<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<script type="application/javascript">
    $(function () {

        // 공지사항 수정 함수
        function changeNoticeModify () {
            let result = $.JJAjaxAsync({
				url: _contextPath + '/get',
				data: {
                    noticeSeq: $('#noticeSeqView').val()
				}
			});

            if ( result.success ) {
                $.initializeNoticeSet(result.data);
                $('#noticeSet').show();
                $('#noticeView').hide();
			} else {
                alert(result.msg);
			}
        }

        // 공지사항 삭제 함수
        function removeNotice (noticeSeq) {
            if (noticeSeq === undefined) {
                alert("삭제 할 수 없습니다.");
                return;
            }

            if (confirm("삭제 하시겠습니까?")) {

                let result = $.JJAjaxAsync({
                    url: _contextPath + '/remove',
                    data: {
                        noticeSeq: noticeSeq
                    }
                });

                if (result.success) {
                    $.search();
                } else {
                    alert("삭제 할 수 없습니다.");
                }
            }
        }

        // 공시 사항 보기 닫기 이벤트
        $('#noticeViewClose, #close').on('click', function () {
            $.closeNoticeView();
        });

        // 수정
        $('#modify').on('click', function () {
            changeNoticeModify();
        });

        // 삭제
        $('#remove').on('click', function () {
            removeNotice($('#noticeSeqView').val());
        });

    });
</script>

