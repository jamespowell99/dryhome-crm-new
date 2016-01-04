'use strict';

angular.module('dryhomecrmApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


