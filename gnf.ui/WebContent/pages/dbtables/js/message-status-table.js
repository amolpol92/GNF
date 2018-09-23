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

      $('#message-status-datatable').DataTable({
        "ajax" : domain+"/api/getMsgStatusCacheData",
        columnDefs : [ {
          width : '20%',
          targets : 0
        } ],
        fixedColumns : true,

        "columns" : [ 
        {
          "data" : "globalTransactionId"
        }, {
          "data" : "deliveryReport"
        }],
        "initComplete" : function(settings, json) {
          $('#loading').detach();
          $('#message-status-data *').addClass('visible').hide().fadeIn(500);
        },
        mark : true
      });

    });
