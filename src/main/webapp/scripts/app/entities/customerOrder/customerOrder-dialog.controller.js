'use strict';

angular.module('dryhomecrmApp').controller('CustomerOrderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerOrder',
        function($scope, $stateParams, $uibModalInstance, entity, CustomerOrder) {

        $scope.customerOrder = entity;
        $scope.load = function(id) {
            CustomerOrder.get({id : id}, function(result) {
                $scope.customerOrder = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dryhomecrmApp:customerOrderUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.customerOrder.id != null) {
                CustomerOrder.update($scope.customerOrder, onSaveSuccess, onSaveError);
            } else {
                CustomerOrder.save($scope.customerOrder, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
