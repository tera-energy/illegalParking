<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/htmlLayout.jsp">

    <!-- content -->
    <stripes:layout-component name="contents">
        <body id="login_body">
            <div class="container container_login">
                <div class="row justify-content-center">
                    <div class="col-lg-5">
                        <div class="row login_logo">
                            <img class="logo" src="/resources/assets/img/logo_login.png">
                        </div>
                        <div class="row">
                            <div class="d-flex justify-content-center h2 text-white mb-5">불법주정차 신고관리</div>
                        </div>
                        <div class="row">
                            <form method="post" id="FormLogin" action="/loginProcess">
                                <div class="form-floating mb-2">
                                    <h5 class="input_labels">아이디</h5>
                                    <input class="input_box" name="email" id="email" value="" placeholder="아이디를 입력하세요" onfocus="this.placeholder=''" onblur="this.placeholder='아이디를 입력하세요'"/>
                                </div>
                                <hr class="devider">
                                <div class="form-floating mb-2">
                                    <h5 class="input_labels">비밀번호</h5>
                                    <input class="input_box" name="password" id="password" type="password" value="" placeholder="비밀번호를 입력하세요" onfocus="this.placeholder=''" onblur="this.placeholder='비밀번호를 입력하세요'"/>
                                </div>
                                <hr class="devider">
                                <div class="form-check mb-3">
                                    <input class="form-check-input" id="saveId" type="checkbox" value=""/>
                                    <label class="form-check-label check_id" for="saveId">아이디 저장</label>
                                </div>
                                <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                    <a class="small" href="password"></a>
                                    <a class="btn btn-dark btn-dark-login" id="BtnLogin">로그인</a>
                                </div>
                            </form>
                        </div>
                        <div class="card-footer text-center py-3">
                        </div>
                    </div>
                </div>
            </div>
            <div class="phone_num" style="color:white">문의 : 061-930-7071</div>
        </body>
    </stripes:layout-component>

    <!-- javascript -->
    <stripes:layout-component name="javascript">
        <script type="text/javascript">

            let idName = "email";

            //쿠키 저장하는 함수
            function set_cookie(name, value, exp) {
                let date = new Date();
                date.setTime(date.getTime() + exp*24*60*60*1000);
                document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
            }

            //쿠키 값 가져오는 함수
            function get_cookie(name) {
                let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
                return value? value[2] : null;
            }

            function del_cookie(name) {
                document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
            }

            $.cookie = function (key, val){
                if (val == undefined) {
                    return get_cookie(key);
                }
                set_cookie(key, val, new Date());
            }

            $.cookie.del = del_cookie;

            $(function () {
                function saveId(){
                    if ( $('#saveId').is(":checked") ) {
                        $.cookie(idName, $('#email').val() );
                    } else {
                        $.cookie.del(idName);
                    }
                }

                // 로그인
                function login(){
                    if ( $('#email').val() === '' ){
                        alert("아이디를 입력하세요.");
                        return;
                    }

                    if ($('#password').val() === '') {
                        alert("패스워드를 입력하세요.");
                        return;
                    }

                    saveId();
                    $form.submit();
                }

                let $form = $('#FormLogin');
                let $btnLogin = $('#BtnLogin');

                $btnLogin.on('click', function () {
                    login();
                });

                $('#password').on('keydown', function (e) {
                    if (e.key == 'Enter') { // Enter key
                        login();
                    }
                });

                let url = location.href;
                let path = url.split('?');
                if (path.length > 1) {
                    let state = path[1].split('=')[1];

                    if (state == 'fail') {
                        alert('인증 실패 하였습니다.');
                    }
                    location.href = path[0];
                }

                if ( $.cookie(idName) !== undefined && $.cookie(idName) !== null){
                    $('#email').val($.cookie(idName));
                    $('#saveId').attr("checked", true);
                }

            });
        </script>

    </stripes:layout-component>

</stripes:layout-render>