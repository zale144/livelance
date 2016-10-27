var hire_me = angular.module('hire_me.routes', ['ngRoute']);

hire_me.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl : 'static/app/html/partial/home.html',
		})
		.when('/login', {
			templateUrl : 'static/app/html/partial/login.html',
			controller : 'IndexController'
		})
		.when('/register', {
			templateUrl : 'static/app/html/partial/registration.html',
			controller : 'RegistrationController'
		})
		.when('/upload', {
			templateUrl : 'static/app/html/partial/imageUpload.html',
			controller : 'ImageUploadController'
		})
		.when('/accounts', {
			templateUrl : 'static/app/html/partial/accounts.html',
			controller : 'AccountController'
		})
		.when('/accounts/view/:id', {
        	templateUrl : '/static/app/html/partial/viewAccount.html',
        	controller : 'AccountController'
        })
        .when('/accounts/edit/:id', {
        	templateUrl : '/static/app/html/partial/viewAccount.html',
        	controller : 'AccountController'
        })
        .when('/profiles/:id', {
        	templateUrl : '/static/app/html/partial/viewAccount.html',
        	controller : 'ProfileController'
        })
		.when('/categories', {
			templateUrl : 'static/app/html/partial/categories.html',
			controller : 'CategoryController'
		})
		.when('/deals', {
			templateUrl : 'static/app/html/partial/deals.html',
			controller : 'DealController'
		})
		.when('/profiles', {
			templateUrl : 'static/app/html/partial/profiles.html',
			controller : 'ProfileController'
		})
		.when('/management', {
			templateUrl : 'static/app/html/partial/management.html',
			controller : 'ManagementController'
		})
		.otherwise({
            redirectTo: '/'
		});
}]);