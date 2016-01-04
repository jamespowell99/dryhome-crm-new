'use strict';

angular.module('dryhomecrmApp')
    .factory('CustomerSearch', function ($resource) {
        return $resource('api/_search/customers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
