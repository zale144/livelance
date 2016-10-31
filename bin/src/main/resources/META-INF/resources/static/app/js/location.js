//		function initialize(latitude,longitude) {
//  			var mapProp = {
//    			center:new google.maps.LatLng(latitude,longitude),
//    			zoom:10,
//    			mapTypeId:google.maps.MapTypeId.ROADMAP
// 			};
//  			var map=new google.maps.Map(document.getElementById("googleMap"), mapProp);
//
//  			var marker=new google.maps.Marker({
//  			  position:new google.maps.LatLng(latitude,longitude),
//  			  });
//
//  			marker.setMap(map);
//
//  			var infowindow = new google.maps.InfoWindow({
//  			  content:"<a href='http://localhost:9001/#/profiles/1'>Jovan Jovanovic</a>" 
//  			  });
//
//  			infowindow.open(map,marker);
//			}
//			google.maps.event.addDomListener(window, 'load', initialize);
//			
//	



			
			function geoFindMe(){
			     navigator.geolocation.getCurrentPosition(success, error, geo_options);
			 
			     function success(position) {
			    	 initialize(position.coords.latitude, position.coords.longitude)
			    	 var latitude = position.coords.latitude;
					 var longitude = position.coords.longitude;
					 var altitude = position.coords.altitude;
					 var accuracy = position.coords.accuracy;
			 
			 //do something with above position thing e.g. below
			 alert('I am here! lat:' + latitude +' and long : ' +longitude );
			}
			 
			function error(error) {
			 alert("Unable to retrieve your location due to "+error.code + " : " + error.message);
			 };
			 
			var geo_options = {
			 enableHighAccuracy: true,
			 maximumAge : 30000,
			 timeout : 27000
			 };
			}