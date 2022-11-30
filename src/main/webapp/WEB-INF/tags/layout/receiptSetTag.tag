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

<!-- content -->
<main id="reportSet">
    <div class="container-fluid px-4">
        <h1 class="mt-4">신고목록</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item active">${subTitle} > 신고목록</li>
        </ol>
        <div class="card mb-2 shadow-sm rounded">
            <div class="card-header">
                <div class="row">
                    <div class="col-9">
                        <i class="fas fa-table"></i>
                        차량 번호 :
                        <span id="carNum" class="title"></span>
                    </div>
                    <div class="col-3 d-flex justify-content-end">
                        <a class="ms-5 btn btn-close" id="close"></a>
                    </div>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <table class="table  table-borderless">
                        <colgroup>
                            <col width="10%">
                            <col >
                        </colgroup>
                        <tbody>
                            <tr>
                                <td class="h3" colspan="2"><span class="ms-3 fw-bold" id="receiptStateTypeView"></span></td>
                            </tr>
                            <tr>
                                <td><label class="ms-lg-5">신고회수 : </label></td>
                                <td><span class="ms-lg-2" id="overlapCount"></span></td>
                            </tr>
                            <tr>
                                <td><label class="ms-lg-5">위치 : </label></td>
                                <td><span class="ms-lg-2" id="addr"></span></td>
                            </tr>
                            <tr>
                                <td><label class="ms-lg-5">신고자 : </label></td>
                                <td><span class="ms-lg-2" id="name"></span></td>
                            </tr>
                        </tbody>
                    </table>

                    <form id="data">
                        <div class="row mb-2 ms-2">
                            <div class="col-6">
                                <div class="card">
                                    <div class="card" style="width: 100%;">
                                        <img id="firstFileName" src="" class="card-img-top" alt="..." style="width: 100%; height: 400px;">
                                        <div class="card-body">
                                            <h5 class="card-title mb-4 fst-italic fw-bold text-danger">1차 신고</h5>
                                            <p class="card-text">신고 위치 : <span id="firstAddr" class="title"></span></p>
                                            <p class="card-text">신고 시간 : <span id="firstRegDt" class="title"></span></p>
                                            <p class="card-text">위치 분석 : <span id="firstIllegalType" class="title"></span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-6 m-0">
                                <div class="card ">
                                    <div class="card" style="width: 100%;">
                                        <img id="secondFileName" src="" class="card-img-top" alt="..." style="width: 100%; height: 400px;">
                                        <div class="card-body">
                                            <h5 class="card-title mb-4  fst-italic fw-bold text-danger">2차 신고</h5>
                                            <p class="card-text">신고 위치 : <span id="secondAddr" class="title"></span></p>
                                            <p class="card-text">신고 시간 : <span id="secondRegDt" class="title"></span></p>
                                            <p class="card-text">위치 분석 : <span id="secondIllegalType" class="title"></span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="card-footer" id="comments"></div>
        </div>
    </div>
</main>