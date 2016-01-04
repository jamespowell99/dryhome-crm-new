 'use strict';

angular.module('dryhomecrmApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-dryhomecrmApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-dryhomecrmApp-params')});
                }
                return response;
            }
        };
    });
