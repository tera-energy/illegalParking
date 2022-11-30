<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.user.enums.Role" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<% String contextPath = request.getContextPath(); %>
<stripes:layout-render name="/WEB-INF/views/layout/navHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<!-- side -->
	<stripes:layout-component name="side">
		<jsp:include page="side.jsp" flush="true"/>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main id="parkingTable">
			<div class="container-fluid px-4">
				<h1 class="mt-4">내정보</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active"> 내정보</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<div class="row">
							<div class="col-9">
								<i class="fas fa-table me-1"></i> 내정보
							</div>
							<div class="col-3 d-flex justify-content-end">
								<a class="btn btn-close" id="myInfoClose"></a>
							</div>
						</div>
					</div>
					<div class="card-body">
						<form class="row mb-3">
							<div class="row">
								<div class="col-4 mb-2">
									<tags:inputTag id="username" title="계정" placeholder=""/>
								</div>
							</div>
							<div class="row">
								<div class="col-4 mb-2">
									<tags:inputTagWithPassword id="password" title="패스워드" placeholder=""/>
								</div>
							</div>
							<div class="row">
								<div class="col-4 mb-2">
									<tags:selectTagWithType id="role" current="" items="${Role.values()}" title="권한"/>
								</div>
							</div>
							<div class="row mt-3">
								<div class="col-2 btn-group">
									<a class="btn btn-primary" id="modify">수정</a>
									<a class="btn btn-outline-dark" id="close">닫기</a>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</main>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/myInfo/myInfo-scripts.js"></script>
		<script type="application/javascript">

            $(function () {

                let setPasswoardUi = function () {
                    let $eyeIcon = $('#eye').children();
                    let className = $eyeIcon.attr('class');
                    if (className.indexOf('fa-eye-slash') > -1) {
                        $eyeIcon.attr('class', className.replace('fa-eye-slash', 'fa-eye'));
                        $('#password').attr('type', "text");
                    } else {
                        $eyeIcon.attr('class', className.replace('fa-eye', 'fa-eye-slash'));
                        $('#password').attr('type', 'password');
                    }
                };

                let setMyinfo = function () {
                    let result = $.JJAjaxAsync({
                        url: _contextPath + '/myInfo/get',
                        data: {
                            userSeq: _userSeq
                        }
                    });

                    if (result.success) {
                        let user = result.data;
                        $('#username').val(user.username);
                        $('#password').val(user.password);
                        $('#role').val(user.role);
                    } else {
                        alert("사용자 정보를 가져오는데 실패 하였습니다.");
                    }
                }

                // 초기화
                let initialize = function () {

                    $('#eye').on('click', function () {
                        setPasswoardUi();
                    });

                    $('#close, #myInfoClose').on('click', function () {
                        location.href = "/";
                    });

                    setMyinfo();

                }

                initialize();
            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>