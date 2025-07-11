<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/partials/flowbit-header.jspf" %>
<html>
<head>
<title>Edit Profile</title>
<link rel="stylesheet" href="/css/bootstrap.css" />
<link rel="stylesheet" href="/css/offset.css" />
<style>
.hidden {
	display: none;
}


</style>
<script>
	function validateForm() {
		const pass = document.getElementById("password").value;
		const confirm = document.getElementById("confirmPassword").value;
		if (pass !== confirm) {
			alert("Passwords do not match");
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<div class="container mt-5">
		<div class="card p-4">
			<h3>Edit Profile</h3>
			<form method="post" action="/profile/edit"
				onsubmit="return validateForm()">
				<div class="form-group mb-3">
					<label>Name</label> <input type="text" name="name"
						class="form-control" value="${user.name}" required>
				</div>
				<div class="form-group mb-3">
					<label>Email</label> <input type="email" name="email"
						class="form-control" value="${user.email}" readonly>
				</div>
				<div class="form-group mb-3 ${user.role == 'MANAGER' ?'hidden':''}">
					<label>Role</label> <select name="role" class="form-control">
						<option value="EMPLOYEE"
							${user.role == 'EMPLOYEE' ? 'selected' : ''}>EMPLOYEE</option>
						<option value="MANAGER"
							${user.role == 'MANAGER' ? 'selected' : ''}>MANAGER</option>
					</select>

					<c:if test="${pending}">
						<small class="form-text text-muted">Manager role request
							pending approval.</small>
					</c:if>
				</div>



				<div class="form-group mb-3">
					<label>New Password</label> <input type="password" name="password"
						id="password" class="form-control">
				</div>
				<div class="form-group mb-3">
					<label>Confirm Password</label> <input type="password"
						id="confirmPassword" class="form-control">
				</div>
				<div class="d-flex justify-content-between align-items-center">
					<button type="submit" class="btn btn-primary">Save Changes</button>
					<a href="<c:url value='/dashboard'/>" class="btn btn-secondary">Back
						to Dashboard</a>
				</div>
			</form>
			<c:if test="${not empty message}">
				<div class="alert alert-info mt-3">${message}</div>
			</c:if>

		</div>
	</div>

	<script>
		function validateForm() {
			const pass = document.getElementById("password").value.trim();
			const confirm = document.getElementById("confirmPassword").value
					.trim();

			if (pass || confirm) {
				if (pass !== confirm) {
					alert("Passwords do not match");
					return false;
				}
			}

			return true;
		}
	</script>

</body>
</html>