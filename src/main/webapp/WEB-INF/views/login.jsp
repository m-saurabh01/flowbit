<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Login - Task Manager</title>
<link rel="stylesheet" href="<c:url value='/css/bootstrap.css'/>">
<link rel="stylesheet" href="<c:url value='/css/bootstrap-icons.css'/>">
<style>
.auth-card {
	max-width: 450px;
	margin: 60px auto;
	padding: 2rem;
	border-radius: 10px;
	box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
}

</style>
</head>
<body class="bg-light">
    <div class="d-flex justify-content-center align-items-center vh-100">
        <div class="card p-4 shadow .auth-card " style="min-width: 350px; max-width: 450px; width: 100%;">
          
			<h4 class="mb-3 text-center">
				<i class="bi bi-box-arrow-in-right me-2"></i> Login
			</h4>
			<form id="loginForm">
				<div class="mb-3">
					<label for="email" class="form-label">Email</label> <input
						type="email" id="email" class="form-control"
						placeholder="Enter your email" required />
				</div>
				<div class="mb-3">
					<label for="password" class="form-label">Password</label> <input
						type="password" id="password" class="form-control"
						placeholder="Enter password" required />
				</div>
				<div class="text-center mt-5">
					<button type="submit" class="btn btn-primary px-4">
						<i class="bi bi-unlock me-1"></i> Login
					</button>
				</div>

			</form>
			<p class="text-danger mt-3 text-center" id="error"></p>
			<hr />
			<p class="text-center mb-0">
				Don't have an account? <a href="<c:url value='/signup' />">Sign
					Up</a>
			</p>
		</div>
	</div>

	<script src="<c:url value='/js/bootstrap.bundle.min.js' />"></script>
	<script src="<c:url value='/js/login.js' />"></script>
</body>
</html>
