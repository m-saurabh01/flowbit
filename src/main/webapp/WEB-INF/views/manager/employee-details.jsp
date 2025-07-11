<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="date" uri="http://example.com/tags/dateutils"%>
<%@ include file="/WEB-INF/views/partials/flowbit-header.jspf" %>

<html>
<head>
<title>Employee Overview</title>
<link rel="stylesheet" href="/css/bootstrap.css" />
<link rel="stylesheet" href="/css/bootstrap-icons.css" />
<script src="/js/bootstrap.bundle.min.js"></script>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/tinymce/tinymce.min.js"></script>
<link rel="stylesheet" href="/css/offset.css" />
</head>
<body class="bg-light">
	<div class="container mt-5">

		<!-- Back Button -->
		<div class="mb-4">
			<a href="/dashboard" class="btn btn-outline-secondary px-3 mb-3">
				<i class="bi bi-arrow-left-circle me-2 px-2"></i> Back to Dashboard
			</a>
		</div>

		<!-- Employee Card -->
		<div class="card shadow-sm mb-4">
			<div class="card-body">
				<h4 class="card-title mb-3">
					<i class="bi bi-person-circle text-primary me-2 px-2"></i>
					${employee.name}
				</h4>
				<p class="text-muted mb-1 px-2">
					<i class="bi bi-envelope-at me-2 px-2"></i>${employee.email}
				</p>
				<p class="text-muted px-2">
					<i class="bi bi-award me-2 px-2"></i>${employee.role}
				</p>
			</div>
		</div>

		<!-- Assigned Tasks -->
		<h5 class="mb-3 px-2">
			<i class="bi bi-list-task me-2 px-2"></i>Assigned Tasks
		</h5>

		<c:if test="${not empty tasks}">
			<!-- 🔍 Search -->
			<input type="text" class="form-control mb-3" id="taskSearch"
				placeholder="Search tasks..."
				onkeyup="filterCards('taskSearch', 'tasks')" />
		</c:if>

		<div class="list-group shadow-sm" id="tasks">
			<c:forEach var="task" items="${tasks}">
				<a href="/tasks/${task.id}"
					class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
					<div>
						<i class="bi bi-journal-text me-2 text-secondary px-2"></i> <strong>${task.title}</strong>
					</div> <span
					class="badge
                    <c:choose>
                        <c:when test="${task.status.name() == 'ON_HOLD'}">bg-secondary</c:when>
                        <c:when test="${task.status.name() == 'IN_PROGRESS'}">bg-info</c:when>
                        <c:when test="${task.status.name() == 'IN_REVIEW'}">bg-warning text-dark</c:when>
                        <c:when test="${task.status.name() == 'DONE'}">bg-success</c:when>
                        <c:otherwise>bg-light text-dark</c:otherwise>
                    </c:choose>
                    px-3 py-2 rounded-pill text-uppercase fw-semibold">
						${task.status} </span>
				</a>
			</c:forEach>

			<c:if test="${empty tasks}">
				<div class="list-group-item text-muted text-center">
					<i class="bi bi-check-circle me-2 text-success"></i> No tasks
					assigned to this employee.
				</div>
			</c:if>
		</div>
	</div>

	<!-- JS: Filtering -->
	<script>
	function filterCards(inputId, containerId) {
	    const query = document.getElementById(inputId).value.toLowerCase();
	    const container = document.getElementById(containerId);
	    const items = container.querySelectorAll('.list-group-item');
	     
	   items.forEach(item => {
	        const text = item.textContent.toLowerCase();
	        if(!text.includes(query)){
	        	item.classList.remove('d-flex');
	        	item.classList.add('d-none');
	        }else{
	        	item.classList.add('d-flex');
	        	item.classList.remove('d-none');
	        }
	    });
	}

    function filterTable(inputId, tableBodyId) {
        const query = document.getElementById(inputId).value.toLowerCase();
        const rows = document.getElementById(tableBodyId).getElementsByTagName("tr");
        for (let row of rows) {
            row.style.display = row.textContent.toLowerCase().includes(query) ? '' : 'none';
        }
    }
</script>
</body>
</html>
