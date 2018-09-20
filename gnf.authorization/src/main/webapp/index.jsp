<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Authorization Service</title>

<style>
th {
	padding-right: 100px;
}

pre {
	background-color: #f2f2f2;
}

table {
	background-color: #f2f2f2;
}

td, th {
	border: 1px solid silver;
	padding: 5px 15px;
}

</style>

</head>

<body>

  <div id="container" style="text-align: -webkit-center;">

    <h1>Guidelines</h1>

    <div id="table-div">

      <table>

        <tr>
          <th>URL</th>
          <td>/authorize</td>
        </tr>

        <tr>
          <th>Method</th>
          <td>POST</td>
        </tr>
        <tr>
          <th>Request Body</th>
          <td><pre>
{
    &quot;sourceAuthLevel&quot;: 1,
    &quot;groupId&quot;: &quot;1&quot;
}

			</pre></td>
        </tr>

      </table>

    </div>

  </div>




</body>
</html>