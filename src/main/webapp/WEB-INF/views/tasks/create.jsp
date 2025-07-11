<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Create Task</title>
<link rel="stylesheet" href="/css/bootstrap.css" />
<link rel="stylesheet" href="/css/select2.min.css" />

<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/select2.min.js"></script>

<style>
body {
	background-color: #f8f9fa;
}

.card {
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	border-radius: 10px;
}

.form-title {
	font-size: 24px;
	font-weight: bold;
}

.select2-container--default .select2-selection--multiple .select2-selection__choice
	{
	border: 2px solid #007bff;
	background-color: #e9f2ff;
	color: #33577e;
	font-weight: 500;
	border-radius: 6px;
	margin-right: 4px;
}

.select2-container--default .select2-selection--multiple .select2-selection__choice__remove:hover
	{
	color: #c44646;
}

.select2-container .select2-selection--multiple {
	min-height: 38px;
	padding-left: 5px;
	border: 1px solid #ced4da;
}

.file-drop {
	border: 2px dashed #6c757d;
	padding: 20px;
	border-radius: 5px;
	text-align: center;
	color: #6c757d;
	transition: all 0.3s ease-in-out;
}

.file-drop:hover {
	background-color: #f8f9fa;
	border-color: #007bff;
	color: #007bff;
}
</style>
</head>
<body>
	<div class="container mt-5">
		<div class="card p-4">
			<h3>Create New Task</h3>
			<form method="post" action="/tasks/create"
				enctype="multipart/form-data">
				<div class="form-group mb-3">
					<label>Title</label> <input type="text" name="title"
						class="form-control" required />
				</div>

				<div class="form-group mb-3">
					<label>Description</label>
					<textarea name="description" class="form-control rich-text-editor"
						rows="5"></textarea>
				</div>

				<div class="form-group mb-3">
					<label>Assign To</label> <select name="assignedUserIds"
						class="form-control js-user-select" multiple="multiple" required>
						<c:forEach var="user" items="${users}">
							<option value="${user.id}">${user.name}</option>
						</c:forEach>
					</select> <small class="form-text text-muted">Search and select
						multiple users.</small>
				</div>

				<div class="form-group mb-3">
					<label>Priority</label> <select name="priority"
						class="form-control" required>
						<c:forEach var="p" items="${priorities}">
							<option value="${p}">${p}</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group mb-3">
					<label>Status</label> <select name="status" class="form-control"
						required>
						<c:forEach var="s" items="${statuses}">
							<option value="${s}">${s}</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group mb-3">
					<label>Tags (comma-separated)</label> <input type="text"
						name="tags" class="form-control" />
				</div>

				<div class="form-group mb-3">
					<label>Estimated Hours</label> <input type="number"
						name="estimatedHours" step="0.1" min="0.1" class="form-control"
						required />
				</div>

				<div class="form-group mb-3">
					<label>Deadline</label> <input type="date" name="deadline"
						class="form-control" required />
				</div>



				<div class="form-group mb-3">
					<label class="form-label">Attachments</label>
					<div class="file-drop"
						onclick="document.getElementById('fileUpload').click();">
						Click or drag files here (PDF, JPG, PNG, DOCX) – max 5MB each</div>
					<input type="file" name="attachments" id="fileUpload" multiple
						style="display: none" accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
						onchange="showSelectedFiles(this)" />
					<div id="fileList" class="mt-2 text-muted"></div>
				</div>

				<div class="d-flex justify-content-between align-items-center mt-3">
					<button type="submit" class="btn btn-primary">Create Task</button>
					<a href="<c:url value='/dashboard'/>" class="btn btn-secondary">Cancel</a>
				</div>
			</form>
		</div>
	</div>

	<!-- TinyMCE (for rich text editor) -->
	<script src="/js/tinymce/tinymce.min.js"></script>

	<script>
    $(document).ready(function () {
        $('.js-user-select').select2({
            placeholder: "Select users...",
            width: '100%',
            allowClear: true
        });

        tinymce.init({
            selector: '.rich-text-editor',
            height: 250,
            menubar: false,
            plugins: 'link image code lists',
            toolbar: 'undo redo | bold italic underline | bullist numlist | link image | code',
            content_style: "body { font-family:Arial; font-size:14px }"
        });
    });
    
    function showSelectedFiles(input) {
        const list = document.getElementById("fileList");
        list.innerHTML = "";
        for (const file of input.files) {
            const size = (file.size / 1024 / 1024).toFixed(2);
            const line = document.createElement("div");
            line.textContent = `✔ ${file.name} (${size} MB)`;
            list.appendChild(line);
        }
    }
    
</script>
</body>
</html>
