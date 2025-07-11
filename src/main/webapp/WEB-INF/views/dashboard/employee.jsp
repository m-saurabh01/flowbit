<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="date" uri="http://example.com/tags/dateutils"%>
<%@ include file="/WEB-INF/views/partials/flowbit-header.jspf" %>
<html>
<head>
<title>Employee Dashboard</title>
<link rel="stylesheet" href="<c:url value='/css/bootstrap.css'/>" />
<link rel="stylesheet" href="<c:url value='/css/bootstrap-icons.css'/>" />
<script src="<c:url value='/js/chart-my.js'/>"></script>
<script src="<c:url value='/js/jquery-3.6.0.min.js'/>"></script>
<link rel="stylesheet" href="/css/offset.css" />
<style>
#taskTableContainer {
	max-height: 400px;
	overflow-y: auto;
}

.table td, .table th {
	vertical-align: middle;
}

.chart-card {
	padding: 1.5rem;
	min-height: 360px;
}

.chart-card .card-title {
	font-size: 1.25rem;
	margin-bottom: 1.2rem;
}

.chart-container {
	height: 320px;
}
</style>
</head>
<body class="bg-light">
	<div class="container mt-5">

		<!-- Header -->
		<div class="d-flex justify-content-between align-items-center mb-4">
			<div>
				<h2>Hello, ${user.name}</h2>
				<p class="text-muted">Welcome to your task dashboard</p>
			</div>
			<div>
				<a href="/profile/edit" class="btn btn-outline-primary me-2"> <i
					class="bi bi-pencil-square me-1"></i> Edit Profile
				</a> <a href="/mapping/request" class="btn btn-outline-info me-2"> <i
					class="bi bi-link-45deg me-1"></i> Request Mapping
				</a>
				<form action="/logout" method="post" class="d-inline">
					<button class="btn btn-outline-danger"><i class="bi bi-box-arrow-right me-1 px-1"></i>Logout</button>
				</form>
				
			</div>
		</div>

		<!-- Task Summary Charts -->
		<div class="row mb-5">
			<div class="col-md-6 mb-4">
				<div class="card shadow-sm h-100">
					<div class="card-body">
						<h5 class="card-title mb-3">
							<i class="bi bi-graph-up me-2 text-success px-2"></i>Task Status
							Summary
						</h5>
						<div style="position: relative; height: 300px;">
							<canvas id="taskChart"></canvas>
						</div>
					</div>
				</div>
			</div>
			<!-- Optional: Add another chart here in col-md-6 -->
			<div class="col-md-6 mb-4">
				<div class="card shadow-sm h-100">
					<div class="card-body">
						<h5 class="card-title">
							<i class="bi bi-bar-chart-fill me-2 text-primary px-2"></i>Task
							Priority Distribution
						</h5>
						<div style="position: relative; height: 300px;">
							<canvas id="priorityChart"></canvas>
						</div>
					</div>
				</div>
			</div>

		</div>

		<!-- Your Tasks Table -->
		<h5 class="mb-3">
			<i class="bi bi-list-check me-2 text-dark px-2"></i>Your Tasks
		</h5>

		<c:if test="${not empty myTasks}">
			<input type="text" id="taskSearch" class="form-control mb-3"
				placeholder="Search tasks...">
		</c:if>

		<div id="taskTableContainer"
			class="table-responsive shadow-sm bg-white rounded">
			<table class="table table-hover align-middle mb-0">
				<thead class="table-light sticky-top">
					<tr>
						<th>Title</th>
						<th>Status</th>
						<th>Priority</th>
						<th>Deadline</th>
					</tr>
				</thead>
				<tbody id="taskTableBody">
					<c:forEach var="task" items="${myTasks}">
						<tr>
							<td><a href="/tasks/${task.id}" class="text-decoration-none">
									<i class="bi bi-journal-text me-1 text-secondary"></i> <strong>${task.title}</strong>
							</a></td>
							<td><span
								class="badge text-uppercase fw-semibold 
                                <c:choose>
                                    <c:when test="${task.status == 'ON_HOLD'}">bg-secondary</c:when>
                                    <c:when test="${task.status == 'IN_PROGRESS'}">bg-info</c:when>
                                    <c:when test="${task.status == 'IN_REVIEW'}">bg-warning text-dark</c:when>
                                    <c:when test="${task.status == 'DONE'}">bg-success</c:when>
                                    <c:otherwise>bg-light text-dark</c:otherwise>
                                </c:choose>
                            ">${task.status}</span>
							</td>
							<td><span
								class="badge bg-light text-dark px-2 py-1 border border-dark rounded-pill text-uppercase fw-semibold">
									${task.priority} </span></td>
							<td><i class="bi bi-calendar-event me-1"></i>
								${date:formatDate(task.deadline, 'dd MMM yyyy')}</td>
						</tr>
					</c:forEach>
					<c:if test="${empty myTasks}">
						<tr>
							<td colspan="4" class="text-muted text-center"><i
								class="bi bi-check-circle me-1 text-success"></i> No tasks
								assigned.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>



	<!-- JS for Chart and Search -->
	<script>
    // Chart
    new Chart(document.getElementById('taskChart'), {
        type: 'doughnut',
        data: {
            labels: [
                <c:forEach var="label" items="${chartLabels}" varStatus="loop">
                    "${label}"<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
            ],
            datasets: [{
                data: [
                    <c:forEach var="val" items="${chartValues}" varStatus="loop">
                        ${val}<c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                ],
                backgroundColor: [
                    '#6c757d', '#17a2b8', '#ffc107', '#28a745', '#fd7e14', '#dc3545'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom' }
            }
        }
    });
    
    new Chart(document.getElementById('priorityChart'), {
        type: 'bar',
        data: {
            labels: [
                <c:forEach var="label" items="${priorityChartLabels}" varStatus="loop">
                    "${label}"<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
            ],
            datasets: [{
                label: 'Number of Tasks',
                data: [
                    <c:forEach var="val" items="${priorityChartValues}" varStatus="loop">
                        ${val}<c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                ],
                backgroundColor: '#007bff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    beginAtZero: true,
                   
                },
                x: {
                    
                }
            }
        }
    });


    // Search filter
    $('#taskSearch').on('keyup', function () {
        const query = $(this).val().toLowerCase();
        $('#taskTableBody tr').each(function () {
            const rowText = $(this).text().toLowerCase();
            $(this).toggle(rowText.includes(query));
        });
    });

    // Logout
    function logout() {
        document.cookie = "token=; path=/; max-age=0";
        window.location.href = "/login";
    }
</script>
</body>
</html>
