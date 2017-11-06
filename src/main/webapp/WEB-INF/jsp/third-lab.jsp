<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
</head>
<body>

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">RSA, ElGamal algorithms [SECOND LAB]</a>
        </div>
    </div>
</nav>

<div class="container">
    <h3 align="center">MD5 & SHA</h3>
    <form method="POST" action="/lab3/encrypt">
        <table class="table table-striped">
            <tr>
                <td style="width:40%">Input value:</td>
                <td>
                    <input style="width:100%" type="text" name="inputData"
                           placeholder="type what do you want to encrypt"/>
                </td>
            </tr>
            <tr>
                <td>Result MD5</td>
                <td>
                    <c:out value="${result_md5}"/>
                </td>
            </tr>
            <tr>
                <td>Result SHA-512</td>
                <td>
                    <c:out value="${result_sha}"/>
                </td>
            </tr>
            <tr>
                <td colspan="3"><input type="submit" value="Encrypt">
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>

</html>