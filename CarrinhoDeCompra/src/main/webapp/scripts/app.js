'use strict';

angular.module('carrinhoDeCompra',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Produtos',{templateUrl:'views/Produto/search.html',controller:'SearchProdutoController'})
      .when('/Produtos/new',{templateUrl:'views/Produto/detail.html',controller:'NewProdutoController'})
      .when('/Produtos/edit/:ProdutoId',{templateUrl:'views/Produto/detail.html',controller:'EditProdutoController'})
      .when('/Vendas',{templateUrl:'views/Venda/search.html',controller:'SearchVendaController'})
      .when('/Vendas/new',{templateUrl:'views/Venda/detail.html',controller:'NewVendaController'})
      .when('/Vendas/edit/:VendaId',{templateUrl:'views/Venda/detail.html',controller:'EditVendaController'})
      .when('/VendaProdutos',{templateUrl:'views/VendaProduto/search.html',controller:'SearchVendaProdutoController'})
      .when('/VendaProdutos/new',{templateUrl:'views/VendaProduto/detail.html',controller:'NewVendaProdutoController'})
      .when('/VendaProdutos/edit/:VendaProdutoId',{templateUrl:'views/VendaProduto/detail.html',controller:'EditVendaProdutoController'})
      .when('/VendaProdutos/new/:VendaId',{templateUrl:'views/VendaProduto/detail.html',controller:'NewVendaProdutoController'})
      .when('/VendaProdutos/edit/:VendaProdutoId/:VendaId',{templateUrl:'views/VendaProduto/detail.html',controller:'EditVendaProdutoController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
