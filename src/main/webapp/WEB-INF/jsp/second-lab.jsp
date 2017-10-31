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
    <h3 align="center">RSA</h3>
    <form method="POST" action="/lab2/generateRSAKeys">
        <input style="width: 100%;" type="submit" value="Generate new private key & public key">
        <h4>Is private & public key generated: <c:out value="${isRSAKeyGenerated}"/></h4>
    </form>
    <form method="POST" action="/lab2/encryptRSA">
        <table class="table table-striped">
            <tr>
                <td style="width:40%">Input value:</td>
                <td>
                    <input style="width:100%" type="text" name="inputText"
                           placeholder="type what do you want to encrypt"/>
                </td>
            </tr>
            <tr>
                <td>Use default private & public keys'</td>
                <td>
                    <input type="checkbox" name="isDefaultKeysUsed"/>
                </td>
            </tr>
            <tr>
                <td>Result</td>
                <td>
                    <c:out value="${rsaEncryptedResult}"/>
                </td>
            </tr>
            <tr>
                <td colspan="3"><input ${ isRSAKeyGenerated==null ? 'disabled="disabled"' : ''} type="submit"
                                                                                                value="Encrypt">
                </td>
            </tr>
        </table>
    </form>
    <form method="POST" action="/lab2/decryptRSA">
        <table class="table table-striped">
            <tr>
                <td style="width:40%">Use default private & public keys'</td>
                <td>
                    <input type="checkbox" name="isDefaultKeysUsed"/>
                </td>
            </tr>
            <tr>
                <td>Result</td>
                <td>
                    <c:out value="${rsaDecryptedResult}"/>
                </td>
            </tr>
            <tr>
                <td colspan="3"><input ${ isRSAKeyGenerated==null ? 'disabled="disabled"' : ''} type="submit"
                                                                                                value="Decrypt">
                </td>
            </tr>
        </table>
    </form>
    <h3 align="center">ElGamal</h3>
    <form method="POST" action="/lab2/generateGamal">
        <table class="table table-striped">
            <tr>
                <td style="width:40%">Generate keys</td>
                <td>
                    <input type="number" name="initValue"
                           placeholder="init prime value"/>
                </td>
                <td>
                    <input type="submit" value="Generate">
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    Public key: <c:out value="${publicGamalKey}"/>
                </td>
            </tr>
        </table>
    </form>
    <form method="POST" action="/lab2/encryptGamal">
        <table class="table table-striped">
            <tr>
                <td style="width:40%">Input value</td>
                <td>
                    <input type="number" name="inputText"
                           placeholder="type what do you want to encrypt"/>
                </td>
                <td>
                    <input type="submit" value="Crypt me!">
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    Encrypted result: <c:out value="${encryptedGamalValue}"/>
                </td>
            </tr>
        </table>
    </form>
    <form method="POST" action="/lab2/decryptGamal">
        <table class="table table-striped">
            <tr>
                <td colspan="3">
                    Private key: <c:out value="${privateGamalKey}"/>
                </td>
            </tr>
            <tr>
                <td style="width:40%">
                    Decrypted result:
                </td>
                <td>
                   <c:out value="${initDecryptedValue}"/>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <input type="submit" value="Decrypt me!">
                </td>
            </tr>
        </table>
    </form>
    <h4 align="center">Is digit prime?</h4>
    <form method="POST" action="/lab2/isPrime">
        <table class="table table-striped">
            <tr>
                <td style="width:40%">Input value</td>
                <td>
                    <input type="number" name="maybePrime"
                           placeholder="type what do you want to check"/>
                </td>
                <td>
                    <input type="submit" value="Check!">
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    Is prime? <b><c:out value="${isPrime}"/></b>
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>

</html>