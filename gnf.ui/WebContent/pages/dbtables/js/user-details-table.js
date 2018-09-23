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

      $('#userdetails-datatable').DataTable({
        "ajax" : domain+"/userdetailsdata",
        columnDefs : [ {
          width : '20%',
          targets : 0
        } ],
        fixedColumns : true,

        "columns" : [ {
          "data" : "userId"
        }, {
          "data" : "userName"
        }, {
          "data" : "emailId"
        }, {
          "data" : "mobileNumber"
        }, {
          "data" : function(d) {
              return getCheckbox(d.emailFlag, 'u-'+d.userId+'-email-checkbox');
          }
        }, {
          "data" : function(d) {
              return getCheckbox(d.smsFlag, 'u-'+d.userId+'-sms-checkbox');
          }
        } ],
        "initComplete" : function(settings, json) {
          initCheckBoxEvent();
          $('#loading').detach();
          $('#userdetails-data *').addClass('visible').hide().fadeIn(500);
        },
        mark : true
      });

      function getCheckbox(preference,id) {
        var checked = "checked";
        var flag = preference === 'Yes';
        var html;
        if (!flag) {
          checked = "";
        }
        
        return '<label class="checkbox-container "><input id='+id+' type="checkbox" ' + checked + '><span class="checkmark"></span></label>'
        
       // return '<input id='+id+' type="checkbox" data-toggle="toggle" ' + checked + '>';
      }
   
      function initCheckBoxEvent(){
        
        $('input[type=checkbox]').change(function(data){
          var msg = 'Do you want to update the preference?';
          if(!confirm(msg)){
            this.checked = !this.checked;
            return;
          } 
          var checkboxId = data.target.id;
          var id = checkboxId.split("-")[1];
          var type = checkboxId.split("-")[2];
          var value = data.target.checked;
          var json = {"id":id, "type":type, "value":value};
          updateUserPreference(json);
        });
      }
      
      function updateUserPreference(json){
        
        $.post('/api/updateUserPreference', JSON.stringify(json), function(response){
          if(response!==undefined && response.rowsModified>0){
            //TODO Display Message
          }
          //$('#loading-div, #loading').removeClass('overlay, visible');
         // $('#loading-div, #loading').addClass('invisible');
        });
        
      };

    });
