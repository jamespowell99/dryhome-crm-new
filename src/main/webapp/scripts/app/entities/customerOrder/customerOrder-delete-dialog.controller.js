'use strict';

angular.module('dryhomecrmApp')
	.controller('CustomerOrderDeleteController', function($scope, $uibModalInstance, entity, CustomerOrder) {

        $scope.customerOrder = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CustomerOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
