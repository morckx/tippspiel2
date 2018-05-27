import Route from '@ember/routing/route';
import {inject} from '@ember/service';

export default Route.extend({
  auth: inject(),
  model(params, transition) {
    return this.get('auth.user').then(authenticatedUser => {
      return this.get('store').findRecord('user', authenticatedUser.id);
    })
  }
});
