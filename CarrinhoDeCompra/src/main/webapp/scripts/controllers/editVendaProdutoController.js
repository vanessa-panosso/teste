

angular.module('carrinhoDeCompra').controller('EditVendaProdutoController', function($scope, $routeParams, $location, flash, VendaProdutoResource , ProdutoResource, VendaResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.vendaProduto = new VendaProdutoResource(self.original);
            ProdutoResource.queryAll(function(items) {
                $scope.produtoSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.vendaProduto.produto && item.id == $scope.vendaProduto.produto.id) {
                        $scope.produtoSelection = labelObject;
                        $scope.vendaProduto.produto = wrappedObject;
                        self.original.produto = $scope.vendaProduto.produto;
                    }
                    return labelObject;
                });
            });
            VendaResource.queryAll(function(items) {
                $scope.vendaSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.vendaProduto.venda && item.id == $scope.vendaProduto.venda.id) {
                        $scope.vendaSelection = labelObject;
                        $scope.vendaProduto.venda = wrappedObject;
                        self.original.venda = $scope.vendaProduto.venda;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The vendaProduto could not be found.'});
            $location.path("/VendaProdutos");
        };
        VendaProdutoResource.get({VendaProdutoId:$routeParams.VendaProdutoId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.vendaProduto);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The vendaProduto was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.vendaProduto.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/VendaProdutos");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The vendaProduto was deleted.'});
            $location.path("/VendaProdutos");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.vendaProduto.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("produtoSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.vendaProduto.produto = {};
            $scope.vendaProduto.produto.id = selection.value;
        }
    });
    $scope.$watch("vendaSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.vendaProduto.venda = {};
            $scope.vendaProduto.venda.id = selection.value;
        }
    });
    
    $scope.get();
});