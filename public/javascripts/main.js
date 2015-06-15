(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var IndexController = function IndexController(Area) {
  _classCallCheck(this, IndexController);

  this.areas = Area.query();
};

exports["default"] = IndexController;
module.exports = exports["default"];

},{}],2:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, '__esModule', {
  value: true
});

var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

var NewController = (function () {
  function NewController($state, Area) {
    _classCallCheck(this, NewController);

    this.$state = $state;
    this.area = new Area();
  }

  _createClass(NewController, [{
    key: 'register',
    value: function register(area) {
      var _this = this;

      area.$save().then(function (saved) {
        _this.$state.go('root.areas.show', { id: saved.id }, { reload: true });
      }, function (err) {
        console.log(err);
      });
    }
  }]);

  return NewController;
})();

exports['default'] = NewController;
module.exports = exports['default'];

},{}],3:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, '__esModule', {
  value: true
});

var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

var ShowController = (function () {
  function ShowController($stateParams, Area, Event) {
    _classCallCheck(this, ShowController);

    this.areaId = $stateParams['id'];
    this.Area = Area;
    this.Event = Event;
    this.area = Area.get({ id: this.areaId });
    this.events = Event.query({ areaId: this.areaId });

    // TODO should get from Server
    this.triggerTypes = [{ value: 1, label: '入ったら' }, { value: 2, label: '留まっていたら' }, { value: 3, label: '離れたら' }, { value: 4, label: 'いなかったら' }];
    this.actionTypes = [{ value: 1, label: 'サーバーにログを吐く' }, { value: 2, label: 'Slackに通知をする' }];
    this._newEvent();
  }

  _createClass(ShowController, [{
    key: 'registerEvent',
    value: function registerEvent(event) {
      var _this = this;

      event.userArea = this.area;
      event.$save(function (created) {
        _this.events.push(created);
        _this._newEvent();
      });
    }
  }, {
    key: 'labelByTriggerType',
    value: function labelByTriggerType(triggerTypeValue) {
      var triggerType = this.triggerTypes.filter(function (trigger) {
        return trigger.value == triggerTypeValue;
      }).shift();
      return triggerType && triggerType.label;
    }
  }, {
    key: 'actionByActionType',
    value: function actionByActionType(actionTypeValue) {
      var actionType = this.actionTypes.filter(function (action) {
        return action.value == actionTypeValue;
      }).shift();
      return actionType && actionType.label;
    }
  }, {
    key: '_newEvent',
    value: function _newEvent() {
      this.newEvent = new this.Event({
        areaId: this.areaId,
        options: ''
      });
    }
  }]);

  return ShowController;
})();

exports['default'] = ShowController;
module.exports = exports['default'];

},{}],4:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, '__esModule', {
  value: true
});
exports['default'] = AreaFactory;

function AreaFactory($resource) {
  return $resource('/api/areas/:id', { id: '@id' });
}

module.exports = exports['default'];

},{}],5:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, '__esModule', {
  value: true
});
exports['default'] = EventFactory;

function EventFactory($resource) {
  return $resource('/api/areas/:areaId/events/:id', {
    id: '@id',
    areaId: '@areaId'
  });
}

module.exports = exports['default'];

},{}],6:[function(require,module,exports){
"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var LayoutController = function LayoutController() {
  _classCallCheck(this, LayoutController);

  this.navbarCollapsed = true;
};

exports["default"] = LayoutController;
module.exports = exports["default"];

},{}],7:[function(require,module,exports){
'use strict';

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

var _commonResourcesAreaResourceJs = require('./common/resources/area.resource.js');

var _commonResourcesAreaResourceJs2 = _interopRequireDefault(_commonResourcesAreaResourceJs);

var _commonResourcesEventResourceJs = require('./common/resources/event.resource.js');

var _commonResourcesEventResourceJs2 = _interopRequireDefault(_commonResourcesEventResourceJs);

var _layoutLayoutControllerJs = require('./layout/layout.controller.js');

var _layoutLayoutControllerJs2 = _interopRequireDefault(_layoutLayoutControllerJs);

var _appAreasIndexControllerJs = require('./app/areas/index.controller.js');

var _appAreasIndexControllerJs2 = _interopRequireDefault(_appAreasIndexControllerJs);

var _appAreasNewControllerJs = require('./app/areas/new.controller.js');

var _appAreasNewControllerJs2 = _interopRequireDefault(_appAreasNewControllerJs);

var _appAreasShowControllerJs = require('./app/areas/show.controller.js');

var _appAreasShowControllerJs2 = _interopRequireDefault(_appAreasShowControllerJs);

angular.module('m3hack-d', ['ngResource', 'ngAnimate', 'ui.bootstrap', 'ui.router']).config(function ($locationProvider, $stateProvider, $urlRouterProvider) {
  $locationProvider.html5Mode(true);
  $urlRouterProvider.otherwise('/');
  $stateProvider.state('root', {
    abstract: true,
    url: '',
    templateUrl: 'layout/layout.tpl.html',
    controller: _layoutLayoutControllerJs2['default'],
    controllerAs: 'layoutCtrl'
  }).state('root.root', { url: '/' }).state('root.areas', {
    url: '/areas',
    controller: _appAreasIndexControllerJs2['default'],
    controllerAs: 'indexCtrl',
    templateUrl: 'app/areas/index.tpl.html'
  }).state('root.areas.new', {
    url: '/new',
    controller: _appAreasNewControllerJs2['default'],
    controllerAs: 'newCtrl',
    templateUrl: 'app/areas/new.tpl.html'
  }).state('root.areas.show', {
    url: '/:id',
    controller: _appAreasShowControllerJs2['default'],
    controllerAs: 'showCtrl',
    templateUrl: 'app/areas/show.tpl.html'
  });
}).factory({
  Area: _commonResourcesAreaResourceJs2['default'],
  Event: _commonResourcesEventResourceJs2['default']
});

},{"./app/areas/index.controller.js":1,"./app/areas/new.controller.js":2,"./app/areas/show.controller.js":3,"./common/resources/area.resource.js":4,"./common/resources/event.resource.js":5,"./layout/layout.controller.js":6}]},{},[7]);
