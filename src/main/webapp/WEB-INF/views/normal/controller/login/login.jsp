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
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-5">
                    <div class="card shadow-lg border-0 rounded-lg mt-5">
                        <div class="card-header"><h3 class="text-center font-weight-light my-4">Login</h3></div>
                        <div class="card-body">

                            <form method="post" id="FormLogin" action="/loginProcess">
                                <div class="form-floating mb-3">
                                    <input class="form-control" name="email" id="email" type="email" placeholder="user" value="admin"/>
                                    <label for="email">메일</label>
                                </div>
                                <div class="form-floating mb-3">
                                    <input class="form-control" name="password" id="password" type="password" value="qwer1234"/>
                                    <label for="password">패스워드</label>
                                </div>
                                <div class="form-check mb-3">
                                    <input class="form-check-input" id="inputRememberPassword" type="checkbox" value=""/>
                                    <label class="form-check-label" for="inputRememberPassword">패스워드 기억</label>
                                </div>
                                <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                    <a class="small" href="password">패스워드 분실?</a>
                                    <a class="btn btn-primary" id="BtnLogin">로그인</a>
                                </div>
                            </form>

                        </div>
                        <div class="card-footer text-center py-3">
                            <div class="small"><a href="register"> 계정의 없다면 회원 가입하세요!</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </stripes:layout-component>

    <!-- javascript -->
    <stripes:layout-component name="javascript">
        <script type="text/javascript">
            $(function () {

                let $form = $('#FormLogin');
                let $btnLogin = $('#BtnLogin');

                $btnLogin.on('click', function () {
                    $form.submit();
                });

                $('#inputPassword').on('keydown', function (e) {
                    if (e.key == 'Enter') { // Enter key
                        $form.submit();
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

            });
        </script>

    </stripes:layout-component>

</stripes:layout-render>