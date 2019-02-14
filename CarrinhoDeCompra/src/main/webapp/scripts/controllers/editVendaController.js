

angular.module('carrinhoDeCompra').controller('EditVendaController', function($scope, $routeParams, $location, flash, VendaResource ,ProdutoResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.adicionandoProduto = false;
    $scope.vendaProduto = $scope.vendaProduto || {};
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.venda = new VendaResource(self.original);
            VendaProdutoResource.queryAll(function(items) {
                $scope.produtosSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.venda.produtos){
                        $.each($scope.venda.produtos, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.produtosSelection.push(labelObject);
                                $scope.venda.produtos.push(wrappedObject);
                            }
                        });
                        self.original.produtos = $scope.venda.produtos;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The venda could not be found.'});
            $location.path("/Vendas");
        };
        VendaResource.get({VendaId:$routeParams.VendaId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.venda);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The venda was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.venda.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Vendas");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The venda was deleted.'});
            $location.path("/Vendas");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.venda.$remove(successCallback, errorCallback);
    };
    
    $scope.selecionadoProduto = function(produtoSelecionado) {
		$scope.vendaProduto.produto = {}
		$scope.vendaProduto.produto.id = produtoSelecionado.value.id;
		$scope.vendaProduto.produto.nome = produtoSelecionado.value.nome;
		$scope.vendaProduto.quantidade = 1;
		$scope.vendaProduto.valorCustoProduto = produtoSelecionado.value.custoProduto;
    };
    $scope.produtoList = ProdutoResource.queryAll(function(items){
        $scope.produtoSelectionList = $.map(items, function(item) {
            return ( {
                value : item,
                text : item.nome
            });
        });
    });
    $scope.get();
	
	$scope.adicionarProduto = function() {
        $scope.adicionandoProduto = true;
    };
	$scope.cancelarProduto = function() {
        $scope.adicionandoProduto = false;
		$scope.vendaProduto = {};
    };
	$scope.saveProduto = function() {
		$scope.adicionandoProduto = false;
		if (!$scope.venda.produtos) {
			$scope.venda.produtos = [];
		}
		$scope.venda.produtos.push($scope.vendaProduto);
		$scope.vendaProduto = {};
	}
	$scope.calcularValores = function() {
		if ($scope.venda.produtos && $scope.venda.produtos.length) {
			var totalItens = $scope.venda.produtos.length;
			var despesaProduto = $scope.venda.valorDespesasTotais / totalItens;
			var total = 0;
			total += $scope.venda.produtos.map(function(valor){
				valor.valorDespesa = despesaProduto;
				var margemLucro = valor.marquemLucro != null? (valor.valorCustoProduto * (valor.margemLucro / 100)) : 0;
				valor.valor = (valor.valorCustoProduto * valor.quantidade) + valor.valorDespesa + margemLucro;
				return valor.valor;
			});
			$scope.venda.valorVenda = total;
		}
	}
});