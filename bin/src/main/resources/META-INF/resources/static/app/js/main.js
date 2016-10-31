var hire_me = angular.module('hire_me', [ 'ui.bootstrap',
//                                        'uibAccordion',
                                          'ngAnimate',
                                          'ngRoute',
                                          'hateoas',
                                          'ngResource',
                                          'hire_me.controllers',
                                          'hire_me.routes']);
hire_me.config(function (HateoasInterceptorProvider) {
    HateoasInterceptorProvider.transformAllResponses();
});