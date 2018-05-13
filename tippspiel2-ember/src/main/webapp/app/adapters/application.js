import RESTAdapter from 'ember-data/adapters/rest';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import {decamelize} from '@ember/string';
import {pluralize} from 'ember-inflector';

export default RESTAdapter.extend({
  auth: inject(),
  host: 'http://0.0.0.0:8080',
  namespace: 'api',
  pathForType(modelName) {
    return decamelize(pluralize(modelName));
  },
  handleResponse(status, headers, payload, requestData) {
    if (status === 500) {
      return {status, message: 'An unknown error occurred.'};
    } else if (status === 401 || status === 403) {
      return {status, message: "Access denied."};
    } else if (status === 400 || status === 404) {
      return {status, message: payload.message};
    } else {
      return this._super(...arguments);
    }
  },
  headers: computed(function () {
    const token = this.get('auth').getToken();
    return token != null
      ? {Authorization: 'Bearer ' + token}
      : {};
  }).volatile()
});
