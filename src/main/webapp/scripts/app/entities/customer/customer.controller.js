'use strict';

angular.module('dryhomecrmApp')
    .controller('CustomerController', function ($scope, $state, Customer, CustomerSearch, ParseLinks) {

        $scope.customers = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Customer.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.customers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CustomerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.customers = result;
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
            $scope.customer = {
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
