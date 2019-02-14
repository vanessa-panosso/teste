
angular.module('carrinhoDeCompra').controller('NewVendaController', function ($scope, $location, locationParser, flash, VendaResource , ProdutoResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.venda = $scope.venda || {};
	$scope.adicionandoProduto = false;
    $scope.vendaProduto = $scope.vendaProduto || {};
	
    $scope.produtoList = ProdutoResource.queryAll(function(items){
        $scope.produtoSelectionList = $.map(items, function(item) {
            return ( {
                value : item,
                text : item.nome
            });
        });
    });

    $scope.selecionadoProduto = function(produtoSelecionado) {
		$scope.vendaProduto.produto = {}
		$scope.vendaProduto.produto.id = produtoSelecionado.value.id;
		$scope.vendaProduto.produto.nome = produtoSelecionado.value.nome;
		$scope.vendaProduto.quantidade = 1;
		$scope.vendaProduto.valorCustoProduto = produtoSelecionado.value.custoProduto;
    };
	
	$scope.cancelarProduto = function() {
        $scope.adicionandoProduto = false;
		$scope.vendaProduto = {};
    };
    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'Venda cadastrada com sucesso.'});
            $location.path('/Vendas');
        };
        var errorCallback = function(response) {
            if(response && response.data) {
                flash.setMessage({'type': 'error', 'text': response.data.message || response.data}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
		$scope.calcularValores();
        VendaResource.save($scope.venda, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Vendas");
    };
	
	$scope.adicionarProduto = function() {
        $scope.adicionandoProduto = true;
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
			$scope.venda.produtos.map(function(valor){
				valor.valorDespesa = despesaProduto;
				var valorSomado = (valor.valorCustoProduto * valor.quantidade) + valor.valorDespesa;
				var margemLucro = valor.margemLucro != null? (valorSomado * (valor.margemLucro / 100)) : 0;
				valor.valor =  valorSomado + margemLucro;
				total += parseFloat(valor.valor);
			});
			$scope.venda.valorVenda = total;
		}
	}
});