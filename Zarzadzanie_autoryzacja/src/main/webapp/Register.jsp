<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
<form action="register" method="post"> Username:<br>
  <input type="text" name="Username"><br>
 Password:<br>
  <input type="text" name="Password"><br>
  Confirm Password:<br>
   <input type="text" name="ConPassword"><br>
   Email:<br>
    <input type="text" name="Email"><br>
    <input type="submit" value="Submit">
    <input type="reset"/><br>
    <input type="submit" name="login" value="Log in" formaction="/Login.jsp" />
    </form>
    <p>${regComplete}</p>
    <p>${regError}</p>
    <p>${dbError}</p>
</body>
</html>