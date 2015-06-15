import AreaResourceFactory from './common/resources/area.resource.js';
import EventResourceFactory from './common/resources/event.resource.js';
import layoutCtrl from './layout/layout.controller.js';
import appAreasIndexController from './app/areas/index.controller.js';
import appAreasNewController from './app/areas/new.controller.js';
import appAreasShowController from './app/areas/show.controller.js';

angular.module('m3hack-d', ['ngResource', 'ngAnimate', 'ui.bootstrap', 'ui.router'])
  .config(($locationProvider, $stateProvider, $urlRouterProvider) => {
    $locationProvider.html5Mode(true);
    $urlRouterProvider.otherwise('/');
    $stateProvider
      .state('root', {
        abstract: true,
        url: '',
        templateUrl: 'layout/layout.tpl.html',
        controller: layoutCtrl,
        controllerAs: 'layoutCtrl'
      })
      .state('root.root', { url: '/' })
      .state('root.areas', {
        url: '/areas',
        controller: appAreasIndexController,
        controllerAs: 'indexCtrl',
        templateUrl: 'app/areas/index.tpl.html'
      })
      .state('root.areas.new', {
        url: '/new',
        controller: appAreasNewController,
        controllerAs: 'newCtrl',
        templateUrl: 'app/areas/new.tpl.html'
      })
      .state('root.areas.show', {
        url: '/:id',
        controller: appAreasShowController,
        controllerAs: 'showCtrl',
        templateUrl: 'app/areas/show.tpl.html'
      })
    ;
  })
  .factory({
    Area: AreaResourceFactory,
    Event: EventResourceFactory
  })
;
