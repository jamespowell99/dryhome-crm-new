'use strict';

angular.module('dryhomecrmApp')
    .factory('CustomerOrder', function ($resource, DateUtils) {
        return $resource('api/customerOrders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.orderDate = DateUtils.convertLocaleDateFromServer(data.orderDate);
                    data.dispatchDate = DateUtils.convertLocaleDateFromServer(data.dispatchDate);
                    data.invoiceDate = DateUtils.convertLocaleDateFromServer(data.invoiceDate);
                    data.paymentDate = DateUtils.convertLocaleDateFromServer(data.paymentDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.orderDate = DateUtils.convertLocaleDateToServer(data.orderDate);
                    data.dispatchDate = DateUtils.convertLocaleDateToServer(data.dispatchDate);
                    data.invoiceDate = DateUtils.convertLocaleDateToServer(data.invoiceDate);
                    data.paymentDate = DateUtils.convertLocaleDateToServer(data.paymentDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.orderDate = DateUtils.convertLocaleDateToServer(data.orderDate);
                    data.dispatchDate = DateUtils.convertLocaleDateToServer(data.dispatchDate);
                    data.invoiceDate = DateUtils.convertLocaleDateToServer(data.invoiceDate);
                    data.paymentDate = DateUtils.convertLocaleDateToServer(data.paymentDate);
                    return angular.toJson(data);
                }
            }
        });
    });
