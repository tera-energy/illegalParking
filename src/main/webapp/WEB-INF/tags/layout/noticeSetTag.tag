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
<%@ attribute name="items" type="java.lang.Object" required="true" %>

<!-- content -->
<main id="noticeSet">
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
						<a class="btn btn-close" id="noticeSetClose"></a>
					</div>
				</div>
			</div>

			<div class="card-body">
				<form id="noticeSetForm">
					<input type="hidden" id="noticeSeq" name="noticeSeq" value="">
					<div class="row mb-3">
						<div class="col-2">
							<div class="input-group">
								<label class="input-group-text" for="noticeType">분류 </label>
								<tags:selectTagWithType id="noticeType" current="" items="${items}"/>
							</div>
						</div>
					</div>
					<div class="row mb-3">
						<div class="col-12">
							<div class="input-group">
								<label class="input-group-text" for="subject">제목 </label>
								<tags:inputTag id="subject" placeholder="제목을 입력하세요"/>
							</div>
						</div>
					</div>
					<div class="row mb-2">
						<div class="col-12">
							<div class="form-group">
								<div id="editor" name="editor"></div>
								<input type="hidden" id="toastdata" name="toastdata"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-12 d-flex justify-content-lg-start">
							<div class="input-group">
								<a class="btn btn-primary" id="register">등록</a>
								<a class="btn btn-outline-dark" id="noticeSetCloseBtn">닫기</a>
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

        // 글작성 닫기
        $('#noticeSetClose, #noticeSetCloseBtn').on('click', function () {
            $.initializeNoticeSet({
                noticeSeq: '',
                subject: '',
                noticeType: 'DISTRIBUTION',
                html:''
			});
            $.closeNoticeSet();
        })

        // 에디터 클래스 생성
        const editor = new toastui.Editor({
            el: document.querySelector('#editor'),
            height: '500px',
            initialEditType: 'wysiwyg',
            previewStyle: 'vertical',
            hooks: {
                addImageBlobHook: async (blob, callback) => {
                    const upload = uploadImage(blob);
                    callback(upload, 'alt text');
                }
            }
        });

        // 에디터 이미지
        const uploadImage = (blob) => {
            const formData = new FormData();
            formData.append('image', blob);
            // 서버로부터 이미지 주소 받아오는거 구현 필요
            const url = "https://www.premiumoutlets.co.kr/webcontents/20220127_144158_220124_slide_01.jpg";
            return url;
        };

        // 등록 이벤트
        $('#register').on('click', function () {
            register('noticeSetForm');
        });

        // 글작성 에디터 초기화 함수
        $.initializeNoticeSet = function (opt) {
            $('#noticeSeq').val(opt.noticeSeq);
            $('#subject').val(opt.subject);
            $('#noticeType').val(opt.noticeType);
            editor.setHTML(opt.html);
        }

        // 등록 함수
        function register(id) {
            let data = $.getData(id);
            data.html = editor.getHTML();
            data.content = data.html.replaceAll('<p>', '').replaceAll('</p>', '\n').replaceAll('<br>', '\n');
            data.content = data.content.replaceAll('<table>', '').replaceAll('</table>', '');
            data.content = data.content.replaceAll('<thead>', '').replaceAll('</thead>', '');
            data.content = data.content.replaceAll('<th>', '').replaceAll('</th>', '\n');
            data.content = data.content.replaceAll('<tbody>', '').replaceAll('</tbody>', '');
            data.content = data.content.replaceAll('<td>', '').replaceAll('</td>', '');
            data.content = data.content.replaceAll('<tr>', '').replaceAll('</tr>', '\n');
            data.userSeq = _userSeq;

            let result = $.JJAjaxAsync({
                url: _contextPath + '/set',
                data: data
            });

            if (result.success) {
                $.closeNoticeSet();
                $.search();
            } else {
                alert(result.msg);
            }
        }
    });

</script>

