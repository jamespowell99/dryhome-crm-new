'use strict';

angular.module('dryhomecrmApp').controller('OrderItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderItem', 'Product', 'N',
        function($scope, $stateParams, $uibModalInstance, entity, OrderItem, Product, N) {

        $scope.orderItem = entity;
        $scope.products = Product.query();
        $scope.ns = N.query();
        $scope.load = function(id) {
            OrderItem.get({id : id}, function(result) {
                $scope.orderItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dryhomecrmApp:orderItemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.orderItem.id != null) {
                OrderItem.update($scope.orderItem, onSaveSuccess, onSaveError);
            } else {
                OrderItem.save($scope.orderItem, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
