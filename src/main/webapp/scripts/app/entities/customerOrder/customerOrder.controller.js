'use strict';

angular.module('dryhomecrmApp')
    .controller('CustomerOrderController', function ($scope, $state, CustomerOrder, CustomerOrderSearch, ParseLinks) {

        $scope.customerOrders = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            CustomerOrder.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.customerOrders = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CustomerOrderSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.customerOrders = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.customerOrder = {
                name: null,
                contactTitle: null,
                contactFirstName: null,
                contactSurname: null,
                tel: null,
                mob: null,
                email: null,
                address1: null,
                address2: null,
                address3: null,
                town: null,
                postCode: null,
                products: null,
                interested: null,
                paid: null,
                notes: null,
                id: null
            };
        };
    });
