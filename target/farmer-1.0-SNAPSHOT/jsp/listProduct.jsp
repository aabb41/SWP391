<%-- 
    Document   : listProduct
    Created on : 18 Jun 2025, 18:15:14
    Author     : nam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entity.Products, java.util.Vector" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Products</title>
    </head>
    <body>
        <p align="right">
            <%//mapName.getKey(keyvalue) --> value
             //session.getAttribute(keyvalue) --< value
                String userName =(String)session.getAttribute("userName");
            %>
        <a href="${pageContext.request.contextPath}/">Home</a>

        </p>
        
        
        <% //get date form controller
               Vector<Products> vector = (Vector<Products>)request.getAttribute("productData");
               String titleTable = (String)request.getAttribute("titleTable");
        %>
        <div class="search-form">
            <form action="ProductURL" method="get">
                <input type="hidden" name="service" value="listProduct">
                <label for="searchField">Search by:</label>
                <select name="searchField" id="searchField">
                    <option value="ProductID">Produc ID</option>
                </select>
                <input type="text" name="searchValue" placeholder="Enter search value">
                <p><input type="submit" name="submit" value="Search">
                <input type="reset" value="Clear">
                </div>

        <p><a href="ProductURL?service=insertProduct">insertProduct</a></p>
        <table>
            <caption><%=titleTable%></caption>
            <tr>
        <th>ProductID</th>
        <th>ProductName</th>
        <th>EntryPrice</th>
        <th>RetailPrice</th>
        <th>Description</th>
        <th>WhoSalePricre</th>
        <th>Unit</th>
        <th>CategoryID</th>
        <th>SupplierID</th>
        <th>CreateAt</th>
        <th>Update</th>
        <th>status</th>
      </tr>
            <% for (Products products : vector){%>
          <tr>
        <td><%=products.getProductId()%></td>
        <td><%=products.getProductName()%></td>
        <td><%=products.getEntryPrice()%></td>
        <td><%=products.getRetailPrice()%></td>
        <td><%=products.getDescription()%></td>
        <td><%=products.getWHOSALEPRICE()%></td>
        <td><%=products.getUnit()%></td>
        <td><%=products.getCategoryId()%></td>
        <td><%=products.getSupplierID()%></td>
        <td><%=products.getCreateAt()%></td>
        <td><%=products.getUpdate()%></td>
        <td><%=products.getStatus()%></td>
        
      </tr>
      <%}%>
        </table>
    </body>
</html>
