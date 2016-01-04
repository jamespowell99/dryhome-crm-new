'use strict';

angular.module('dryhomecrmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customerOrder', {
                parent: 'entity',
                url: '/customerOrders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CustomerOrders'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrders.html',
                        controller: 'CustomerOrderController'
                    }
                },
                resolve: {
                }
            })
            .state('customerOrder.detail', {
                parent: 'entity',
                url: '/customerOrder/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CustomerOrder'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-detail.html',
                        controller: 'CustomerOrderDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CustomerOrder', function($stateParams, CustomerOrder) {
                        return CustomerOrder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('customerOrder.new', {
                parent: 'customerOrder',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-dialog.html',
                        controller: 'CustomerOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('customerOrder', null, { reload: true });
                    }, function() {
                        $state.go('customerOrder');
                    })
                }]
            })
            .state('customerOrder.edit', {
                parent: 'customerOrder',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-dialog.html',
                        controller: 'CustomerOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customerOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('customerOrder.delete', {
                parent: 'customerOrder',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-delete-dialog.html',
                        controller: 'CustomerOrderDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customerOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
