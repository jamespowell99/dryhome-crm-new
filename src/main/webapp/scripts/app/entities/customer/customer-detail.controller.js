'use strict';

angular.module('dryhomecrmApp')
    .controller('CustomerDetailController', function ($scope, $rootScope, $stateParams, entity, Customer) {
        $scope.customer = entity;
        $scope.load = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
            });
        };
        var unsubscribe = $rootScope.$on('dryhomecrmApp:customerUpdate', function(event, result) {
            $scope.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
