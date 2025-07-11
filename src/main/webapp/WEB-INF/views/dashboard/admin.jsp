<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="date" uri="http://example.com/tags/dateutils"%>
<%@ include file="/WEB-INF/views/partials/flowbit-header.jspf" %>
<html>
<head>
<title>Manager Dashboard</title>
<link rel="stylesheet" href="<c:url value='/css/bootstrap.css'/>" />
<link rel="stylesheet" href="<c:url value='/css/bootstrap-icons.css'/>" />
<script src="<c:url value='/js/bootstrap.bundle.min.js'/>"></script>
<script src="<c:url value='/js/chart-my.js'/>"></script>
<link rel="stylesheet" href="/css/offset.css" />
</head>
<body class="bg-light">
	<div class="container mt-5">

		<div class="d-flex justify-content-between mb-4">
			<div>
				<h2>Welcome, ${user.name}</h2>
				<p class="text-muted">Role: ${user.role}</p>
			</div>
			<div>
				<a href="/tasks/create" class="btn btn-success">+ Create Task</a> <a
					href="/profile/edit" class="btn btn-outline-primary">Edit
					Profile</a> <a href="/mapping/request"
					class="btn btn-outline-info btn-md">Request Mapping</a>
				<form action="/logout" method="post" class="d-inline">
					<button class="btn btn-outline-danger"><i class="bi bi-box-arrow-right me-1 px-1"></i>Logout</button>
				</form>


			</div>
		</div>

		<!-- Navigation Tabs -->
		<ul class="nav nav-tabs" id="dashboardTabs" role="tablist">
			<li class="nav-item" role="presentation">
				<button class="nav-link active" id="home-tab" data-bs-toggle="tab"
					data-bs-target="#home" type="button" role="tab">Home</button>
			</li>
			<li class="nav-item" role="presentation">
				<button class="nav-link" id="mytasks-tab" data-bs-toggle="tab"
					data-bs-target="#mytasks" type="button" role="tab">My
					Tasks</button>
			</li>
			<li class="nav-item" role="presentation">
				<button class="nav-link" id="approval-tab" data-bs-toggle="tab"
					data-bs-target="#approval" type="button" role="tab">Task
					Approval</button>
			</li>
			<li class="nav-item" role="presentation">
				<button class="nav-link" id="employees-tab" data-bs-toggle="tab"
					data-bs-target="#employees" type="button" role="tab">Employees</button>
			</li>
		</ul>

		<div class="tab-content p-4 bg-white border border-top-0 shadow-sm"
			id="dashboardTabsContent">

			<!-- Home Tab -->
			<div class="tab-pane fade show active" id="home" role="tabpanel">
				<div class="row mb-5">
					<div class="col-md-6 mb-4">
						<div class="card shadow-sm h-100">
							<div class="card-body">
								<h5 class="card-title">Task Distribution</h5>
								<div style="position: relative; height: 300px;">
									<canvas id="taskChart"></canvas>
								</div>
							</div>

						</div>
					</div>
					<div class="col-md-6 mb-4">
						<div class="card shadow-sm h-100">
							<div class="card-body">
								<h5 class="card-title">Upcoming Deadlines</h5>
								<div style="position: relative; height: 300px;">
									<canvas id="deadlineChart"></canvas>
								</div>
							</div>

						</div>
					</div>
				</div>

				<c:if test="${not empty overdueTasks}">
					<h5 class="mb-3">
						<i class="bi bi-clock-history me-2 text-danger"></i> Overdue Tasks
					</h5>

					<div class="card shadow-sm border-danger mb-4">
						<div class="card-header bg-danger text-white">
							<strong> <i
								class="bi bi-exclamation-triangle-fill me-2 px-2"></i> Attention
								Needed
							</strong>
						</div>
						<div class="list-group list-group-flush">
							<c:forEach var="task" items="${overdueTasks}">
								<a href="/tasks/${task.id}"
									class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
									<div>
										<i class="bi bi-exclamation-circle-fill text-danger me-2 px-1"></i>
										<strong>${task.title}</strong>
									</div> <span class="px-3 py-1 rounded-pill text-white fw-semibold"
									style="background-color: #dc3545;">
										${date:formatDate(task.deadline, 'dd MMM yyyy')} </span>
								</a>
							</c:forEach>
						</div>
					</div>
				</c:if>


				<c:if test="${empty overdueTasks}">
					<div class="card shadow-sm mb-4">
						<div class="list-group-item text-muted text-center">
							<i class="bi bi-check-circle me-1 text-success"></i> No overdue
							tasks.
						</div>
					</div>
				</c:if>
			</div>



			<!-- My Tasks Tab -->
			<div class="tab-pane fade" id="mytasks" role="tabpanel">
				<h5 class="mb-3">
					<i class="bi bi-person-lines-fill me-2 px-2"></i> Tasks Assigned to
					You
				</h5>

				<c:if test="${not empty myTasks}">
					<input type="text" class="form-control mb-3" id="myTaskSearch"
						placeholder="Search tasks..."
						onkeyup="filterCards('myTaskSearch', 'myTaskList')" />
				</c:if>

				<div id="myTaskList" class="list-group shadow-sm">
					<c:forEach var="task" items="${myTasks}">
						<a href="/tasks/${task.id}"
							class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
							<div>
								<i class="bi bi-journal-text me-2 text-secondary px-2"></i> <strong>${task.title}</strong>
							</div> <span
							class="badge 
					<c:choose>
						<c:when test="${task.status.name() == 'PENDING'}">bg-secondary</c:when>
						<c:when test="${task.status.name() == 'IN_PROGRESS'}">bg-info</c:when>
						<c:when test="${task.status.name() == 'IN_REVIEW'}">bg-warning text-dark</c:when>
						<c:when test="${task.status.name() == 'DONE'}">bg-success</c:when>
						<c:otherwise>bg-light text-dark</c:otherwise>
					</c:choose>
					px-3 py-2 rounded-pill text-uppercase fw-semibold">
								${task.status} </span>
						</a>
					</c:forEach>

					<c:if test="${empty myTasks}">
						<div class="list-group-item text-muted text-center">
							<i class="bi bi-check-circle me-2 text-success"></i> No tasks
							assigned to you.
						</div>
					</c:if>
				</div>
			</div>


			<!-- Task Approval Tab -->
			<div class="tab-pane fade" id="approval" role="tabpanel">
				<h5 class="mb-3">
					<i class="bi bi-clipboard-check me-2 px-2 text-primary"></i> Tasks
					Awaiting Approval
				</h5>

				<c:if test="${not empty tasksForReview}">
					<input type="text" class="form-control mb-3" id="approvalSearch"
						placeholder="Search review tasks..."
						onkeyup="filterCards('approvalSearch', 'approvalList')" />
				</c:if>

				<div id="approvalList">
					<c:forEach var="task" items="${tasksForReview}">
						<div class="card shadow-sm mb-3">
							<div class="card-body">
								<div class="d-flex justify-content-between align-items-center">
									<div>
										<h6 class="mb-2">
											<i class="bi bi-journal-check me-2 text-secondary px-1"></i>
											<strong>${task.title}</strong>
										</h6>
										<p class="mb-0 text-muted">
											<i class="bi bi-hourglass-split me-1"></i> Status: <span
												class="badge
									<c:choose>
										<c:when test="${task.status.name() == 'PENDING'}">bg-secondary</c:when>
										<c:when test="${task.status.name() == 'IN_PROGRESS'}">bg-info</c:when>
										<c:when test="${task.status.name() == 'IN_REVIEW'}">bg-warning text-dark</c:when>
										<c:when test="${task.status.name() == 'DONE'}">bg-success</c:when>
										<c:otherwise>bg-light text-dark</c:otherwise>
									</c:choose>
									px-3 py-1 rounded-pill text-uppercase fw-semibold small">
												${task.status} </span>
										</p>
									</div>

									<div class="text-end">
										<!-- View Task -->
										<a href="/tasks/${task.id}"
											class="btn btn-outline-primary btn-sm me-2"> <i
											class="bi bi-eye me-1"></i> View
										</a>

										<!-- Approve -->
										<form method="post" action="/tasks/approve" class="d-inline">
											<input type="hidden" name="taskId" value="${task.id}" />
											<button class="btn btn-success btn-sm">
												<i class="bi bi-check-circle me-1"></i> Approve
											</button>
										</form>

										<!-- Reject -->
										<form method="post" action="/tasks/reject"
											class="d-inline ms-2">
											<input type="hidden" name="taskId" value="${task.id}" />
											<button class="btn btn-danger btn-sm">
												<i class="bi bi-x-circle me-1"></i> Reject
											</button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>

				<c:if test="${empty tasksForReview}">
					<div class="text-muted">
						<i class="bi bi-check-circle me-2 text-success"></i> No tasks
						pending review.
					</div>
				</c:if>
			</div>



			<!-- Employees Tab -->

			<div class="tab-pane fade" id="employees" role="tabpanel">
				<h5 class="mb-4">
					<i class="bi bi-people-fill me-2 px-2"></i>Your Employees
				</h5>
				<c:if test="${not empty team}">
					<input type="text" class="form-control mb-3" id="employeeSearch"
						placeholder="Search employees..."
						onkeyup="filterCards('employeeSearch', 'employeeList')" />
				</c:if>

				<div class="row" id="employeeList">
					<c:forEach var="emp" items="${team}">
						<div class="col-md-6 mb-3 employee-entry">
							<a href="/manager/employee/${emp.id}"
								class="text-decoration-none">
								<div class="card shadow-sm h-100 hover-shadow">
									<div class="card-body">
										<div class="d-flex align-items-center mb-2">
											<i class="bi bi-person-circle fs-4 text-primary px-2"></i>
											<h6 class="mb-0 text-dark">${emp.name}</h6>
										</div>
										<p class="mb-0 text-muted px-2">
											<i class="bi bi-envelope-at me-1"></i> ${emp.email}<br /> <i
												class="bi bi-award me-1"></i> ${emp.role}
										</p>
									</div>
								</div>
							</a>
						</div>
					</c:forEach>

					<c:if test="${empty team}">
						<div class="text-muted px-2">No employees mapped to you.</div>
					</c:if>
				</div>
			</div>


		</div>

	</div>

	<script>
    const labels = [<c:forEach var="l" items="${chartLabels}">'${l}',</c:forEach>];
    const values = [<c:forEach var="v" items="${chartValues}">${v},</c:forEach>];

    new Chart(document.getElementById('taskChart'), {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: values,
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

    const deadlineLabels = [<c:forEach var="day" items="${deadlineLabels}">'${day}',</c:forEach>];
    const deadlineCounts = [<c:forEach var="count" items="${deadlineCounts}">${count},</c:forEach>];

    new Chart(document.getElementById('deadlineChart'), {
        type: 'bar',
        data: {
            labels: deadlineLabels,
            datasets: [{
                label: 'Tasks Due',
                data: deadlineCounts,
                backgroundColor: '#007bff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true, position: 'bottom' }
            },
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
    
    function filterCards(inputId, containerId) {
	    const query = document.getElementById(inputId).value.toLowerCase();
	    const container = document.getElementById(containerId);
	    const items = container.querySelectorAll('.list-group-item,.employee-entry');
	    
	    console.log(query,items)
	     
	   items.forEach(item => {
	        const text = item.textContent.toLowerCase();
	        if(!text.includes(query)){
	        	
	        	item.classList.add('d-none');
	        }else{
	        	
	        	item.classList.remove('d-none');
	        }
	    });
	}

    function logout() {
        document.cookie = "token=; path=/; max-age=0";
        window.location.href = "/login";
    }
</script>
</body>
</html>
