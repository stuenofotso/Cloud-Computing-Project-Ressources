<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Connection | ANWEB</title>
		<link href="assets/css/bootstrap.css" rel="stylesheet" />
		<link href="assets/css/custom-styles.css" rel="stylesheet" />
		<link href="assets/css/sweetalert.css" rel="stylesheet" />
	</head>
	<body>
		<div class="row" style="margin: 20px; margin-top: 200px ;">
			<div class="col-lg-offset-3 col-md-offset-3 col-sm-offset-2 col-xs-offset-2 col-lg-6 col-md-6 col-sm-8 col-xs-12">
				<fieldset>
					<legend style="color: blue;">Login</legend>
					<form class="form-horizontal col-lg-offset-3 col-sm-offset-2" role="form" action="<c:url value="/login" />" method="post">
					  <div class="form-group">
					    <label class="control-label col-sm-2 col-xm-6" for="email">Email</label>
					    <div class="col-sm-6">
					      <input type="email" class="form-control" value="<c:out value="${user.email}"/>" name="email" id="email" placeholder="Enter email" required="" />
					    </div>
					    <div class="col-sm-4">
					      	<span class="alert-danger">${form.errors['email']}</span>
					    </div>
					  </div>
					  <div class="form-group">
					    <label class="control-label col-sm-2 col-xm-6" for="pwd">Password</label>
					    <div class="col-sm-6"> 
					      <input type="password" class="form-control" name="passwd" id="pwd" placeholder="Enter password" required="">
					    </div>
					    <div class="col-sm-4">
					      	<span class="alert-danger">${form.errors['passwd']}</span>
					    </div>
					  </div>
					  <div class="form-group">
					    <div class="col-sm-offset-2 col-sm-6">
					    	<button type="reset" class="btn btn-primary">Reset</button>
					      	<button type="submit" class="btn btn-success">Submit</button>
					    </div>
					  </div>
					</form>
					<hr />
				</fieldset>
			</div>
		</div>
	</body>

    <!-- jQuery Js -->
    <script src="assets/js/jquery-1.10.2.js"></script>

    <!-- Bootstrap Js -->
    <script src="assets/js/bootstrap.min.js"></script>

	<!-- Custom Js -->
    <script src="assets/js/custom-scripts.js"></script>

	<!-- SweetAlert Js -->
	<script src="assets/js/sweetalert.min.js"></script>
</html>