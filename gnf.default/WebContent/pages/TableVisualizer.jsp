<!--
  - Author(s): AdarshSinghal
  - Date: 09/07/2018
  - Description: Useful for developers to view table structure on page. 
  -     Hidden from UI page but created with intention of saving time 
  -     & convenience for all developers.
  -     These are the SQL queries covered:- SHOW TABLES, DESCRIBE TABLE, SHOW CREATE TABLE
-->


<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="app.service.TextTranslator, java.util.Locale"%>
<%
	Locale locale = request.getLocale();
	TextTranslator translator = new TextTranslator(locale.getLanguage());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Table Visualizer</title>
</head>

<style>
td>a {
	color: black !important;
}
</style>

<body>
  <%@ include file="/parts/html-head.jsp"%>
  <%@ include file="/parts/header.jsp"%>


  <table
    class="table-bordered table-striped table-condensed mt-5 mr-1 float-right sticky-top">
    <tr>
      <th><%=translator.translate("Table of Contents")%></th>
    </tr>
    <tr>
      <td><a href="#show-tables"><%=translator.translate("SHOW TABLES")%></a></td>
    </tr>
    <tr>
      <td><a href="#User_Details"> <%=translator.translate("User_Details")%></a></td>
    </tr>
    <tr>
      <td><a href="#User_Preferences"> <%=translator.translate("User_Preferences")%></a></td>
    </tr>

    <tr>
      <td><a href="#activity_logging"> <%=translator.translate("activity_logging")%>
      </a></td>
    </tr>
    <tr>
      <td><a href="#group_membership"> <%=translator.translate("group_membership")%>
      </a></td>
    </tr>
    <tr>
      <td><a href="#message_status_cache_db"> <%=translator.translate("message_status_cache_db")%>
      </a></td>
    </tr>
    <tr>
      <td><a href="#publisher"> <%=translator.translate("publisher")%>
      </a></td>
    </tr>
    <tr>
      <td><a href="#subscriber"> <%=translator.translate("subscriber")%>
      </a></td>
    </tr>

    <tr>
      <td><a href="#user_group_details"> <%=translator.translate("user_group_details")%>
      </a></td>
    </tr>
    
     <tr>
      <td><a href="#createTable"> <%=translator.translate("CREATE TABLE")%>
      </a></td>
    </tr>
    
  </table>

  <div class="container">

    <h6 id="show-table" class="mt-5">SHOW TABLES</h6>

    <pre>
+-------------------------+
| Tables_in_msgdb         |
+-------------------------+
| User_Details            |
| User_Preferences        |
| activity_logging        |
| group_membership        |
| message_status_cache_db |
| publisher               |
| subscriber              |
| user_group_details      |
+-------------------------+
  
  </pre>

    <h6 id="User_Details">DESCRIBE User_Details;</h6>
    <pre>
+--------------------+-----------------+------+-----+---------+----------------+
| Field              | Type            | Null | Key | Default | Extra          |
+--------------------+-----------------+------+-----+---------+----------------+
| user_id            | int(6) unsigned | NO   | PRI | NULL    | auto_increment |
| user_name          | varchar(50)     | NO   |     | NULL    |                |
| user_email_id      | varchar(50)     | NO   |     | NULL    |                |
| user_mobile_number | varchar(50)     | YES  |     | NULL    |                |
| user_fax_number    | varchar(50)     | YES  |     | NULL    |                |
+--------------------+-----------------+------+-----+---------+----------------+
  
  </pre>

    <h6 id="User_Preferences">DESCRIBE User_Preferences</h6>

    <pre>
+----------------+-----------------+------+-----+---------+----------------+
| Field          | Type            | Null | Key | Default | Extra          |
+----------------+-----------------+------+-----+---------+----------------+
| id             | int(6) unsigned | NO   | PRI | NULL    | auto_increment |
| user_name      | varchar(50)     | NO   |     | NULL    |                |
| email_prefered | varchar(10)     | NO   |     | NULL    |                |
| sms_prefered   | varchar(10)     | NO   |     | NULL    |                |
| fax_prefered   | varchar(10)     | YES  |     | NULL    |                |
| phone          | varchar(10)     | YES  |     | NULL    |                |
| user_id        | int(6)          | NO   |     | NULL    |                |
+----------------+-----------------+------+-----+---------+----------------+
  </pre>

    <h6 id="activity_logging">DESCRIBE activity_logging</h6>

    <pre>
+---------------------+--------------+------+-----+-------------------+----------------+
| Field               | Type         | Null | Key | Default           | Extra          |
+---------------------+--------------+------+-----+-------------------+----------------+
| id                  | int(11)      | NO   | PRI | NULL              | auto_increment |
| message_id          | varchar(100) | NO   |     | NULL              |                |
| message_data        | text         | NO   |     | NULL              |                |
| subscription_name   | varchar(20)  | YES  |     | NULL              |                |
| published_timestamp | timestamp    | NO   |     | CURRENT_TIMESTAMP |                |
| glo_tran_id         | varchar(30)  | YES  |     | NULL              |                |
| topic_name          | varchar(20)  | YES  |     | NULL              |                |
+---------------------+--------------+------+-----+-------------------+----------------+
    </pre>



    <h6 id="group_membership">DESCRIBE group_membership</h6>
    <pre>
+----------+--------+------+-----+---------+-------+
| Field    | Type   | Null | Key | Default | Extra |
+----------+--------+------+-----+---------+-------+
| user_id  | int(6) | NO   |     | NULL    |       |
| group_id | int(6) | NO   |     | NULL    |       |
+----------+--------+------+-----+---------+-------+
</pre>


    <h6 id="message_status_cache_db">DESCRIBE
      message_status_cache_db</h6>
    <pre>
+-------------+-------------+------+-----+---------+-------+
| Field       | Type        | Null | Key | Default | Extra |
+-------------+-------------+------+-----+---------+-------+
| glo_tran_id | varchar(30) | NO   |     | NULL    |       |
| dlv_rprt    | varchar(22) | YES  |     | NULL    |       |
+-------------+-------------+------+-----+---------+-------+
    </pre>



    <h6 id="publisher">DESCRIBE publisher</h6>
    <pre>
+---------------------+--------------+------+-----+-------------------+-------+
| Field               | Type         | Null | Key | Default           | Extra |
+---------------------+--------------+------+-----+-------------------+-------+
| message_id          | varchar(100) | NO   | PRI | NULL              |       |
| topic_name          | varchar(20)  | NO   |     | NULL              |       |
| message             | varchar(255) | NO   |     | NULL              |       |
| published_timestamp | timestamp    | NO   |     | CURRENT_TIMESTAMP |       |
| global_txn_id       | varchar(30)  | YES  |     | NULL              |       |
+---------------------+--------------+------+-----+-------------------+-------+
    </pre>



    <h6 id="subscriber">DESCRIBE subscriber</h6>
    <pre>
+---------------------+--------------+------+-----+-------------------+----------------+
| Field               | Type         | Null | Key | Default           | Extra          |
+---------------------+--------------+------+-----+-------------------+----------------+
| id                  | int(11)      | NO   | PRI | NULL              | auto_increment |
| message_id          | varchar(100) | NO   |     | NULL              |                |
| message             | varchar(255) | NO   |     | NULL              |                |
| subscription_name   | varchar(20)  | NO   |     | NULL              |                |
| published_timestamp | timestamp    | NO   |     | CURRENT_TIMESTAMP |                |
| pull_timestamp      | timestamp    | NO   |     | CURRENT_TIMESTAMP |                |
| ack_id              | varchar(255) | YES  |     | NULL              |                |
| global_txn_id       | varchar(30)  | YES  |     | NULL              |                |
+---------------------+--------------+------+-----+-------------------+----------------+
    </pre>

    <h6 id="user_group_details">DESCRIBE user_group_details</h6>
    <pre>
MySQL [msgdb]> DESCRIBE user_group_details;
+------------------+-----------------+------+-----+---------+----------------+
| Field            | Type            | Null | Key | Default | Extra          |
+------------------+-----------------+------+-----+---------+----------------+
| group_id         | int(6) unsigned | NO   | PRI | NULL    | auto_increment |
| group_name       | varchar(50)     | NO   | UNI | NULL    |                |
| group_auth_level | int(6)          | NO   |     | NULL    |                |
+------------------+-----------------+------+-----+---------+----------------+
</pre>

<h3 class="mt-5 text-center">Show Create Table</h3>

<pre id="createTable">

drop table if exists User_Details;
CREATE TABLE `User_Details` (
  `user_id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_email_id` varchar(50) NOT NULL,
  `user_mobile_number` varchar(50) DEFAULT NULL,
  `user_fax_number` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

drop table if exists User_Preferences;
CREATE TABLE `User_Preferences` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `email_prefered` varchar(10) NOT NULL,
  `sms_prefered` varchar(10) NOT NULL,
  `fax_prefered` varchar(10) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `user_id` int(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

drop table if exists activity_logging;
CREATE TABLE `activity_logging` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` varchar(100) NOT NULL,
  `message_data` text NOT NULL,
  `subscription_name` varchar(20) DEFAULT NULL,
  `published_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `glo_tran_id` varchar(30) DEFAULT NULL,
  `topic_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

drop table if exists group_membership;
CREATE TABLE `group_membership` (
  `user_id` int(6) NOT NULL,
  `group_id` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists message_status_cache_db;
CREATE TABLE `message_status_cache_db` (
  `glo_tran_id` varchar(30) NOT NULL,
  `dlv_rprt` varchar(22) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists publisher;
CREATE TABLE `publisher` (
  `message_id` varchar(100) NOT NULL,
  `topic_name` varchar(20) NOT NULL,
  `message` varchar(255) NOT NULL,
  `published_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `global_txn_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists subscriber;
CREATE TABLE `subscriber` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` varchar(100) NOT NULL,
  `message` varchar(255) NOT NULL,
  `subscription_name` varchar(20) NOT NULL,
  `published_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pull_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ack_id` varchar(255) DEFAULT NULL,
  `global_txn_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

drop table if exists user_group_details;
CREATE TABLE `user_group_details` (
  `group_id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) NOT NULL,
  `group_auth_level` int(6) NOT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_name` (`group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


</pre>


  </div>
</body>
</html>