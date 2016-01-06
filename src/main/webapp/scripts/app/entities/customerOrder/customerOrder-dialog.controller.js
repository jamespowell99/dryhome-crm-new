'use strict';

angular.module('dryhomecrmApp').controller('CustomerOrderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerOrder', 'Customer', 'OrderItem',
        function($scope, $stateParams, $uibModalInstance, entity, CustomerOrder, Customer, OrderItem) {

        $scope.customerOrder = entity;
        $scope.customers = Customer.query();
        $scope.orderitems = OrderItem.query();
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
        $scope.datePickerForOrderDate = {};

        $scope.datePickerForOrderDate.status = {
            opened: false
        };

        $scope.datePickerForOrderDateOpen = function($event) {
            $scope.datePickerForOrderDate.status.opened = true;
        };
        $scope.datePickerForDispatchDate = {};

        $scope.datePickerForDispatchDate.status = {
            opened: false
        };

        $scope.datePickerForDispatchDateOpen = function($event) {
            $scope.datePickerForDispatchDate.status.opened = true;
        };
        $scope.datePickerForInvoiceDate = {};

        $scope.datePickerForInvoiceDate.status = {
            opened: false
        };

        $scope.datePickerForInvoiceDateOpen = function($event) {
            $scope.datePickerForInvoiceDate.status.opened = true;
        };
        $scope.datePickerForPaymentDate = {};

        $scope.datePickerForPaymentDate.status = {
            opened: false
        };

        $scope.datePickerForPaymentDateOpen = function($event) {
            $scope.datePickerForPaymentDate.status.opened = true;
        };
}]);
