'use strict';

angular.module('dryhomecrmApp')
    .controller('OrderItemDetailController', function ($scope, $rootScope, $stateParams, entity, OrderItem, Product, N) {
        $scope.orderItem = entity;
        $scope.load = function (id) {
            OrderItem.get({id: id}, function(result) {
                $scope.orderItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('dryhomecrmApp:orderItemUpdate', function(event, result) {
            $scope.orderItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
