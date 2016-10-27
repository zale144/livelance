var livelance = angular.module('livelance.controllers', []);

//DEALS
livelance.controller('DealController', function($scope, dealService, dealsData,
		$location, $window) {
	$scope.deals = dealsData.ret;
	
	var forceSSL = function () {
	    if ($location.protocol() !== 'https') {
	        $window.location.href = $location.absUrl().replace('http', 'https');
	    }
	};
	
	forceSSL();
	
	var updateDeals = function() {
		dealService.getDeals({}, function(data) {
			dealsData.addDeals(data);
		});
	}
	
	if(JSON.stringify(dealService.qry) === JSON.stringify({})) {
		updateDeals();
	}
	
	// FORMAT DATE
	$scope.formatDate = function(date) {
		var dateOut = undefined;
		if (date) {
			dateOut = new Date(date);
		}
		return dateOut;
	};
});

// INDEX
livelance.controller('IndexController', function($scope, $location, sessionService,
		dealService, dealsData) {
	
	$scope.loadMap = function() {
		dealService.loadMap();
	}
	
	$scope.updateDeals = function(query) {	
		$location.path("/deals");
		dealService.getDeals(query, function(data) {
			dealsData.addDeals(data);
		});
	};
	
	$scope.username = localStorage.getItem("session");
	$scope.role = false;
	$scope.logged = false;
	
	$scope.login = function() {
		sessionService.login($scope.account).success(function(data, status, headers, config) {
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

// ACCOUNT
livelance.controller('AccountController',	function($scope, Account, sessionService,
		 $location, $routeParams, $resource, fileUpload, Profile, Deal) {

	$scope.update = update;
	$scope.show = false;
	$scope.self =false;
	$scope.edit = false;
	$scope.alert = false;
	var account = {};
	
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
				$scope.profile = $resource($scope.account._links.profile.href).get(function() {
					$scope.profile.deals = $resource($scope.profile._links.deals.href).query(function() {
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
		var uploadUrl = "/app/api/files/images/";
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
	
	$scope.checkRemoveAccount = function(acc) {
		account = acc;
		$scope.alert = true;
	}
	
	$scope.remove = function() {
		Account.delete({id:account.username}, function() {
			var index = $scope.accounts.indexOf(account);
			$scope.accounts.splice(index, 1);
			$scope.alert = false;
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
		if(!$scope.deal.service) {
			alert('You must specify a service.');
			return;
		} else if(!$scope.deal.description) {
			alert('You must provide a description.');
			return;
		} else if($scope.deal.locations.length === 0) {
			alert('Please click "Add location".');
			return;
		}
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
		if($scope.deal.locations.length >= 2) {
			alert('You can use a maximum of two locations.');
			return;
		} else if(!location.country || !location.city || !location.street) {
			alert('You need to enter a location first.');
			return;
		}
		for(l in $scope.deal.locations) {
			if($scope.deal.locations[l].street === location.street &&
					$scope.deal.locations[l].number === location.number) {
				alert('You have already added that location.');
				return;
			}
		}
		var loc = {};
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
					 var address = fullAddress[0].split(' ', 10);
					 var number;
					 var street = fullAddress[0];
					 for(n in address) {
						 if(!isNaN(address[n])) {
							number = address[n];
							street = street.slice(0, street.length-(number.length+1));
						 }
					 }
					 
					 $scope.loc.country = fullAddress[2];
					 $scope.loc.city = city;
					 $scope.loc.street = street;
	    	    	 $scope.loc.number = number;
					 
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

// CATEGORY
livelance.controller('CategoryController', function($scope, Category, $routeParams) {
	var url = function() {
		return {id : $routeParams.id || ''};
	}

	var update = function() {
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

// REGISTRATION
 livelance.controller("RegistrationController", function($scope, 
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


// PROFILE
livelance.controller('ProfileController', function($scope, profileService) {

	$scope.update = function() {
		profileService.update(function(data) {
			$scope.profile = data;
		});
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

// MANAGEMENT
livelance.controller('ManagementController', function($scope, Management) {
	$scope.management = Management.get();
});

//RATING 
livelance.controller('RatingController', function (Rating, $scope, ratingService) {
	$scope.rate = 0;
	$scope.max = 5;
	$scope.isReadonly = false;

	$scope.hoveringOver = function(value) {
		$scope.overStar = value;
		$scope.percent = 100 * (value / $scope.max);
	};
  
	$scope.submitComment = function(deal, customerName, rate, comment) {
		ratingService.saveComment(deal, customerName, rate, comment);
	}
});

// DROPDOWN
livelance.controller('NavbarController', function ($scope,
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
})
