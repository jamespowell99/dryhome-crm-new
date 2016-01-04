'use strict';

angular.module('dryhomecrmApp')
    .factory('CustomerOrder', function ($resource, DateUtils) {
        return $resource('api/customerOrders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
