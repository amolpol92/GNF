<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>DLP Service</title>

<style>
th {
	padding-right: 100px;
}

pre {
	background-color: #f2f2f2;
}

table {
    width:800px;
	background-color: #f2f2f2;
}

td, th{
	border: 1px solid silver;
	padding: 5px 15px;
}

#deidentify-table-div{
  margin: 30px 0px; 
}

</style>

</head>

<body>

  <div id="container" style="text-align: -webkit-center;">

    <h1>Guidelines</h1>

    <div id="inspect-table-div">

      <table>

        <tr>
          <th>URL</th>
          <td>/inspect</td>
        </tr>

        <tr>
          <th>Method</th>
          <td>POST</td>
        </tr>
        <tr>
          <th>Request Body</th>
          <td><pre>
{
    &quot;message&quot;: &quot;Message on which you want to perform DLP Inspection&quot;
}

			</pre></td>
        </tr>

      </table>

    </div>
    
    <div id="deidentify-table-div">

      <table>

        <tr>
          <th>URL</th>
          <td>/deidentify</td>
        </tr>

        <tr>
          <th>Method</th>
          <td>POST</td>
        </tr>
        <tr>
          <th>Request Body</th>
          <td><pre>
{
    &quot;message&quot;: &quot;Message on which you want to perform DLP Deidentification&quot;
}

      </pre></td>
        </tr>

      </table>

    </div>

  </div>




</body>
</html>