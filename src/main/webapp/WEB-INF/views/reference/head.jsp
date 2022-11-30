<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String contextPath = request.getContextPath(); %>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <title>${title}</title>
    <link rel="shortcut icon" href="<%=contextPath%>/resources/assets/img/favicon.ico">
    <link href="<%=contextPath%>/resources/css/styles.css" rel="stylesheet"/>
    <link href="<%=contextPath%>/resources/css/area/group-styles.css" rel="stylesheet"/>
    <link href="<%=contextPath%>/resources/css/user/user-styles.css" rel="stylesheet"/>
    <link href="<%=contextPath%>/resources/css/home/home-styles.css" rel="stylesheet"/>
    <link href="<%=contextPath%>/resources/css/login/login-styles.css" rel="stylesheet"/>
<%--    <link href="https://cdn.jsdelivr.net/npm/simple-datatables@latest/dist/style.css" rel="stylesheet"/>--%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.48.4/codemirror.css" />
    <c:choose>
        <c:when test="${defaultPathName eq 'notice'}">
            <link href="<%=contextPath%>/resources/css/notice/notice-styles.css" rel="stylesheet"/>
            <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
        </c:when>
    </c:choose>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/lodash@4.17.21/lodash.min.js"></script>

    <script src="<%=contextPath%>/resources/js/scripts.js"></script>

    <script type="application/javascript">
        // contextPath 구하기
        $.getContextPath = function () {
            let hostIndex = location.href.indexOf(location.host) + location.host.length;
            return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
        }

        const _contextPath = $.getContextPath();
        <%--const _contextPath = '${pageContext.request.contextPath}';--%>

        let SELECT_ALL = "all";
        let SELECT_TYPE = "type";
        let SELECT_DONG = "dong";
        let SELECT_TYPE_AND_DONG = "typeAndDong";
        const log = console.log;

        let _userSeq = '${_user.userSeq}';
        let _userName = '${_user.userName}';
        let _name = '${_user.name}';
        let _role = '${_user.role}';

        $(function () {
            let paths = location.pathname.split("/");
            let size = paths.length;

            $('#navMenu').find('a').removeClass("active");
            $('#layoutSidenav_nav').find('a').removeClass("active");

            if (location.pathname === '/home') {
                $('#sidebarToggle').hide();
                $('#navhome').addClass("active");
            } else if (location.pathname === '/login') {
            } else if (location.pathname === '/register') {
            } else if (location.pathname === '/myInfo') {
            } else if (location.pathname === '/report/reportListByGovernment') {
                $('#navreportByGovernment').addClass("active");
            } else {
                $('#nav' + _contextPath.replace("/", "")).addClass("active");
            }

            $('#side_' + paths[size - 1]).addClass("active");

            // Toggle the side navigation
            const sidebarToggle = document.body.querySelector('#sidebarToggle');
            if (sidebarToggle) {
                sidebarToggle.addEventListener('click', event => {
                    event.preventDefault();
                    document.body.classList.toggle('sb-sidenav-toggled');
                    localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
                });
            }

            $('#back').on('click', function () {
                window.history.back();
            });

            $('#myInfo').on('click', function (){
               location.href = '${pageContext.request.contextPath}/myInfo';
            });

        });
    </script>
</head>