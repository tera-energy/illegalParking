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
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="/WEB-INF/views/layout/navHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<!-- side -->
	<stripes:layout-component name="side"></stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div>
				<div class="m-0 p-0 main_image">
					<div class="col main_text">
						<p class="col fs-1 text-white">안녕하세요,</p>
						<p class="col fs-2 text-white">'${officeName}' 입니다.</p>
					</div>
				</div>

				<div>
					<div class="container-fluid info_area">
						<div class="row">
							<div class="col-6">
								<div class="row">
									<h4 class="chart_title">신고건수</h4>
								</div>
								<div class="row">
									<div id="pieChart" style="align-items: start;"></div>
									<div class="container data_info">
										<div class="row">
											<h5>총 신고건수</h5>
											<p>${totalCount}</p>
										</div>
										<div class="row">
											<div class="col">
												<h5>대기</h5>
												<div class="row">
													<span>${completeCount}</span>
												</div>
											</div>

											<div class="col">
												<h5>미처리</h5>
												<div class="row">
													<span>${exceptionCount}</span>
												</div>
											</div>

											<div class="col">
												<h5>처리</h5>
												<div class="row">
													<span>${penaltyCount}</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6">
								<div class="col">
									<h4 class="chart_title">신고발생 / 신고접수</h4>
									<p class="explain">※ 데이터 Ai 자동 분석을 통해 중복 신고, 변동 단속 시간, 오류 신고 등 제외를 통하여 ${totalCount}의 신고 중 ${sendPenaltyCount}건의 신고를 담당 부서에 전달드렸습니다.</p>
								</div>
								<div class="col">
									<div id="barChart" style="height: 420px; width: 100%;"></div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</main>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<footer class="py-4 bg-dark">
			<div class="container-fluid px-4">
				<div class="d-flex align-items-center justify-content-between small">
					<div class="text-muted">
						COPYRIGHT &copy; TERAENERGY INC. ALL RIGHTS RESERVED. ( <a href="http://teraenergy.co.kr/">teraenergy.co.kr</a> )
					</div>
					<div>
						<img src="http://teraenergy.co.kr/teraenergy/images/logo_bottom.png" alt="테라이너지 로고" style="width: 100px; height: 25px;">
					</div>
				</div>
			</div>
		</footer>
		<%--		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>--%>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
		<script src="<%=contextPath%>/resources/js/scripts.js"></script>
		<script type="application/javascript">
            $(function () {

                let completeCount = '${completeCount}';
                let exceptionCount = '${exceptionCount}';
                let penaltyCount = '${penaltyCount}';
                let reportCounts = ${reportCounts};
                let receiptCounts = ${receiptCounts};

                // 신고 접수 건수 파이 차트
                $.drawPieChart = function (opt) {
                    let options = {
                        animationEnabled: true,
                        // title: {
                        //     text: "신고 건수"
                        // },
                        legend: {
                            horizontalAlign: "right",
                            verticalAlign: "bottom"
                        },
                        data: [{
                            type: "doughnut",
                            innerRadius: "70%",
                            showInLegend: true,
                            legendText: "{label}",
                            indexLabel: "{label}",
                            dataPoints: [
                                {label: "대기", y: Number(opt.completeCount)},
                                {label: "미처리", y: Number(opt.exceptionCount)},
                                {label: "처리", y: Number(opt.penaltyCount)}
                            ]
                        }]
                    };

                    function draw() {
                        $("#pieChart").css("height", "50vh").css("width", "30vw");
                        $("#pieChart").CanvasJSChart(options);
                        $('.canvasjs-chart-credit').hide();
                    };

                    setTimeout(() => {
                        draw();
                    }, 200);
                }

                // 월별 신고 건수
                $.drawBarChart = function (opt) {

                    let reportsDatas = [];
                    let receiptDatas = [];

                    for (let i = 0; i < opt.receiptCounts.length; i++) {
                        reportsDatas.push({label: (i + 1) + "월", y: opt.reportCounts[i]});
                        receiptDatas.push({label: (i + 1) + "월", y: opt.receiptCounts[i]});
                    }

                    var chart = new CanvasJS.Chart("barChart", {
                        animationEnabled: true,
                        // title: {
                        //     text: "신고 발생 / 신고 접수 "
                        // },
                        axisY: {
                            lineThickness: 0,
                            gridColor: "#D8D8D8"
                        },
                        data: [
                            {
                                // 신고 발생 건수
                                type: "column",
                                dataPoints: receiptDatas
                            },
                            {
                                // 신고 접수 건수
                                type: "column",
                                dataPoints: reportsDatas
                            }
                        ]
                    });

                    chart.render();
                }

                // 파이 차트
				$.drawPieChart({
                    completeCount: completeCount,
                    exceptionCount: exceptionCount,
                    penaltyCount: penaltyCount
                });

                // bar 차트
                $.drawBarChart({
                    receiptCounts: receiptCounts,
                    reportCounts: reportCounts
                });

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>