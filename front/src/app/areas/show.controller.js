export default class ShowController {
  constructor($stateParams, Area, Event) {
    this.areaId = $stateParams['id'];
    this.Area = Area;
    this.Event = Event;
    this.area = Area.get({ id: this.areaId });
    this.events = Event.query({ areaId: this.areaId });

    // TODO should get from Server
    this.triggerTypes = [
      { value: 1, label: '入ったら' },
      { value: 2, label: '留まっていたら'},
      { value: 3, label: '離れたら' },
      { value: 4, label: 'いなかったら' }
    ];
    this.actionTypes = [
      { value: 1, label: 'サーバーにログを吐く' },
      { value: 2, label: 'Slackに通知をする' },
      { value: 3, label: 'メールを送る' },
      { value: 4, label: 'Twitterでつぶやく' },
      { value: 5, label: 'しゃべる' }
    ];
    this._newEvent();

  }

  registerEvent(event) {
    event.userArea = this.area;
    event.$save((created) => {
      this.events.push(created);
      this._newEvent();
    });
  }

  labelByTriggerType(triggerTypeValue) {
    const triggerType = this.triggerTypes
      .filter((trigger) => { return trigger.value == triggerTypeValue })
      .shift();
    return triggerType && triggerType.label;
  }

  actionByActionType(actionTypeValue) {
    const actionType = this.actionTypes
      .filter((action) => { return action.value == actionTypeValue })
      .shift();
    return actionType && actionType.label;
  }

  _newEvent() {
    this.newEvent = new this.Event({
      areaId: this.areaId,
      options: ''
    });
  }
}
