<%-- 
    Document   : insertProduct
    Created on : 18 Jun 2025, 18:49:57
    Author     : nam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="ProductURL" method="post">
    <input type="hidden" name="service" value="insertProduct">
    <table>
        <tr>
            <td>ProductID</td>
            <td><input type="text" name="ProductId" required></td>
        </tr>
        <tr>
            <td>ProductName</td>
            <td><input type="text" name="ProductName" required></td>
        </tr>
        <tr>
            <td>Entry Price</td>
            <td><input type="number" step="0.01" name="EntryPrice" required></td>
        </tr>
        <tr>
            <td>Retail Price</td>
            <td><input type="number" step="0.01" name="RetailPrice" required></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><textarea name="Description"></textarea></td>
        </tr>
        <tr>
            <td>WHOSALEPRICE</td>
            <td><input type="number" step="0.01" name="WHOSALEPRICE" required></td>
        </tr>
        <tr>
            <td>Unit</td>
            <td><input type="text" name="Unit" required></td>
        </tr>
        <tr>
            <td>Quantity</td>
            <td><input type="number" name="Quantity" required></td>
        </tr>
        <tr>
            <td>CategoryId</td>
            <td><input type="number" name="CategoryId" required></td>
        </tr>
        <tr>
            <td>SupplierID</td>
            <td><input type="number" name="SupplierID" required></td>
        </tr>
        <tr>
            <td>Status</td>
            <td><input type="number" name="status" required></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" name="submit" value="Insert Product">
            </td>
        </tr>
    </table>
</form>

    </body>
</html>
