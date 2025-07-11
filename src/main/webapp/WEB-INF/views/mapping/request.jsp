<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/partials/flowbit-header.jspf" %>
<html>
<head>
<title>Request Manager Mapping</title>
<link rel="stylesheet" href="/css/bootstrap.css" />
<link rel="stylesheet" href="/css/bootstrap-icons.css" />
<link rel="stylesheet" href="/css/offset.css" />
</head>
<body class="bg-light">
	<div class="container mt-5">
		<div class="card shadow-sm p-4">
			<!-- Heading & Back Button -->
			<div class="d-flex justify-content-between align-items-center mb-4">
				<h4 class="mb-0">
					<i class="bi bi-person-plus-fill text-primary me-2 px-2"></i> Request
					Manager Mapping
				</h4>
				<a href="/dashboard" class="btn btn-outline-secondary"> <i
					class="bi bi-arrow-left-circle me-2"></i> Back to Dashboard
				</a>
			</div>

			<c:if test="${not empty currentManager}">
				<div class="alert alert-secondary d-flex align-items-center mb-4"
					role="alert">
					<i class="bi bi-person-check-fill text-success me-2 fs-5 px-3"></i>
					<div>
						Currently mapped to: <strong>${currentManager.name}</strong> <small
							class="text-muted">&nbsp;(${currentManager.email})</small>
					</div>
				</div>
			</c:if>

			<c:if test="${empty currentManager}">
				<div class="alert alert-warning d-flex align-items-center mb-4"
					role="alert">
					<i
						class="bi bi-exclamation-circle-fill text-warning me-2 fs-5 px-3"></i>
					<div>You are not mapped to any manager. Please select a
						manager and submit a request.</div>
				</div>
			</c:if>

			<!-- Mapping Form -->
			<form method="post" action="/mapping/request">
				<div class="mb-3">

					<select name="managerId" id="managerId" class="form-select"
						required style="height: 45px; width: 30%; text-align: center;">
						<option value="" disabled selected>Select a manager...</option>
						<c:forEach var="m" items="${managers}">
							<option value="${m.id}">${m.name}(${m.email})</option>
						</c:forEach>
					</select>
				</div>

				<button type="submit" class="btn btn-primary">
					<i class="bi bi-send-check me-2 px-2"></i> Send Request
				</button>
			</form>

			<!-- Message Alert -->
			<c:if test="${not empty message}">
				<div class="alert alert-info mt-4">
					<i class="bi bi-info-circle me-2 px-2"></i>${message}
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>
