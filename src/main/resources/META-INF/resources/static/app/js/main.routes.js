var livelance = angular.module('livelance.routes', ['ngRoute']);

livelance.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl : 'static/app/html/partials/home.html',
		})
		.when('/about', {
			templateUrl : 'static/app/html/partials/about.html'
		})
		.when('/login', {
			templateUrl : 'static/app/html/partials/login.html',
			controller : 'IndexController'
		})
		.when('/register', {
			templateUrl : 'static/app/html/partials/registration.html',
			controller : 'RegistrationController'
		})
		.when('/upload', {
			templateUrl : 'static/app/html/partials/imageUpload.html',
			controller : 'ImageUploadController'
		})
		.when('/accounts', {
			templateUrl : 'static/app/html/partials/accounts.html',
			controller : 'AccountController'
		})
		.when('/accounts/view/:id', {
        	templateUrl : 'static/app/html/partials/viewAccount.html',
        	controller : 'AccountController'
        })
        .when('/accounts/edit/:id', {
        	templateUrl : 'static/app/html/partials/viewAccount.html',
        	controller : 'AccountController'
        })
        .when('/profiles/:id', {
        	templateUrl : 'static/app/html/partials/viewAccount.html',
        	controller : 'ProfileController'
        })
		.when('/categories', {
			templateUrl : 'static/app/html/partials/categories.html',
			controller : 'CategoryController'
		})
		.when('/deals', {
			templateUrl : 'static/app/html/partials/deals.html',
			controller : 'DealController'
		})
		.when('/profiles', {
			templateUrl : 'static/app/html/partials/profiles.html',
			controller : 'ProfileController'
		})
		.when('/management', {
			templateUrl : 'static/app/html/partials/management.html',
			controller : 'ManagementController'
		})
		.otherwise({
            redirectTo: '/'
		});
}]);