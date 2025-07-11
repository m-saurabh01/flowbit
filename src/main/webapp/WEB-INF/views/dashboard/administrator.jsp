<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/partials/flowbit-header.jspf" %>
<html>
<head>
<title>Administrator Dashboard</title>
<link rel="stylesheet" href="/css/bootstrap.css" />
<link rel="stylesheet" href="/css/bootstrap-icons.css">
<link rel="stylesheet" href="/css/offset.css" />
<style>
.card-custom {
	border-radius: 10px;
	overflow: hidden;
}

.tab-pane {
	padding: 1rem;
	background-color: #fff;
	border: 1px solid #dee2e6;
	border-top: none;
	border-radius: 0 0 0.5rem 0.5rem;
}

.nav-tabs .nav-link.active {
	background-color: #f8f9fa;
	border-color: #dee2e6 #dee2e6 #fff;
}

.table th, .table td {
	vertical-align: middle;
}

.btn-sm {
	min-width: 90px;
}

.alert-flash {
	margin-top: 1rem;
}


</style>

</head>
<body>
	<div class="container mt-5">
		<c:if test="${not empty message}">
			<div
				class="alert alert-success alert-flash alert-dismissible fade show d-flex justify-content-between align-items-center"
				role="alert">
				<div>
					<i class="bi bi-check-circle me-2"></i> ${message}
				</div>
				<button type="button" class="btn-close ms-3" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
		</c:if>


		<div class="d-flex justify-content-between align-items-center mb-3">
			<h2>Administrator Dashboard</h2>
			<form action="/logout" method="post" class="d-inline">
					<button class="btn btn-danger"><i class="bi bi-box-arrow-right me-1 px-1"></i>Logout</button>
				</form>
		</div>

		<ul class="nav nav-tabs" id="adminTabs" role="tablist">
			<li class="nav-item"><a class="nav-link active"
				data-bs-toggle="tab" href="#users">Users</a></li>
			<li class="nav-item"><a class="nav-link" data-bs-toggle="tab"
				href="#requests">Role Requests</a></li>
			<li class="nav-item"><a class="nav-link" data-bs-toggle="tab"
				href="#tasks">Task Metrics</a></li>
			<li class="nav-item"><a class="nav-link" data-bs-toggle="tab"
				href="#mapping">Mapping Requests</a></li>
		</ul>

		<div class="tab-content mt-4">
			<!-- === Users Panel === -->
			<div class="tab-pane fade show active" id="users">
				<h4>All Users</h4>
				<input type="text" class="form-control mb-2"
					placeholder="Search users..."
					onkeyup="searchTable(this, 'usersTable')" />
				<table class="table table-bordered" id="usersTable">
					<thead>
						<tr>
							<th>Name</th>
							<th>Email</th>
							<th>Role</th>
							<th>Status</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${users}">
							<tr>
								<td>${user.name}</td>
								<td>${user.email}</td>
								<td>${user.role}</td>
								<td>${user.active ? "Active" : "Inactive"}</td>
								<td>
									<!-- Existing Toggle Button -->
									<form method="post"
										action="<c:url value='/admin/users/toggle/${user.id}'/>"
										style="display: inline;">
										<button
											class="btn btn-sm ${user.active ? 'btn-danger' : 'btn-success'}">
											${user.active ? 'Deactivate' : 'Activate'}</button>
									</form> <!-- Change Password Button -->
									<button type="button"
										class="btn btn-sm btn-outline-warning ms-2"
										data-bs-toggle="modal"
										data-bs-target="#changePasswordModal${user.id}">
										<i class="bi bi-key-fill me-1"></i> Change Password
									</button> <!-- Password Change Modal -->
									<div class="modal fade" id="changePasswordModal${user.id}"
										tabindex="-1" aria-hidden="true">
										<div class="modal-dialog modal-dialog-centered modal-md">
											<div class="modal-content shadow-sm">
												<form method="post" action="/admin/change-password">
													<input type="hidden" name="userId" value="${user.id}" />
													<div class="modal-header bg-warning-subtle">
														<h5 class="modal-title text-dark">
															<i class="bi bi-key-fill me-2 text-warning"></i> Change
															Password for <strong>${user.name}</strong>
														</h5>
														<button type="button" class="btn-close"
															data-bs-dismiss="modal"></button>
													</div>

													<div class="modal-body px-4 pt-4 pb-2">
														<div class="mb-3">
															<label for="newPassword${user.id}" class="form-label">New
																Password</label> <input type="password" name="newPassword"
																id="newPassword${user.id}" class="form-control"
																placeholder="Enter a new secure password" required
																minlength="6" />
														</div>
													</div>

													<div class="modal-footer bg-light px-4 py-3">
														<button type="button" class="btn btn-secondary"
															data-bs-dismiss="modal">Cancel</button>
														<button type="submit" class="btn btn-warning">
															<i class="bi bi-check-circle me-1"></i> Update Password
														</button>
													</div>
												</form>
											</div>
										</div>
									</div>


								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<!-- === Role Requests Panel === -->
			<div class="tab-pane fade" id="requests">
				<h4>Pending Manager Role Requests</h4>
				<input type="text" class="form-control mb-2"
					placeholder="Search role requests..."
					onkeyup="searchTable(this, 'roleRequestsTable')" />
				<c:if test="${empty pendingManagers}">
					<div class="alert alert-info">No pending requests.</div>
				</c:if>
				<table class="table table-striped" id="roleRequestsTable">
					<thead>
						<tr>
							<th>Email</th>
							<th>Name</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${pendingManagers}">
							<tr>
								<td>${user.email}</td>
								<td>${user.name}</td>
								<td>
									<form action="/admin/approve-role" method="post"
										style="display: inline">
										<input type="hidden" name="userId" value="${user.id}" />
										<button class="btn btn-sm btn-success">Approve</button>
									</form>
									<form action="/admin/reject-role" method="post"
										style="display: inline">
										<input type="hidden" name="userId" value="${user.id}" />
										<button class="btn btn-sm btn-secondary">Reject</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<!-- === Mapping Requests Panel === -->
			<div class="tab-pane fade" id="mapping">
				<h4 class="mb-3">Pending Mapping Requests</h4>
				<input type="text" class="form-control mb-2"
					placeholder="Search mapping requests..."
					onkeyup="searchTable(this, 'mappingRequestsTable')" />
				<c:if test="${empty mappingRequests}">
					<div class="alert alert-info">No pending mapping requests.</div>
				</c:if>
				<c:if test="${not empty mappingRequests}">
					<table class="table table-bordered table-hover"
						id="mappingRequestsTable">
						<thead class="table-light">
							<tr>
								<th>Employee Name</th>
								<th>Employee Email</th>
								<th>Requested Manager</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="req" items="${mappingRequests}">
								<tr>
									<td>${req.employee.name}</td>
									<td>${req.employee.email}</td>
									<td>${req.requestedManager.name}</td>
									<td>
										<form action="/admin/mapping-requests/approve" method="post"
											style="display: inline;">
											<input type="hidden" name="requestId" value="${req.id}" />
											<button class="btn btn-sm btn-success me-2">Approve</button>
										</form>
										<form action="/admin/mapping-requests/reject" method="post"
											style="display: inline;">
											<input type="hidden" name="requestId" value="${req.id}" />
											<button class="btn btn-sm btn-danger">Reject</button>
										</form>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
			</div>

			<!-- === Task Metrics Panel === -->
			<div class="tab-pane fade" id="tasks">
				<h4>Task Overview</h4>
				<input type="text" class="form-control mb-2"
					placeholder="Search tasks..."
					onkeyup="searchTable(this, 'taskTable')" />
				<table class="table table-hover" id="taskTable">
					<thead>
						<tr>
							<th>Title</th>
							<th>Status</th>
							<th>Assignee</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="task" items="${tasks}">
							<tr>
								<td>${task.title}</td>
								<td>${task.status}</td>
								<td><c:forEach var="user" items="${task.assignedUsers}"
										varStatus="loop">
                                    ${user.name}<c:if
											test="${!loop.last}">, </c:if>
									</c:forEach></td>
								<td>
									<form action="/admin/delete-task" method="post"
										style="display: inline">
										<input type="hidden" name="taskId" value="${task.id}" />
										<button class="btn btn-sm btn-danger">Delete</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<!-- === Charts === -->
				<hr />
				<div class="row mt-4">
					<div class="col-md-6 mb-4">
						<div class="card shadow-sm">
							<div class="card-header text-center fw-bold">Task Status
								Distribution</div>
							<div
								class="card-body d-flex justify-content-center align-items-center"
								style="height: 300px;">
								<canvas id="statusChart"
									style="max-width: 100%; max-height: 100%;"></canvas>
							</div>
						</div>
					</div>

					<div class="col-md-6 mb-4">
						<div class="card shadow-sm">
							<div class="card-header text-center fw-bold">Estimated vs
								Actual Hours</div>
							<div class="card-body" style="height: 300px;">
								<canvas id="hoursChart"
									style="max-width: 100%; max-height: 100%;"></canvas>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- CHART + TAB SCRIPT -->
	<script src="/js/bootstrap.bundle.min.js"></script>
	<script src="/js/chart-my.js"></script>
	<script>
    // Tab hash persistence
    document.addEventListener("DOMContentLoaded", function () {
    const hash = window.location.hash;

    // Safety check for tab anchor
    if (hash) {
        const el = document.querySelector(`#adminTabs a[href="${hash}"]`);
        if (el) {
            const tabTrigger = new bootstrap.Tab(el);
            tabTrigger.show();
        }
    }

    // Always attach tab history updater
    document.querySelectorAll('#adminTabs a').forEach(function (el) {
        el.addEventListener('click', function () {
            history.replaceState(null, null, el.getAttribute('href'));
        });
    });
});


    // Search Table Function
    function searchTable(input, tableId) {
    if (!tableId) return;
    const table = document.getElementById(tableId);
    if (!table) return;
    
    const filter = input.value.toLowerCase();
    const rows = table.querySelectorAll("tbody tr");

    rows.forEach(row => {
        const text = row.innerText.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}
    </script>


	<script>
 // Pie Chart - Status
   new Chart(document.getElementById('statusChart'), {
        type: 'pie',
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
                    '#007bff',  // TODO
                    '#ffc107',  // IN_PROGRESS
                    '#17a2b8',  // IN_REVIEW
                    '#28a745',  // DONE
                    '#fd7e14',  // ON_HOLD
                    '#dc3545'   // NOT_STARTED
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom',
                    labels: {
                        padding: 10
                    }
                }
            }
        }
    });

    // Bar Chart - Hours
    new Chart(document.getElementById('hoursChart'), {
        type: 'bar',
        data: {
            labels: [
                <c:forEach var="label" items="${taskTitles}" varStatus="loop">
                "${label}"<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
            ],
            datasets: [{
                label: 'Estimated',
                data: [
                    <c:forEach var="val" items="${estimatedHours}" varStatus="loop">
                    ${val}<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
                ],
                backgroundColor: '#6c757d'
            }, {
                label: 'Actual',
                data: [
                    <c:forEach var="val" items="${actualHours}" varStatus="loop">
                    ${val}<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
                ],
                backgroundColor: '#dc3545'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom'
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            scales: {
                x: {
                    ticks: {
                        display: false  
                    },
                    grid: {
                        display: false  
                    }
                },
                y: {
                    beginAtZero: true
                }
            }
        }
    });
    
    setTimeout(() => {
        const alert = document.querySelector('.alert-flash');
        if (alert) {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }
    }, 5000);
</script>
</body>
</html>
