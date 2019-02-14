angular.module('carrinhoDeCompra').factory('VendaProdutoResource', function($resource){
    var resource = $resource('rest/vendaprodutos/:VendaProdutoId',{VendaProdutoId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});