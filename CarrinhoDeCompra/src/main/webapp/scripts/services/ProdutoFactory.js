angular.module('carrinhoDeCompra').factory('ProdutoResource', function($resource){
    var resource = $resource('rest/produtos/:ProdutoId',{ProdutoId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});