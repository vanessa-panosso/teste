'use strict';

angular.module('carrinhoDeCompra').filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    };
});