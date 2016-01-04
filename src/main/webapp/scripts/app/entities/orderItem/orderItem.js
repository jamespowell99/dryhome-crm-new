'use strict';

angular.module('dryhomecrmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('orderItem', {
                parent: 'entity',
                url: '/orderItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'OrderItems'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/orderItem/orderItems.html',
                        controller: 'OrderItemController'
                    }
                },
                resolve: {
                }
            })
            .state('orderItem.detail', {
                parent: 'entity',
                url: '/orderItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'OrderItem'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/orderItem/orderItem-detail.html',
                        controller: 'OrderItemDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'OrderItem', function($stateParams, OrderItem) {
                        return OrderItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('orderItem.new', {
                parent: 'orderItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/orderItem/orderItem-dialog.html',
                        controller: 'OrderItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    price: null,
                                    qty: null,
                                    notes: null,
                                    orderIndex: null,
                                    serialNumber: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('orderItem', null, { reload: true });
                    }, function() {
                        $state.go('orderItem');
                    })
                }]
            })
            .state('orderItem.edit', {
                parent: 'orderItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/orderItem/orderItem-dialog.html',
                        controller: 'OrderItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['OrderItem', function(OrderItem) {
                                return OrderItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('orderItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('orderItem.delete', {
                parent: 'orderItem',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/orderItem/orderItem-delete-dialog.html',
                        controller: 'OrderItemDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['OrderItem', function(OrderItem) {
                                return OrderItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('orderItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
