var livelance = angular.module('livelance.services', []);

 //SESSION SERVICE
livelance.service('sessionService', function($http, $resource, Account) {

	this.login = function(account) {
		return $http.post('/login', "username=" + account.username
				+ "&password=" + account.password, {
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		});
	};

	this.logout = function() {
		localStorage.removeItem("session");
		localStorage.removeItem("roles");
		return $http.get('/logout');
	};

	this.isLoggedIn = function() {
		return localStorage.getItem("session") !== null;
	};

	this.setRoles = function(callback) {
		var username = localStorage.getItem("session");
		var rolesSession = [];
		if(username) {
			var account = Account.get({id : username}).$promise.then(function(data) {
				roles = $resource(data._links.roles.href).query().$promise.then(function(rol) {
					for (var i=0; i < rol.length; i++) {
						rolesSession.push(rol[i].code);
					}
					localStorage.setItem("roles", rolesSession);
				});
			});
		}
		
	};
});

livelance.service('dealsData', function() {
	
	this.ret = []
	
	this.addDeals = function(dealsD) {
		for(r in this.ret) {
			var l = this.ret.length;
			this.ret.splice(0, l);
		}
		for(d in dealsD) {
			if(dealsD[d].rid) {
				this.ret.push(dealsD[d]);	
			}
		}
	}
});

// DEAL SERVICE
livelance.factory('Deal', function($resource) {
	return $resource('/api/deals/:id', {
		id : '@id'
	});

}).service('dealService', function(Deal) {

	this.qry = {};
	var q;
	var clbck = function() {};
	
	this.getDeals = function(query, callback) {
		update(query, callback);
		this.qry = query;
		q = query;
		clbck = callback;
	} 

	var update = function(query, callback) {
		var updateFlag = true;
		geoFindMe(function(pos) {
			query.latitude = pos[0];
			query.longitude = pos[1];
			
			if(zoom) {
				if(zoom < 8) {
					zoom = 8;
					updateFlag = false;
				}
				query.radius = (Math.pow(2, (21-zoom)) * 1.24 / 10000);
			} else {
				query.radius = 0.031744;
			}
			
			if(updateFlag) {
				deleteMarkers();
				var deals = Deal.query(query, function(data) {
					
					angular.forEach(deals, function(deal) {
							deal.locations = deal.resource("locations").query(function() {
								deal.profile = deal.resource("profile").get(function() {
									deal.service = deal.resource("service").get(function() {
										deal.ratings = deal.resource("ratings").query(function() {
											loadDealLocations(deal);
										});
									});
								});
							});
						})
						callback(deals);
						addMe();
					});
				}
			});
	}
	
	// UPDATE MARKERS
	 var loadDealLocations = function(deal) {
		var pos = localStorage.getItem("position").split(',',2);
		var latitude = pos[0];
		var longitude = pos[1];
	 	if(deal.rid) {
	 		var descr = deal.description;
	 		var picLink = deal.profile.pictureLink;
	 		var profileLink = './#/profiles/' + deal.profile.rid;
	 		var averageRating = deal.averageRating;
	 		var serviceCost = deal.serviceCost;
	 		var distance;
	 		var la = undefined;
	 		var lo = undefined;
	 		for(l in deal.locations) {
	 			la = deal.locations[l].latitude;
				lo = deal.locations[l].longitude;
				deal.locations[l].distance = getDistance(deal.locations[l].latitude, 
						deal.locations[l].longitude, pos[0], pos[1]);
				deal.shorterDistance = min(deal.locations);
				distance = deal.locations[l].distance;
				createMarker( {
				   city:'',
			       desc: descr,
			       pic: picLink,
				   link: profileLink,
				   avgRat: averageRating,
				   price: serviceCost,
				   dist: distance,
				   lat:la,
				   long:lo
				});
			}
	 	}
	 	
	 }
	 
	var min = function(arr) {
		var min = Number.MAX_VALUE;
		for (var i = 0; i < arr.length; i++) {
			if(arr[i].distance < min) {
				min = arr[i].distance;
			}
		}
		return min;
	}

	var zoom;
	var map;
	//GEOLOCATION
	var geoFindMe = function(callback) {
		var pos = [];
		var located = localStorage.getItem("position");
		if(!located) {
			navigator.geolocation.getCurrentPosition(success, error, geo_options);
	    	function success(position) {
		    	var latitude = position.coords.latitude;
		    	var longitude = position.coords.longitude;
				var altitude = position.coords.altitude;
				var accuracy = position.coords.accuracy;
				alert('Successfully located: \nlatitude: ' + latitude + ' ' + 'Longitude: ' + longitude +
						'\n\nIf it is not your location, click "Reset my location" or refresh the page.\n\n' +
						'Otherwise, you can drag the green marker (you)\n' + 
						'on the map to your actual location.');
				pos.push(latitude);
				pos.push(longitude);
				localStorage.setItem("position", pos);
				if(callback) {
					callback(pos);
				}
				window.location.reload();
	    	}
	 
			function error(error) {
				alert("Unable to retrieve your location due to " + error.code + " : " + error.message +
						'\nPlease manually set your current location by dragging the green marker on the map.');
				localStorage.setItem("position", '45.254324,19.829870');
				window.location.reload();
			};
		 
			var geo_options = {
				enableHighAccuracy: true,
				maximumAge : 30000,
				timeout : 27000
			};
		} else {
			if(callback) {
				pos = located.split(',',2);
				callback(pos);
			}
		}
		
	}
	
	function getDistance(lat1, lon1, lat2, lon2) {
		 var R = 6371; // Radius of the earth in km
		  var dLat = (lat2 - lat1) * Math.PI / 180;  // deg2rad below
		  var dLon = (lon2 - lon1) * Math.PI / 180;
		  var a = 
		     0.5 - Math.cos(dLat)/2 + 
		     Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * 
		     (1 - Math.cos(dLon))/2;

		  return (R * 2000 * Math.asin(Math.sqrt(a)) | 0);
	}

	 var infoWindow;
	 var markers = [];
	 var createMarker = function (info) {
         
         var marker = new google.maps.Marker({
             map:  map,
//             animation: google.maps.Animation.DROP,
             position: new google.maps.LatLng(info.lat, info.long),
             title: info.desc,
             icon: info.icon,
             city: info.city
         });
//         marker.addListener('click', toggleBounce);
         marker.content = '<div class="infoWindowContent"></div>';
         
         google.maps.event.addListener(marker, 'click', function(){
             infoWindow.setContent('<a style="color:#2F4F4F;" href="'+info.link+'"><h4>' + marker.title + '</h4></a>'
             		+'<a href="'+info.link+'"><img src="'+info.pic+'" style="width:45px; height:45px;"></a></br>'
             		+ 'Distance: <strong>'+info.dist + '</strong> m</br>'
             		+ 'Price: <strong>' +info.price + '</strong> RSD</br>'
             		+ 'Rating: <strong>'+info.avgRat + '</strong>/5</br>'
             		+ marker.content);
             infoWindow.open(map, marker);
         });
         
         markers.push(marker);
         
         function toggleBounce() {
        	  if (marker.getAnimation() !== null) {
        	    marker.setAnimation(null);
        	  } else {
        	    marker.setAnimation(google.maps.Animation.BOUNCE);
        	  }
        	}
     }  
	 
	 var me;
	 var addMe = function() {
		 if(me) {
			 me.setMap(null);
			 me = null;
		 }
		 
		 if(!me && localStorage.getItem("position")) {
			 
			 var pos = localStorage.getItem("position").split(',',2);
			 var latitude = pos[0];
			 var longitude = pos[1]; 
			 var marker = new google.maps.Marker({
				map:  map,
	            draggable: true,
//	            animation: google.maps.Animation.DROP,
	            position: new google.maps.LatLng(latitude, longitude),
	            title:'Me',
	            icon: 'https://maps.google.com/mapfiles/ms/icons/green-dot.png',
	            city: 'Me'
	        });
			map.setCenter(new google.maps.LatLng(latitude, longitude));
			me = marker;
			 marker.addListener('drag', function() {
				 
				});
			 
			 marker.addListener('dragend', function() {
					var lat = marker.getPosition().lat();
					var lng = marker.getPosition().lng();
					localStorage.setItem("position", lat+','+lng);
					update(q, clbck);
				}); 
		 }
		 var count = 0;
		
     }
	 
	 function setMapOnAll(map) {
         for (var i = 0; i < markers.length; i++) {
           if(markers[i].city !== 'Me') {
        	   markers[i].setMap(map);
           }
         }
     }
     
     function clearMarkers() {
         setMapOnAll(null);
     }
     
     var deleteMarkers = function() {
         clearMarkers();
         markers = [];
     }
     
     function CenterControl(controlDiv, map) {

         // Set CSS for the control border.
         var controlUI = document.createElement('div');
         controlUI.style.backgroundColor = '#fff';
         controlUI.style.border = '2px solid #fff';
         controlUI.style.borderRadius = '3px';
         controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
         controlUI.style.cursor = 'pointer';
         controlUI.style.marginBottom = '22px';
         controlUI.style.textAlign = 'center';
         controlUI.title = 'Click to reset your location';
         controlDiv.appendChild(controlUI);

         // Set CSS for the control interior.
         var controlText = document.createElement('div');
         controlText.style.color = 'rgb(25,25,25)';
         controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
         controlText.style.fontSize = '16px';
         controlText.style.lineHeight = '38px';
         controlText.style.paddingLeft = '5px';
         controlText.style.paddingRight = '5px';
         controlText.innerHTML = 'Reset my location';
         controlUI.appendChild(controlText);

         // Setup the click event listeners: simply set the map to Chicago.
         controlUI.addEventListener('click', function() {
        	localStorage.removeItem('position');
     		update(q, clbck);
         });

       }
     
	// LOAD MAP
	this.loadMap = function() {
		if(localStorage.getItem("position")) {
			var pos = localStorage.getItem("position").split(',',2);
			var latitude = pos[0];
			var longitude = pos[1];
	
			infoWindow = new google.maps.InfoWindow();
		 	var mapOptions = {
				            zoom: 13,
				            zoomControlOptions: {
				                position: google.maps.ControlPosition.RIGHT_TOP
				            },
				            streetViewControl: false,
				            minZoom: 8,
				            center: new google.maps.LatLng(latitude, longitude),
				            mapTypeId: google.maps.MapTypeId.ROADMAP,
				            scrollwheel: false,
				        }

		 	map = new google.maps.Map(document.getElementById('map'), mapOptions);
		 	
		 	 // Create the DIV to hold the control and call the CenterControl()
	         // constructor passing in this DIV.
	         var centerControlDiv = document.createElement('div');
	         var centerControl = new CenterControl(centerControlDiv, map);

	         
	         centerControlDiv.index = 1;
	         map.controls[google.maps.ControlPosition.TOP_CENTER].push(centerControlDiv);
		 	
				    map.addListener('zoom_changed', function() {
				 		console.log( map.getZoom());
				 		zoom =  map.getZoom();
				 		update(q, clbck);
				 	});
			}
	}
		
	this.add = function(deal) {
		var deal = new Deal();

		return deal.$save(url());
	};
	
});

//ACCOUNT
livelance.factory('Account', function($resource) {
	return $resource('/api/accounts/:id', {
		id : '@id'}, {
			  'update': { method:'PUT' 
			}
	});
});

//PROFILE
livelance.factory('Profile', function($resource) {
	return $resource('/api/profiles/:id', {
		id : '@id'}, {
			  'update': { method:'PUT' 
		}
	});
	
}).service('profileService', function(Profile, $routeParams, $resource) {
	
	var url = function() {
		return {
			id : $routeParams.id || ''
		};
	}
	
	this.update = function(callback) {
		var profileData;
		if(url().id) {
			var profile = Profile.get(url(), function(data) {
				profileData = data;
				profileData.deals = $resource(
						profileData._links.deals.href).query(function() {
							angular.forEach(profileData.deals, function(deal) {
								deal.service = deal.resource("service").get();
								deal.locations = deal.resource("locations").query();
								deal.ratings = deal.resource("ratings").query();
								callback(profileData);
							});
					});
			});
		}
	}
});

//MANAGEMENT
livelance.factory('Management', function($resource) {
	return $resource('/management');
});

//RATING
livelance.factory('Rating', function($resource) {
	return $resource('/api/deals/:deal/ratings/', { deal : '@deal'});
	
}).service('ratingService', function(Rating) {
	this.saveComment = function(deal, customerName, rate, comment) {
		var rating = {}; 
		rating.customerName = customerName;	
		if(!customerName) {
			rating.customerName = 'Anoniman';
		}
		rating.comment = comment;
		rating.rating = rate;	
		if(rate == 0) {
			alert('You must provide a rating!');
		} else {
			Rating.save({deal : deal.rid}, rating, function() {
				deal.ratings.push(rating);
			});
		}
	}
})

//CATEGORY
livelance.factory('Category', function($resource) {
	return $resource('/api/categories/');
});

//FILE UPLOAD
livelance.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

livelance.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        return $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
       
    }
}]);