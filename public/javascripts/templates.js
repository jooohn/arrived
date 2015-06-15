angular.module("m3hack-d").run(["$templateCache", function($templateCache) {$templateCache.put("layout/layout.tpl.html","<div id=\"layout-layout\"><header class=\"header\"><nav role=\"navigation\" class=\"navbar navbar-inverse navbar-embossed navbar-fixed-top\"><div class=\"container-fluid\"><div class=\"navbar-header\"><button type=\"button\" ng-click=\"layoutCtrl.navbarCollapsed = !layoutCtrl.navbarCollapsed\" class=\"navbar-toggle\"></button><a ui-sref=\"root.root\" class=\"navbar-brand\">M3Hack-D</a></div><div collapse=\"layoutCtrl.navbarCollapsed\" class=\"collapse navbar-collapse\"><ul class=\"nav navbar-nav\"><li ui-sref-active=\"active\"><a ui-sref=\"root.areas\">AREA</a></li></ul></div></div></nav></header><div ui-view class=\"container-fluid\"></div></div>");
$templateCache.put("app/areas/index.tpl.html","<div class=\"row\"><div class=\"col-sm-3\"><div class=\"form-group\"><a ui-sref=\"root.areas.new\" class=\"btn btn-primary btn-block\"><span>新規作成</span></a></div><ul class=\"list-group\"><a ng-repeat=\"area in indexCtrl.areas\" ui-sref-active=\"active\" ui-sref=\"root.areas.show({ id: area.id })\" class=\"list-group-item\"><h4 class=\"list-group-item-heading\">{{ area.name }}</h4><p class=\"list-group-item-text\"><span>latitude: {{ area.area.location.latitude }}</span><span>&nbsp;/&nbsp;</span><span>longitude: {{ area.area.location.longitude }}</span></p></a></ul></div><div ui-view class=\"col-sm-9\"></div></div>");
$templateCache.put("app/areas/new.tpl.html","<h4>新規作成</h4><form ng-submit=\"newCtrl.register(newCtrl.area)\" class=\"form-horizontal\"><div class=\"form-group\"><label class=\"control-label col-sm-3\">名前</label><div class=\"col-sm-9\"><input type=\"text\" ng-model=\"newCtrl.area.name\" required class=\"form-control\"></div></div><div class=\"form-group\"><label class=\"control-label col-sm-3\">緯度</label><div class=\"col-sm-9\"><input type=\"number\" ng-model=\"newCtrl.area.area.location.latitude\" min=\"-180.0\" max=\"180.0\" step=\"any\" required class=\"form-control\"></div></div><div class=\"form-group\"><label class=\"control-label col-sm-3\">経度</label><div class=\"col-sm-9\"><input type=\"number\" ng-model=\"newCtrl.area.area.location.longitude\" min=\"-180.0\" max=\"180.0\" step=\"any\" required class=\"form-control\"></div></div><div class=\"form-group\"><label class=\"control-label col-sm-3\">距離</label><div class=\"col-sm-9\"><input type=\"number\" ng-model=\"newCtrl.area.area.distance\" min=\"0.0\" step=\"any\" required class=\"form-control\"></div></div><div class=\"form-group\"><div class=\"col-sm-offset-3 col-sm-10\"><button type=\"submit\" class=\"btn btn-primary\">登録</button></div></div></form>");
$templateCache.put("app/areas/show.tpl.html","<h3>{{ showCtrl.area.name }}</h3><div>{{ showCtrl.area }}</div><div>{{ showCtrl.newEvent }}</div><h5>イベント追加</h5><form ng-submit=\"showCtrl.registerEvent(showCtrl.newEvent)\" class=\"form-horizontal\"><div class=\"form-group\"><label class=\"control-label col-sm-2\">Trigger</label><div class=\"col-sm-10\"><select ng-model=\"showCtrl.newEvent.triggerType\" ng-options=\"trigger.value as trigger.label for trigger in showCtrl.triggerTypes\" required class=\"form-control\"></select></div></div><div class=\"form-group\"><label class=\"control-label col-sm-2\">Action</label><div class=\"col-sm-10\"><select ng-model=\"showCtrl.newEvent.actionType\" ng-options=\"action.value as action.label for action in showCtrl.actionTypes\" required class=\"form-control\"></select></div></div><div class=\"form-group\"><label class=\"control-label col-sm-2\">Options</label><div class=\"col-sm-10\"><input type=\"text\" ng-model=\"showCtrl.newEvent.options\" class=\"form-control\"></div></div><div class=\"form-group\"><div class=\"col-sm-offset-2 col-sm-10\"><button type=\"submit\" class=\"btn btn-primary\">Register</button></div></div></form><table class=\"table\"><thead><tr><td>Trigger</td><td>Action</td><td>Options</td></tr></thead><tbody><tr ng-repeat=\"event in showCtrl.events\"><td>{{ showCtrl.labelByTriggerType(event.triggerType) }}</td><td>{{ showCtrl.actionByActionType(event.actionType) }}</td><td>{{ event.options }}</td></tr></tbody></table>");}]);