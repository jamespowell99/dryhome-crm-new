'use strict';

describe('Controller Tests', function() {

    describe('CustomerOrder Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomerOrder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomerOrder = jasmine.createSpy('MockCustomerOrder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CustomerOrder': MockCustomerOrder
            };
            createController = function() {
                $injector.get('$controller')("CustomerOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dryhomecrmApp:customerOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
