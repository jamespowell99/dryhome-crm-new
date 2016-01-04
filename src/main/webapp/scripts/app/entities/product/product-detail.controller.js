'use strict';

angular.module('dryhomecrmApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        var unsubscribe = $rootScope.$on('dryhomecrmApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
