<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
</head>
<body>

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">DES, AES algorithms [FIRST LAB]</a>
        </div>
    </div>
</nav>

<div class="container">
    <form method="POST" action="/lab1/generateKey">
        Please, before encrypting generate a secret key for method:
        <select name="method">
            <c:forEach items="${methodList}" var="method">
                <option value="${method.key}">${method.key}</option>
            </c:forEach>
        </select>
        <br>
        <input style="width: 100%;" type="submit" value="Generate new secret key">

    </form>
    <c:out value="Is key generated? ${secretKey!=null}"/><br>
    <c:out value="Selected method: ${selectedMethod}"/>
    <br>
    <br>
    <form method="POST" action="/lab1/encrypt">
        <table class="table table-striped">
            <tr>
                <td width="40%">Type</td>
                <td>
                    <select name="methodType">
                        <c:forEach items="${typeList}" var="type">
                            <option>${type.key}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Input data</td>
                <td>
                    <input style="width:100%" type="text" name="inputData"
                           placeholder="type what do you want to encrypt"/>
                </td>
            </tr>
            <tr>
                <td>Use key from file 'key.txt'</td>
                <td>
                    <input type="checkbox" name="isFileUsed"/>
                </td>
            </tr>
            <tr>
                <td>Result</td>
                <td>
                    <c:out value="${result}"/>
                </td>
            </tr>
            <tr>
                <td colspan="3"><input ${ secretKey==null ? 'disabled="disabled"' : ''} type="submit" value="Encrypt">
                </td>
            </tr>
        </table>
    </form>


    <form method="POST" action="/lab1/decrypt">
        <table class="table table-striped">
            <tr>
                <td width="40%">
                    Decrypt last encrypted value:
                </td>
                <td>
                    Value: <c:out value="${result}"/>/>
                </td>
            </tr>
            <tr>
                <td>
                    Algorithm:
                </td>
                <td>
                    Value: <c:out value="${selectedMethod}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Method type:
                </td>
                <td>
                    Value: <c:out value="${encryptedMethodType}"/>
                </td>
            </tr>
            <tr>
                <td>
                    Use key from file 'key.txt'
                </td>
                <td>
                    <input type="checkbox" name="isFileUsed"/>
                </td>
            </tr>
            <tr>
                <td>
                    Initial value is:
                </td>
                <td>
                    <c:out value="${initValue}"/>
                </td>
            </tr>
            <tr>
                <td colspan="3"><input type="submit" value="Decrypt it back">
                </td>
            </tr>
        </table>
    </form>

</div>

<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>

</html>