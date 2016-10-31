var livelance = angular.module('livelance', [ 'ui.bootstrap',
                                          'ngAnimate',
                                          'ngRoute',
                                          'hateoas',
                                          'ngResource',
                                          'livelance.services',
                                          'livelance.controllers',
                                          'livelance.routes']);
livelance.config(function (HateoasInterceptorProvider) {
    HateoasInterceptorProvider.transformAllResponses();
});