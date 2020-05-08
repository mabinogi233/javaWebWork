<%--
  Created by IntelliJ IDEA.
  User: 刘文泽
  Date: 2020/5/8
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>login WebService</title>
</head>



<body>
<form method='post' action='login'>
  <input type='text' name='username'><br/>
  <input type='password' name='password'><br/>
  <input type='text' name='checkcode'><br/>
  <img id="imgCode" src="createCode" onclick="this.src=this.src+'?'"/>
  <input type="button" value="change" id="btn" onclick="imgCode.src=imgCode.src+'?'">
  <input type="submit" value="login">
</form>
</body>
</html>
