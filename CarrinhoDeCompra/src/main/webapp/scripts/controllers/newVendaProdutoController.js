
angular.module('carrinhoDeCompra').controller('NewVendaProdutoController', function ($scope, $location, locationParser, flash, VendaProdutoResource , ProdutoResource, VendaResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.vendaProduto = $scope.vendaProduto || {};
    
    $scope.produtoList = ProdutoResource.queryAll(function(items){
        $scope.produtoSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("produtoSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.vendaProduto.produto = {};
            $scope.vendaProduto.produto.id = selection.value;
        }
    });
    
    $scope.vendaList = VendaResource.queryAll(function(items){
        $scope.vendaSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("vendaSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.vendaProduto.venda = {};
            $scope.vendaProduto.venda.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'Adicionado Produto com sucesso.'});
            $location.path('/Venda');
        };
        var errorCallback = function(response) {
            if(response && response.data) {
                flash.setMessage({'type': 'error', 'text': response.data.message || response.data}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        VendaProdutoResource.save($scope.vendaProduto, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Venda");
    };
});