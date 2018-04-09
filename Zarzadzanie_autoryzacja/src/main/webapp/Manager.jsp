<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manager</title>
</head>
<body>
	<form method="get">
		Username <input type="text" name="Username"> <select id="level" name="level">
			<option value="ZWYKLY">Zwykly</option>
			<option value="PREMIUM">Premium</option>
			<option value="ADMIN">Admin</option></select> <input type="submit"
			value="Submit" formaction="manager"> <input type="reset" />
			<input type="submit" name="users" value="List Users" formaction="users" />
			<input type="submit" name="goback" value="Go back" formaction="userprofile" />
	</form>
	<p>${managerSuccess}</p>
	<p>${returnHtml}</p>
	<p>${dbError}</p>
</body>
</html>