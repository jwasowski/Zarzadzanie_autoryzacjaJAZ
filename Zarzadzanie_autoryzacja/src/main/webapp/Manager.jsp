<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manager</title>
</head>
<body>
	<form method="post">
		Username <input type="text" name="Username"> <select><option
				value="zwykly">Zykly</option>
			<option value="premium">Premium</option>
			<option value="admin">Admin</option></select> <input type="submit"
			value="Submit" formaction="manager"> <input type="reset" />
			<input type="submit" name="users" value="List Users" formaction="users" />
	</form>
</body>
</html>