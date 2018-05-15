import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('standings');
  this.route('bets');
  this.route('sign-up');
  this.route('sign-in');
  this.route('me');

  this.route('communities', function() {
    this.route('new');
  });

  this.route('competitions', function() {
    this.route('new');
  });

  this.route('betting-games', function() {
    this.route('new');
  });

  this.route('users', function() {
    this.route('bets', {path: '/:user_id/bets'});
  });

  this.route('fixtures', function() {
    this.route('bets', {path: '/:fixture_id/bets'});
  });
});

export default Router;
