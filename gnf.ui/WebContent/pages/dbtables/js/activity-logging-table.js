/*
 * Hits the GET URL. The JSON response is used for populating 
 * data into table on JSP page.
 * 
 * Also, handles the refresh icon show/hide
 * 
 * */

$(document).ready(
    function() {
    	var domain = 'https://possible-haven-212003.appspot.com';
      console.log('js loaded');
      $('#loading-div, #loading').addClass('visible').hide().delay(500).fadeIn(
          300);

      $('#logging-datatable').DataTable({
        "ajax" : domain+"/api/getLoggingData",
        columnDefs : [ {
          width : '20%',
          targets : 0
        } ],
        fixedColumns : true,
        "order" : [ [ 5, 'desc' ] ],

        "columns" : [ {
          "data" : "autoIncrId"
        },
        {
          "data" : "messageId"
        }, {
          "data" : function(d) {
            var psMsgData = JSON.stringify(d);
            var message = JSON.parse(psMsgData).messageData.replace(/\n/g,"<br>");
        
            return fixWidth(message, 300);
          }
        },
        {
          
        	  "data" : "topicName"
        }, {
        	"data" : "subscriptionName"
        }, {
          "data" : "publishedTimestamp"
        }, {
          "data" : "globalTransactionId"
        }],
        "initComplete" : function(settings, json) {
          $('#loading').detach();
          $('#logging-data *').addClass('visible').hide().fadeIn(500);
        },
        mark : true
      });
      
      function fixWidth(columnData, width){
        var result = '<span style="display:block;width:'
          +width+'px; word-wrap:break-word;">'
          + columnData+ '</span>';
        return result;
      }

    });
