'use strict';

angular.module('dryhomecrmApp')
    .factory('CustomerOrderSearch', function ($resource) {
        return $resource('api/_search/customerOrders/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
