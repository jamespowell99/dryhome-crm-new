'use strict';

angular.module('dryhomecrmApp')
    .factory('OrderItemSearch', function ($resource) {
        return $resource('api/_search/orderItems/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
