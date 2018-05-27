import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    submit() {
      // TODO Disable password and passwordConfirmation validation.
      this.model.save()
        .then(res => this.get('auth').storeToken(res.get('token')))
        .catch(err => this.get('resHandler').handleError(err))
    }
  }
});
