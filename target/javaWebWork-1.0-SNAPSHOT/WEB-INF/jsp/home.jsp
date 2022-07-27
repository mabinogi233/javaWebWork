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
  <title>图书管理系统 v1.0.0</title>
</head>
<body>
<h1>请登陆系统，该系统30天内免登陆</h1>
<form method='post' action='/login/login'>
  <h4>用户名</h4><input type='text' name='username'><br/>
  <h4>密码</h4>><input type='password' name='password'><br/>
  <input type="submit" value="login">
</form>
</body>
</html>
