<%@page import="java.util.Collections"%>
<%@page import="java.net.NetworkInterface"%>
<%@page import="java.net.InetAddress"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page buffer="500kb"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Add a contact | ANWEB</title>
		<link href="assets/css/bootstrap.css" rel="stylesheet" />
		<link href="assets/font-awesome-4.4.0/css/font-awesome.css" rel="stylesheet" />
		<link href="assets/css/custom-styles.css" rel="stylesheet" />
		<link href="assets/css/sweetalert.css" rel="stylesheet" type="text/css">
	</head>
	<style>
		#list-contact input {
			text-align: center;
		}
		#alerte {
			position: relative;
    		-webkit-animation-name: example; /* Chrome, Safari, Opera */
    		-webkit-animation-duration: 10s; /* Chrome, Safari, Opera */
		    -webkit-animation-iteration-count: infinite; /* Chrome, Safari, Opera */
		    animation-name: example;
		    animation-duration: 10s;
		    animation-iteration-count: infinite;
		}
		@-webkit-keyframes example {
    		0%   {left:0; top:0;}
    		50%  {left:50%; top:50%;}
    		100% {left:0; top:0;}
		}
	</style>
	<body>
		<div class="row" style="margin: 20px;">
			<div class="col-lg-5">
				<h1><a href="#" style="text-decoration: none;">Annuaire WEB</a></h1>
			</div>
			<div class="col-lg-7">
				<div class="pull-right">
					 <h4>Welcome Pr. Hagimont <!--| <c:out value="${sessionUser.email}"/> | <a href="<c:url value="/logout" />" style="text-decoration: none;">Sign Out</a>--></h4>  
				</div>
			</div>
		</div>
		<div class="row" style="margin: 20px 40px; margin-top: 50px ;">
			<div class="row">
				<div class="col-lg-offset-4 col-sm-offset-1 col-lg-4 col-md-4 col-sm-8 col-xs-12">
					<fieldset>
						<legend><b>Add a contact</b></legend>
						<form id="form-contact" class="form-horizontal" role="form" action="<c:url value="/add" />" method="post">
						  <div class="form-group">
						    <label class="control-label col-sm-2" for="nom">Name</label>
						    <div class="col-sm-8">
						      <input name="nom" type="text" value="${ contact.nom }" class="form-control" id="nom" placeholder="Enter name" required="" />
						    </div>
						    <div class="col-sm-12">
						      	<span class="alert-danger">${form.errors['nom']}</span>
						    </div>
						  </div>
						  <div class="form-group">
						    <label class="control-label col-sm-2" for="tel">Phone</label>
						    <div class="col-sm-8"> 
						      <input name="tel" type="text" class="form-control" value="${ contact.tel }" pattern="6 [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}" id="tel" 
						      placeholder="Enter tel" required="" title="Respecter le format ex : 6 99 76 54 67" />
						    </div>
						    <div class="col-sm-12">
						      	<span class="alert-danger">${form.errors['tel']}</span>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="col-sm-offset-2 col-sm-8">
						    	<button type="reset" class="btn btn-primary">Reset</button>
						      	<button type="submit" class="btn btn-success">Add</button>
						      	<!--<a href="<c:url value="/view" />">View Contacts</a>-->
						    </div>
						  </div>
						  <div class="row">
						  		<span class="${empty form.errors ? 'alert-success' : 'alert-danger'}">${form.result}</span>
						  </div>
						</form>
					</fieldset>
				</div>
			</div>
			<div class="row" style="margin: 20px;">
				<div class="col-lg-8 col-md-8 col-sm-10 col-xs-12 col-lg-offset-2 col-md-offset-2 col-sm-offset-2 col-xs-offset-2">
					<!-- <div class="row" style="text-align: center;margin-top: -15px;">
						<h4 style="color: #428BCA;">List of all your contacts</h4>
					</div>-->
	
					<div class="row">
						<div class="col-lg-12 col-lg-offset-0">
							<div class="panel panel-default">
			                    <div class="panel-heading" style="text-align: center;">
			                        <span style="font-size: 20px;"><b>List of all your contacts</b></span>
			                    </div>
			                    <div class="panel-body">
			                        <table id="list-contact" class="table table-striped table-bordered">
							            <thead>
							                <tr>
							                    <th class="text-center">#</th>
							                    <th class="text-center">Name</th>
							                    <th class="text-center">Phone</th>
							                    <th class="text-center">Action</th>
							                </tr>
							            </thead>
								        <c:if test="${!empty contacts}">
											<tbody>
								                <c:set var="rang" value="0" />
								                <c:forEach items="${contacts}" var="contact">
								                	<c:set var="rang" value="${rang+1}" />
								                	<tr>
								                		<td class="text-center" contactid="<c:out value="${contact.id}" />" ><c:out value="${rang}" /></td>
								                		<td class="text-center tt"> <input class="form-control" type="text" value="<c:out value="${contact.nom}" />" /> </td>
								                		<td class="text-center tt"> <input class="form-control" type="text" pattern="6 [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}" value="<c:out value="${contact.tel}" />" /> </td>
								                		<td class="text-center">
								                			<!-- <button class="btn btn-success bt-save">
							                                    <i class=" fa fa-save "></i>
							                                </button>-->
							                                <button class="btn btn-danger bt-delete">
							                                    <i class=" fa fa-minus-circle "></i>
							                                </button>
								                		</td>
								                	</tr>
								                </c:forEach>
							            	</tbody>
								        </c:if>
							        </table>
			                    </div>
			                    <div class="panel-footer">
			                        <div class="row">
										<div class="col-lg-12 col-lg-offset-0">
									    	<span class="" id="alerte"></span>
									    </div>
									</div>
			                    </div>
			                </div>
					    </div>
					</div>
				</div>
				<div class="row" id="content-img-load">
	               <div class="img-load" style="text-align: center;position: fixed;bottom:5%;right:5%"></div>
	           </div>
			</div>
			<footer>
				<p style="bottom: 0px;position: fixed;">copyrigths &COPY; <a id="author" href="#" style="text-decoration: none;"><c:out value="Phoenix Group 2015" /></a></p>
				<p class="row" style="font-size: 15px;position: fixed; right: 20px;">
					<a href="/status" style="text-decoration: none;"><b>Executed by 
						<% out.println(InetAddress.getLocalHost().getHostName()+" - "+InetAddress.getLocalHost().getHostAddress()); %>
					</b></a>
				</p>
				<!-- <p style="position: fixed; right: 0;">Par MOUAFFO REINE - TABUE ROMEO - TUENO STEVE - TEGANTCHOUANG BORIS</p>-->
			</footer>
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

	<!-- Annuaire Js -->
	<script type="text/javascript" src="assets/js/annuaire.js"></script>
</html>