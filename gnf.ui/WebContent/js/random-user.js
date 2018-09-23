/* 
 * Used for generating random user information. 
 * This can be used to test DLP response with different data sets.
 * 
 * The random user is generated on triple click on text-area
 * 
 * */

$(document).ready(function(){
	var counter=0;
	$('#random-user-message').click(function(){
		startCounting();
	});
	
	//If counter reaches 3, populateText into text-area
	var startCounting = function(){
		counter++;
		if(counter===3){
			populateText();
			counter=0;
		}
		setTimeout(() => {
			counter=0;
		}, 1000);
	};
	
	var populateText = function(){
	    $('#msg-loading').removeClass('invisible');
		$.get('https://randomuser.me/api/?nat=us', function(response){
		    $('#msg-loading').addClass('invisible');
			var user = response.results[0];
		//	var name = user.name.first+" "+ user.name.last;
			var email = user.email;
			//var phone = user.phone;
			//var dob = user.dob.date;
			//var street = user.location.street;
			
			var message = "This is a sample notification.\n This contains an email address:-"
			+"\nEmail - "+email;
			
			$('#random-user-message').val(message);
			
			
		});
	};
	
});