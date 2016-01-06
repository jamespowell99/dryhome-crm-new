'use strict';

angular.module('dryhomecrmApp')
    .controller('CustomerOrderDetailController', function ($scope, $rootScope, $stateParams, entity, CustomerOrder, Customer, OrderItem) {
        $scope.customerOrder = entity;
        $scope.load = function (id) {
            CustomerOrder.get({id: id}, function(result) {
                $scope.customerOrder = result;
            });
        };
        var unsubscribe = $rootScope.$on('dryhomecrmApp:customerOrderUpdate', function(event, result) {
            $scope.customerOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
