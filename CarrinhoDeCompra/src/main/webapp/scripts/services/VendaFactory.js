angular.module('carrinhoDeCompra').factory('VendaResource', function($resource){
    var resource = $resource('rest/vendas/:VendaId',{VendaId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});