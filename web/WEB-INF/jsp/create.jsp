<%--
  Created by IntelliJ IDEA.
  User: liuwenze
  Date: 2022/7/26
  Time: 8:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图书管理系统 v1.0.0</title>
</head>
<body>
<form method="post" action="/book/insert">
    <label>名称</label><input type="text" name="name">
    <label>作者</label><input type="text" name="author">
    <label>库存</label><input type="text" name="stock">
    <input name="提交" type="submit">
</form>

<form method="post" action="/book/select">
    <input type="hidden" name="p" value="0"><br/>
    <input type="hidden" name="s" value="10"><br/>
    <input type="submit" value="返回主页面">
</form>
</body>
</html>
