export default class NewController {
  constructor($state, Area) {
    this.$state = $state;
    this.area = new Area();
  }

  register(area) {
    area.$save().then(
      (saved) => {
        this.$state.go('root.areas.show', { id: saved.id }, { reload: true });
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
