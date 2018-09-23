<!--
  - Author(s): AdarshSinghal
  - Date: 09/07/2018
  - Description: 
-->


<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="app.service.TextTranslator, java.util.Locale"%>
<%
	Locale locale = request.getLocale();
	TextTranslator svc = new TextTranslator(locale.getLanguage());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Success</title>
<link rel="stylesheet" type="text/css"
  href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.min.css" />
  <style>
    td,th{
      padding: 5px 20px;
    }
  </style>
</head>
<body>
  <%@ include file="/parts/header.jsp"%>
  <div class="container">
    <div class="card" style="width: 500px; margin: 100px auto;">
      <div class="card-header alert-success">Successfully
        Published on ${fn:length(messageIds)} topics</div>
      <div class="card-body">
      
        <table>
          <tr>
            <th>Global Transaction Id:</th>
            <td><%=request.getAttribute("gbTxnId")%></td>
          </tr>
        </table>


        <table class="table-bordered table-striped mt-3 mb-5">

          <thead>
            <tr>
              <th>Published on Topic</th>
              <th>Message Id from PubSub</th>
            </tr>
          </thead>

          <tbody>

            <c:forEach items="${messageIds}" var="msgId">
              <tr>
                <td>${msgId.topicName}</td>
                <td>${msgId.messageId}</td>
              </tr>
            </c:forEach>


          </tbody>

        </table>



        <div style="text-align: center">
          <a class="btn btn-dark float-left"
            href="/pages/MessageSource.jsp">&lt;&lt;Message Source</a> <span
            class="span4 text-center"><a class="btn btn-dark"
            href="/pages/dbtables/publishdata.jsp">Show Publish
              Table</a></span> <a class="btn btn-dark float-right"
            href="/pages/syncpullclient.jsp">Pull&gt;&gt;</a>
        </div>
      </div>
    </div>
  </div>
  <script
    src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>
</html>