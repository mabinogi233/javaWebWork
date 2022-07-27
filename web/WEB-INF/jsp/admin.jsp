<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: liuwenze
  Date: 2022/7/26
  Time: 4:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>图书管理系统 v1.0.0</title>
    <script>

    </script>
</head>
<body>
<h1>欢迎使用图书管理系统 v1.0.0</h1>
<br/>
<br/>
<a href="/book/insertPage">新增图书</a>
<br/>
<br/>
<a href="/book/removeall">删除全文索引</a>
<br/>
<br/>
<a href="/book/init">重建全文索引</a>
<br/>
<br/>

<form method="post" action="/book/select">
    <label>书名（支持模糊查询与联合查询）</label><input type="text" name="name"><br/>
    <label>作者（支持模糊查询与联合查询）</label><input type="text" name="author"><br/>
    <input type="hidden" name="p" value="0"><br/>
    <input type="hidden" name="s" value="10"><br/>
    <input type="submit" value="提交">
</form>
<br/>
<br/>
<table>
    <thead>
    <th width="20%">编号</th>
    <th width="20%">书名</th>
    <th width="20%">作者</th>
    <th width="20%">库存</th>
    <th width="20%">操作</th>
    </thead>
    <tbody>
    <!--/*@thymesVar id="books" type="java.util.List"*/-->
    <select>
    <c:forEach var="value" items="${books}">
    <tr>
        <td>${value.bid}</td>
        <td>${value.bname}</td>
        <td>${value.bauthor}</td>
        <td>${value.bstock}</td>
        <td>
            <a href="/book/selectOne?id=${value.bid}">修改</a> &nbsp;&nbsp;
            <a href="/book/delete?id=${value.bid}">删除</a>
        </td>
    </tr>
    </c:forEach>
    </select>
    </tbody>
</table>

<c:if test="${haslast}">
    <form method="post" action="/book/select">
        <input type="hidden" name="name" value="${name}"><br/>
        <input type="hidden" name="author" value="${author}"><br/>
        <input type="hidden" name="p" value="${lp}"><br/>
        <input type="hidden" name="s" value="10"><br/>
        <input type="submit" value="上一页">
    </form>
</c:if>
<div>当前页：${p}</div>
<c:if test="${hasnext}">
    <form method="post" action="/book/select">
        <input type="hidden" name="name" value="${name}"><br/>
        <input type="hidden" name="author" value="${author}"><br/>
        <input type="hidden" name="p" value="${np}"><br/>
        <input type="hidden" name="s" value="10"><br/>
        <input type="submit" value="下一页">
    </form>
</c:if>
</body>
</html>