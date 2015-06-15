export default class IndexController {
  constructor(Area) {
    this.areas = Area.query();
  }
}
