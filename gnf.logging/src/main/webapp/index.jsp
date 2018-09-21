<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Logging Service</title>

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

#log-severity-div, #monitored-resource-type {
	margin: 50px 0px;
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
          <td><b>/log</b> (Only Logging to Stackdriver)&emsp;OR &emsp;<b>/persistlog</b> (Persist to DB)</td>
        </tr>

        <tr>
          <th>Method</th>
          <td>POST</td>
        </tr>
        <tr>
          <th>Request Body</th>
          <td><pre>
{
    &quot;message&quot;: &quot;GET is not supported. Use POST method. The request body should be in this format.&quot;,
    &quot;severity&quot;: &quot;INFO&quot;,
    &quot;monitoredResource&quot;: {
        &quot;type&quot;: &quot;global&quot;,
        &quot;labels&quot;: {
            &quot;module_id&quot;: &quot;default-service&quot;,
            &quot;project_id&quot;: &quot;project-212003&quot;,
            &quot;version_id&quot;: &quot;1.0.0&quot;
        }
    },
    &quot;logName&quot;: &quot;MyLogger&quot;,
    &quot;labelsMap&quot;: {
        &quot;Global Txn Id&quot;: &quot;gbtxnSampleId12345&quot;
    },
    &quot;sourceLocation&quot;: {
        &quot;fileName&quot;: &quot;LoggingServlet.java&quot;,
        &quot;lineNumber&quot;: 20,
        &quot;function&quot;: &quot;app.service.logging.servlet.LoggingServlet.doGet(...)&quot;
    }
}

			</pre></td>
        </tr>

      </table>

    </div>

    <div id="log-severity-div">

      <h2>Log Severity</h2>

      <table class="constants responsive"
        id="google.logging.type.LogSeverity.ENUM_VALUES-table">

        <tbody>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.DEFAULT">
            <td><code class="apitype">
                <span>DEFAULT</span>
              </code></td>
            <td>(0) The log entry has no assigned severity level.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.DEBUG">
            <td><code class="apitype">
                <span>DEBUG</span>
              </code></td>
            <td>(100) Debug or trace information.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.INFO">
            <td><code class="apitype">
                <span>INFO</span>
              </code></td>
            <td>(200) Routine information, such as ongoing status
              or performance.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.NOTICE">
            <td><code class="apitype">
                <span>NOTICE</span>
              </code></td>
            <td>(300) Normal but significant events, such as start
              up, shut down, or a configuration change.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.WARNING">
            <td><code class="apitype">
                <span>WARNING</span>
              </code></td>
            <td>(400) Warning events might cause problems.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.ERROR">
            <td><code class="apitype">
                <span>ERROR</span>
              </code></td>
            <td>(500) Error events are likely to cause problems.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.CRITICAL">
            <td><code class="apitype">
                <span>CRITICAL</span>
              </code></td>
            <td>(600) Critical events cause more severe problems or
              outages.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.ALERT">
            <td><code class="apitype">
                <span>ALERT</span>
              </code></td>
            <td>(700) A person must take an action immediately.</td>
          </tr>
          <tr
            id="google.logging.type.LogSeverity.ENUM_VALUES.google.logging.type.LogSeverity.EMERGENCY">
            <td><code class="apitype">
                <span>EMERGENCY</span>
              </code></td>
            <td>(800) One or more systems are unusable.</td>
          </tr>
        </tbody>
      </table>
      <a style="font-size: .9em; margin-left: 50px;"
        href="https://cloud.google.com/service-infrastructure/docs/service-control/reference/rpc/google.logging.type"
        target="_blank"> Google Doc on LogSeverity</a>
    </div>


    <div id="monitored-resource-type">

      <h2>Monitored Resource</h2>

      <table>

        <tr>
          <td>global</td>
          <td>gae_app</td>
          <td>pubsub_topic</td>
          <td>pubsub_subscription</td>
          <td>logging_sink</td>
          <td>gcs_bucket</td>
        </tr>

      </table>


      <a href="https://cloud.google.com/monitoring/api/resources" target="_blank">
        List of all monitored resource</a>

    </div>



  </div>




</body>
</html>