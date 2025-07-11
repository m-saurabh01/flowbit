<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="date" uri="http://example.com/tags/dateutils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Task Details</title>
<link rel="stylesheet" href="/css/bootstrap.css" />
<link rel="stylesheet" href="/css/bootstrap-icons.css" />
<script src="/js/bootstrap.bundle.min.js"></script>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/tinymce/tinymce.min.js"></script>
<style>
.icon-label {
	display: flex;
	align-items: center;
	gap: 0.5rem;
	margin-bottom: 0.75rem;
}

.form-select-status {
	min-width: 180px;
}
</style>
</head>
<body>
	<div class="container mt-5">
		<div class="card shadow-sm p-4">
			<!-- Header -->
			<div class="d-flex justify-content-between align-items-center mb-4">
				<h3 class="mb-0">${task.title}</h3>
				<a href="/dashboard" class="btn btn-outline-secondary"> <i
					class="bi bi-arrow-left-circle me-1"></i> Back to Dashboard
				</a>
			</div>

			<p class="mb-3 text-muted">
				<strong>Status:</strong> ${task.status} &nbsp; | &nbsp; <strong>Priority:</strong>
				${task.priority}
			</p>
			<!-- Task Metadata -->
			<div class="row mb-4 text-muted small">
				<div class="col-md-4 mb-2">
					<i class="bi bi-calendar-plus me-1 text-primary"></i> <strong>Created:</strong>
					${date:formatDateTime(task.createdDate, 'dd MMM yyyy HH:mm')}
				</div>
				<div class="col-md-4 mb-2">
					<i class="bi bi-calendar-check me-1 text-success"></i> <strong>Last
						Updated:</strong> ${date:formatDateTime(task.updatedDate, 'dd MMM yyyy HH:mm')}
				</div>
				<div class="col-md-4 mb-2">
					<i class="bi bi-person-circle me-1 text-secondary"></i> <strong>Created
						By:</strong> ${createdBy}
				</div>
			</div>


			<!-- Description -->
			<div class="mb-4">
				<label class="fw-bold">Description:</label>
				<div class="border p-3 rounded bg-light">${task.description}</div>
			</div>

			<!-- Metadata Section -->
			<div class="mb-4">
				<div class="icon-label">
					<i class="bi bi-tags fs-5"></i> <strong>Tags:</strong>&nbsp;
					${task.tags}
				</div>
				<div class="icon-label">
					<i class="bi bi-clock fs-5"></i> <strong>Deadline:</strong>&nbsp;
					${date:formatDate(task.deadline, 'dd-MM-yyyy')}
				</div>
				<div class="icon-label">
					<i class="bi bi-people fs-5"></i> <strong>Assigned To:</strong>
				</div>
				<ul class="mb-3 ms-4">
					<c:forEach var="user" items="${task.assignedUsers}">
						<li>${user.name}</li>
					</c:forEach>
				</ul>

				<div class="icon-label">
					<i class="bi bi-paperclip fs-5"></i> <strong>Attachments:</strong>
				</div>
				<ul class="mb-3 ms-4">
					<c:forEach var="file" items="${task.attachments}">
						<li><a href="/attachments/download/${file.id}"
							target="_blank">${file.fileName}</a></li>
					</c:forEach>
				</ul>
			</div>

			<!-- Employee Actions -->


			<c:if test="${isAssignedUserId}">
				<c:choose>
					<c:when test="${task.status == 'IN_REVIEW'}">
						<div class="alert alert-warning d-flex align-items-center mb-3">
							<i class="bi bi-lock me-2"></i>
							<div>Task has been submitted for review and is pending
								manager action.</div>
						</div>
					</c:when>
					<c:when test="${task.status == 'DONE'}">
						<div class="alert alert-success d-flex align-items-center mb-3">
							<i class="bi bi-check2-circle me-2 px-2"></i>
							<div>
								This task has been <strong>marked as completed</strong> by your
								manager.
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<!-- Status Update Form -->
						<div class="border rounded p-3 mb-4">
							<h6 class="mb-3">Update Task Status</h6>
							<form method="post" action="/tasks/update-status"
								class="row g-0 align-items-center">
								<input type="hidden" name="taskId" value="${task.id}" />
								<div class="col-auto">
									<select name="status" class="form-select"
										style="min-width: 250px; height: 38px; padding-left: 15px;"
										required>
										<c:forEach var="status" items="${statusOptions}">
											<option value="${status}"
												${task.status == status ? 'selected' : ''}>${status}</option>
										</c:forEach>
									</select>
								</div>
								<div class="col-auto ms-2">
									<button type="submit" class="btn btn-primary"
										style="height: 38px;">
										<i class="bi bi-arrow-repeat me-1"></i> Update
									</button>
								</div>
							</form>
						</div>

						<!-- Submit for Review -->
						<!-- Submit for Review -->
						<c:if test="${task.status == 'IN_PROGRESS'}">
							<form method="post" action="/tasks/submit-review" class="mb-3">
								<input type="hidden" name="taskId" value="${task.id}" />

								<!-- NEW: Actual Hours Field -->
								<div class="mb-2" style="max-width: 250px;">
									<label for="actualHours" class="form-label small fw-semibold">Actual
										Hours Spent</label> <input type="number" min="1" name="actualHours"
										id="actualHours" class="form-control" placeholder="e.g. 5"
										required />
								</div>

								<button type="submit" class="btn btn-success">
									<i class="bi bi-send-check me-1"></i> Submit for Review
								</button>
							</form>
						</c:if>

					</c:otherwise>
				</c:choose>
			</c:if>


			<!-- Comments -->
			<hr />
			<h5 class="mb-3">Comments</h5>
			<input type="text" class="form-control mb-3" id="commentSearch"
				placeholder="Search comments..."
				onkeyup="filterCards('commentSearch', 'commentList')" />

			<div class="comment-list"
				style="max-height: 400px; overflow-y: auto;">
				<c:forEach var="comment" items="${comments}">
					<div class="card mb-3">
						<div
							class="card-header d-flex justify-content-between align-items-center">
							<strong>${comment.author.name}</strong> <small class="text-muted">
								${date:formatDateTime(comment.timestamp, 'dd MMM yyyy HH:mm')} </small>
						</div>
						<div class="card-body">
							<div class="comment-body">${comment.message}</div>
						</div>
					</div>
				</c:forEach>
			</div>


			<c:if test="${task.status != 'DONE'}">
				<form method="post" action="/tasks/add-comment" class="mb-5"
					onsubmit="return validateEditor()">
					<input type="hidden" name="taskId" value="${task.id}" />
					<textarea id="richCommentBox" name="content"></textarea>
					<button type="submit" class="btn btn-outline-primary mt-2">Post
						Comment</button>
				</form>

			</c:if>

			<!-- Daily Logs -->
			<hr />
			<h5 class="mb-3">Daily Logs</h5>
			<input type="text" class="form-control mb-3" id="logSearch"
				placeholder="Search logs..."
				onkeyup="filterTable('logSearch', 'logTable')" />

			<div style="max-height: 300px; overflow-y: auto;">
				<table class="table table-bordered mb-4">
					<thead class="table-light">
						<tr>
							<th>Date</th>
							<th>User</th>
							<th>Hours</th>
							<th>Note</th>
						</tr>
					</thead>
					<tbody id="logTable">
						<c:forEach var="log" items="${logs}">
							<tr>
								<td>${date:formatDateTime(log.date, 'dd-MM-yyyy')}</td>
								<td>${log.user.name}</td>
								<td>${log.hourSpent}</td>
								<td>${log.progressNote}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<c:if test="${task.status != 'DONE'}">
				<form method="post" action="/tasks/add-log" class="row g-2">
					<input type="hidden" name="taskId" value="${task.id}" />
					<div class="col-md-3">
						<input type="number" name="hoursSpent" class="form-control"
							min="1" max="24" placeholder="Hours spent" required />
					</div>
					<div class="col-md-6">
						<input type="text" name="note" class="form-control"
							placeholder="Log note..." required />
					</div>
					<div class="col-md-3">
						<button type="submit" class="btn btn-outline-secondary w-100">Add
							Log</button>
					</div>
				</form>
			</c:if>
		</div>
	</div>

	<!-- JS: Search Filters -->
	<script>
function filterCards(inputId, containerId) {
    const query = document.getElementById(inputId).value.toLowerCase();
    const items = document.getElementById(containerId).children;
    for (let item of items) {
        item.style.display = item.textContent.toLowerCase().includes(query) ? '' : 'none';
    }
}

function filterTable(inputId, tableBodyId) {
    const query = document.getElementById(inputId).value.toLowerCase();
    const rows = document.getElementById(tableBodyId).getElementsByTagName("tr");
    for (let row of rows) {
        row.style.display = row.textContent.toLowerCase().includes(query) ? '' : 'none';
    }
}
tinymce.init({
    selector: '#richCommentBox',
    menubar: false,
    height: 200,
    plugins: 'lists link emoticons code',
    toolbar: 'bold italic underline | bullist numlist | link emoticons | undo redo | code',
    branding: false
});

function validateEditor() {
    const content = tinymce.get('richCommentBox').getContent({ format: 'text' }).trim();
    if (!content) {
        alert("Comment cannot be empty.");
        tinymce.get('richCommentBox').focus();
        return false;
    }
    return true;
}
</script>
</body>
</html>
