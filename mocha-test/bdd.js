'use strict';

// http://chaijs.com/api/bdd
var assert = require('assert');
var expect = require('chai').expect;
var jwt = require('jsonwebtoken');
var restler = require('restler');
// var request = require('request');
var config = {
  host: 'http://localhost',
  port: 3000,
  url: 'http://localhost:3000'
};
var api = function(uri) {
  return config.url + uri;
};

var options = {
  tunnel: 'both',
  host: '172.17.0.20', 
  port: 80
};
// require('global-tunnel').initialize(options);
// process.env.http_proxy = 'http://172.17.0.20:80'; // not working for restify

describe('Calories tracker API - ', function() {

  describe('/register - ', function() {
    it('normal', function(done) {
      var username = 'harry';
      restler.post(api('/register'), {
        data: { 
          username: username, 
          password1: 'test',
          password2: 'test',
          setting: {expNumber: 3000}
        },
      }).on('complete', function(data, response) {
        console.log(data);
        if (response.statusCode == 400) {
          expect(data.error).to.equal('User ' + username + ' already exists!');
        } else if (response.statusCode == 200) {
          expect(data.username).to.equal(username);
        }
        done();
      });
    });
  });
});

