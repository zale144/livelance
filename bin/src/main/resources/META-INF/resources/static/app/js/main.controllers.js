var hire_me = angular.module('hire_me.controllers', []);

// DROPDOWN
hire_me.controller('NavbarController', function ($scope,
		$location) {

	$scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };
	
	  $scope.toggleDropdown = function() {
	    $scope.drop = !$scope.drop;
	    if($scope.drop) {
	    	$scope.open = '';	
	    } else {
	    	$scope.open = 'open';
	    }
	    
	  };
	});

// MANAGEMENT
hire_me.factory('Management', function($resource) {
	return $resource('/management');
}).controller('ManagementController', function($scope, Management) {
	$scope.management = Management.get();
});



// CATEGORY
hire_me.factory('Category', function($resource) {
	return $resource('api/categories/');

}).controller('CategoryController',	function($scope, Category, $routeParams) {
	var url = function() {
		return {id : $routeParams.id || ''};
	}

	var update = function() {
		console.log(url());
		$scope.categories = Category.query(url(), function() {
			angular.forEach($scope.categories, function(category) {
				category.services = [];
				category.services = category.resource("services").query(null, function(data) {
				});
			});
		});
	};

	$scope.update = update;

	$scope.add = function() {
		var category = new Category();
		category.name = $scope.name;
		category.$save(url(), function() {
			$scope.name = "";
			update();
		});
	};

	update();
});

// ACCOUNT
hire_me.factory('Account', function($resource) {
	return $resource('api/accounts/:id', {
		id : '@id'}, {
			  'update': { method:'PUT' 
			}
	});
})

hire_me.controller('AccountController',	function($scope, Account, sessionService,
		 $location, $routeParams, $resource, fileUpload, Profile, Deal) {

	$scope.show = false;
	$scope.self =false;
	$scope.edit = false;
	
	var path = $routeParams.id;
	var user = localStorage.getItem("session");
	if(path === user) {
		$scope.self = true;
	}
	
	var url = function() {
		return {id : $routeParams.id};
	}

	var update = function() {
		console.log(url());
		if (url().id) {
			$scope.account = Account.get(url(), function(data, headers) {
				$scope.profile = $resource(
					$scope.account._links.profile.href).get(function() {
						$scope.profile.deals = $resource(
							$scope.profile._links.deals.href).query(function() {
								angular.forEach($scope.profile.deals, function(deal) {
									deal.service = deal.resource("service").get();
									deal.locations = deal.resource("locations").query();
									deal.ratings = deal.resource("ratings").query();
								});
						});
					});
				}, function(data) {
					if(data.status === 403) {
						sessionService.logout();
						$location.path('/home');
						alert(data.status + " - you don't have access to this page");
					}
				});
		} else {
			$scope.accounts = Account.query(null, function() {

				angular.forEach($scope.accounts, function(account) {
					account.profile = account.resource("profile").get();
				});
				angular.forEach($scope.accounts, function(account) {
					account.roles = account.resource("roles").query();
				});
			}, function(data) {
				if(data.status === 401 || data.status === 403) {
					sessionService.logout();
					$location.path('/home');
					alert(data.status + ' - you have to be logged in as Admin in order to access this page!');
				}
			});
		}
	};
	
	 $scope.uploadFile = function(){
	        var file = $scope.myFile;
	        console.log('file is ' );
	        console.dir(file);
	        var uploadUrl = "/api/files/images/";
	        var link = uploadUrl + file.name;
	        $scope.profile.pictureLink = link;
	        $scope.profile.account = $scope.account;
	        fileUpload.uploadFileToUrl(file, uploadUrl)
	        .success(function(data, status, headers){
	        	Profile.update({id:$scope.profile.rid}, $scope.profile, function() {
	        		update();
	        	});
	        })
	        .error(function(){
	        });
	    };
	    

	$scope.add = function() {
		var account = new Account();
		account.username = $scope.account.username;
		account.$save(url(), function() {
		update();
		});
	};
	
	$scope.remove = function(account) {
		Account.delete({id:account.username}, function() {
			var index = $scope.accounts.indexOf(account);
			$scope.accounts.splice(index, 1);
		}); 	
	}
	
	$scope.editAccount = function() {
		$scope.edit = true;
	}
	
	$scope.saveAccount = function() {
		$scope.profile.account = $scope.account;
		Profile.update({id:$scope.profile.rid}, $scope.profile, function(data) {
			update();
			$scope.edit = false;
		});	
	}
	
	$scope.newDeal = function() {
		$scope.show = true;
		$scope.deal = {};
		$scope.deal.locations = [];
		pickLocation();
	}

	$scope.addDeal = function() {
		var deal = {};
		deal.service = $scope.deal.service;
		deal.description = $scope.deal.description;
		deal.serviceCost = $scope.deal.serviceCost;
		deal.locations = $scope.deal.locations;
		$scope.show = false;
		$scope.deal.profile = { rid : $scope.profile.rid};
		Deal.save($scope.deal, function(data) {
			$scope.deal.rid = data.rid;
			$scope.profile.deals.push($scope.deal);
		});
	}
	
	$scope.addLocation = function(location) {
		var loc = {};
//		if($scope.country) {
//			location.country = $scope.country;	
//			alert($scope.country);
//		}
		loc.country = location.country;
		loc.city = location.city;
		loc.street = location.street;
		loc.number = location.number;
		loc.latitude = $scope.latitude;
		loc.longitude = $scope.longitude;
		$scope.deal.locations.push(loc);
	}
	
	$scope.removeLocation = function(location) {
		var index = $scope.deal.locations.indexOf(location);
		$scope.deal.locations.splice(index, 1);
	}
	
	$scope.removeDeal = function(deal) {
		Deal.delete({id:deal.rid}, function() {
			var index = $scope.profile.deals.indexOf(deal);
			$scope.profile.deals.splice(index, 1);
		});
	}

	$scope.cancel = function() {
		$scope.deal = {};
		$scope.show = false;
	}
	
	// FORMAT DATE
	$scope.formatDate = function(date) {
		var dateOut = undefined;
		if (date) {
			dateOut = new Date(date);
		}
		return dateOut;
	};
	
	
	//PICK LOCATION FROM MAP
	
	var markersArray = [];
   	var geocoder = new google.maps.Geocoder;
   	var infowindow = new google.maps.InfoWindow;
   	
	var latitude = 0;
  	var longitude = 0;
	
  	// GET USER LOCATION
	var pickLocation = function() {
		navigator.geolocation.getCurrentPosition(success, error, geo_options);
  	 
	    function success(position) {
	   	 latitude = position.coords.latitude;
	   	 longitude = position.coords.longitude;
	
	   	 $scope.loadMap();
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
	
	$scope.loc = {};
	
//	 $scope.$watch('loc', function() {
//	        alert('hey, myVar has changed!');
//	    });
	
	// LOAD PICKMAP
	 $scope.loadMap = function() {
	    	var mapOptions = {
	                zoom: 14,
	                center: new google.maps.LatLng(latitude,longitude),
	                mapTypeId: google.maps.MapTypeId.ROADMAP,
	                scrollwheel: true,
	            }
	       	 
	       	 	
	            $scope.map = new google.maps.Map(document.getElementById('pickmap'), mapOptions);

	            $scope.openInfoWindow = function(e, selectedMarker){
	                e.preventDefault();
	                google.maps.event.trigger(selectedMarker, 'click');
	            }			 
	     
	     
	        	function removeMarkers() {
	        		for (var i = 0; i < markersArray.length; i++ ) {
	        		    markersArray[i].setMap(null);
	        		  }
	        		  markersArray.length = 0;
	        	}
	        
	        google.maps.event.addListener($scope.map, 'click', function(event) {
	        	removeMarkers();
	        	$scope.latitude = event.latLng.lat();
	        	$scope.longitude = event.latLng.lng();
//	            alert("Lat=" + $scope.latitude + "; Lng=" + $scope.longitude);
	            geocodeLatLng(geocoder, $scope.map, infowindow);
	          });
	    }
	    
	    
	 //GET ADDRESS FROM LATLONG
	    function geocodeLatLng(geocoder, map, infowindow) {
	    	  var latlng = {lat: parseFloat($scope.latitude), lng: parseFloat($scope.longitude)};
	    	  geocoder.geocode({'location': latlng}, function(results, status) {
	    	    if (status === 'OK') {
	    	      if (results[1]) {
	    	        map.setZoom(11);
	    	        var marker = new google.maps.Marker({
	    	          position: latlng,
	    	          map: map
	    	        });
	    	        markersArray.push(marker);
	    	    	map.panTo(latlng);
	    	    	
	    	    	var fullAddress = results[0].formatted_address.split(', ',3);
	    	    	var city = fullAddress[1].replace(/[0-9]/g, '');
	    	    	
	    	    	$scope.loc.country = fullAddress[2];
	    	    	$scope.loc.city = city;
	    	    	$scope.loc.street = fullAddress[0];
	    	    	
	    	    	update();
	    	        infowindow.setContent(results[0].formatted_address);
	    	        infowindow.open(map, marker);
	    	      } else {
	    	        alert('No results found');
	    	      }
	    	    } else {
	    	      alert('Geocoder failed due to: ' + status);
	    	    }
	    	  });
	    }
	
	update();
});

//FILE UPLOAD
hire_me.directive('fileModel', ['$parse', function ($parse) {
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

hire_me.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        return $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
       
    }
}]);




//LOGIN SESSION
hire_me.service('sessionService', function($http, $resource, Account) {

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

// INDEX
hire_me.controller('IndexController', function($scope, $location, sessionService, $rootScope) {
	
	$scope.username = localStorage.getItem("session");
	$scope.role = false;
	$scope.logged = false;

	$scope.login = function() {
		sessionService.login($scope.account).success(
				function(data, status, headers, config) {
					var username = headers("username");
					localStorage.setItem("session", username);
					sessionService.setRoles();
					$scope.isLoggedIn();
					$scope.isAdmin();
					$location.path('/accounts/view/' + username);
				}).error(function() {
			alert("Error logging in user");
			$location.path('/login');
		});
	}

	$scope.logout = function() {
		sessionService.logout().success(function() {
			$location.path('/home');
		}).error(function() {
			alert('error logging out');
		})
	};

	$scope.isLoggedIn = function() {
		return localStorage.getItem("session");
	}
	
	$scope.isAdmin = function() {
		var roles = localStorage.getItem("roles");
		if(roles) {
			return roles.indexOf("ADMIN") > -1;			
		}
	}
	
	$scope.isSysAdmin = function() {
		var roles = localStorage.getItem("roles");
		if(roles) {
			return roles.indexOf("SYSADMIN") > -1;			
		}
	}
	
});

// REGISTRATION
 hire_me.controller("RegistrationController", function($scope, 
		 sessionService, $location, Account, $resource) {
 
	 $scope.register = function() {
		 Account.save($scope.account, function(returnedData) {
			 console.log(returnedData);
			 
			 sessionService.login($scope.account).then(function() {
				 localStorage.setItem("session", $scope.account.username);
				 $location.path('/accounts/view/' + $scope.account.username);
			 });
		 },
		 function() {
			 alert("Error registering user");
		 });
	 };
	 
 })

// DEAL
hire_me.factory('Deal', function($resource) {
	return $resource('api/deals/:id', {
		id : '@id'
	});

}).controller('DealController', function($scope, Deal, $routeParams, $rootScope,
		$location) {
	var url = function() {
		return {
			id : $routeParams.id || ''
		};
	}
	
	$scope.goToDeals = function(query) {
		$rootScope.query = query;
		$rootScope.updateDeals();
	}

	$rootScope.updateDeals = function() {
		console.log(url());
		$scope.deals = Deal.query($rootScope.query, function() {
			angular.forEach($scope.deals, function(deal) {
				deal.locations = deal.resource("locations").query();
				deal.profile = deal.resource("profile").get();
				deal.service = deal.resource("service").get();
				deal.ratings = deal.resource("ratings").query();
				$rootScope.geoFindMe($scope.deals);
				$location.path("/deals");
			})
		});
	};

	$scope.add = function() {
		var deal = new Deal();
		deal = $scope.deal;
		// ...

		deal.$save(url(), function() {
			update();
		});
	};

//	$rootScope.updateDeals($rootScope.query);
});

// PROFILE
hire_me.factory('Profile', function($resource) {
	return $resource('api/profiles/:id', {
		id : '@id'}, {
			  'update': { method:'PUT' 
		}
	});

}).controller('ProfileController', function($resource, $scope, Profile, $routeParams) {
	var url = function() {
		return {
			id : $routeParams.id || ''
		};
	}

	$scope.update = function() {
		console.log(url());
		if(url().id) {
			var profile = Profile.get(url(), function(data) {
				$scope.profile = data;
				$scope.profile.deals = $resource(
						$scope.profile._links.deals.href).query(function() {
							angular.forEach($scope.profile.deals, function(deal) {
								deal.service = deal.resource("service").get();
								deal.locations = deal.resource("locations").query();
								deal.ratings = deal.resource("ratings").query();
							});
					});
			});
		} else {
			$scope.profiles = Profile.query(url(), function() {
				angular.forEach($scope.profiles, function(profile) {
					profile.deals = profile.resource("deals").query(function() {
						angular.forEach(profile.deals, function(deal) {
							deal.service = deal.resource("service").get();
						});
					});
				});
			});
		}
	};
	
	// FORMAT DATE
	$scope.formatDate = function(date) {
		var dateOut = undefined;
		if (date) {
			dateOut = new Date(date);
		}
		return dateOut;
	};
	$scope.update();
});



//RATING 

hire_me.factory('Rating', function($resource) {
	return $resource('api/deals/:deal/ratings/', { deal : '@deal'});
})

hire_me.controller('RatingController', function (Rating, $scope) {
	$scope.rate = 0;
	$scope.max = 5;
	$scope.isReadonly = false;

	$scope.hoveringOver = function(value) {
		$scope.overStar = value;
		$scope.percent = 100 * (value / $scope.max);
  };
  
  $scope.submitComment = function(deal, customerName, rate, comment) {
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
  
//  $scope.clearFields = function() {    // NE RADI!!!
//	  $scope.customerName = '';
//	  $scope.comment = '';
//	  $scope.rate = 3;
//  }

});



//GEOLOCATION

hire_me.controller('GeoLocation', function($scope, $rootScope) {
	var cities = [];
	var loadDealLocations = function(deals) {
		cities = [];
		for(d in deals) {
			if(deals[d].rid) {
				var descr = deals[d].description;
				var picLink = deals[d].profile.pictureLink;
				var profileLink = '/#/profiles/' + deals[d].profile.rid;
				var averageRating = deals[d].averageRating;
				var serviceCost = deals[d].serviceCost;
				var la = deals[d].locations[0].latitude;
				var lo = deals[d].locations[0].longitude;
				cities.push( {
					   city:'',
				       desc:descr,
				       pic:picLink,
					   link:profileLink,
					   avgRat:averageRating,
					   price: serviceCost,
					   lat:la,
					   long:lo
					})
				}
			}
		}

	
	$rootScope.geoFindMe = function(deals) {
	     navigator.geolocation.getCurrentPosition(success, error, geo_options);
	 
	     function success(position) {
	    	 var latitude = position.coords.latitude;
	    	 var longitude = position.coords.longitude;
			 var altitude = position.coords.altitude;
			 var accuracy = position.coords.accuracy;
	 
//---------------------------
			 loadDealLocations(deals);
			 
			 var mapOptions = {
			            zoom: 14,
			            center: new google.maps.LatLng(latitude,longitude),
			            mapTypeId: google.maps.MapTypeId.ROADMAP,
			            scrollwheel: false,
			        }

			        $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);

			        $scope.markers = [];
			        
			        var infoWindow = new google.maps.InfoWindow();
			        
			        var createMarker = function (info){
			            
			            var marker = new google.maps.Marker({
			                map: $scope.map,
			                position: new google.maps.LatLng(info.lat, info.long),
			                title: info.desc,
			                icon: info.icon
			            });
			            marker.content = '<div class="infoWindowContent"></div>';
			            
			            google.maps.event.addListener(marker, 'click', function(){
			                infoWindow.setContent('<a href="'+info.link+'"><h4>' + marker.title + '</h4></a>'
			                		+'<a href="'+info.link+'"><img src="'+info.pic+'" style="width:45px; height:45px;"></a></br>'
			                		+ 'Rating: <strong>'+info.avgRat + '</strong>/5</br>'
			                		+ 'Price: <strong>' +info.price + '</strong> RSD'
			                		+ marker.content);
			                infoWindow.open($scope.map, marker);
			            });
			            
			       
			            
			            $scope.markers.push(marker);
			            
			        }  
			        
			        createMarker({
		                  city : 'Me',
		                  pic : 'https://i.ytimg.com/vi/J5eiDVPu8v4/maxresdefault.jpg',
		                  desc: '',
		                  lat : latitude,
		                  long : longitude,
		                  icon: 'https://maps.google.com/mapfiles/ms/icons/green-dot.png'
		              });
			        
			        for (i = 0; i < cities.length; i++){
			            createMarker(cities[i]);
			        }

			        $scope.openInfoWindow = function(e, selectedMarker){
			            e.preventDefault();
			            google.maps.event.trigger(selectedMarker, 'click');
			        }			 
			 
			 
			 //--------------------------------------
			 
			 
//	 alert('I am here! lat:' + latitude +' and long : ' +longitude );
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

//	$scope.geoFindMe();
	
	//-----------------------------------------
	
	
});
