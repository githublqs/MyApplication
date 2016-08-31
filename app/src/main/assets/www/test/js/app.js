/**
 * Created by Administrator on 2016/8/30.
 */
var app = angular.module("myApp", ['myApp.rHelloFilter','ngRoute','ngStorage']

    ,function ($provide) {
    //console.log($provide);
    $provide.provider('providerServices01',function () {
        //可以在config里面配置的属性
        this.name='';
        this.age='';
        this.$get=function () {
            var that=this;
            var _name='';
            var service={};
            service.setName=function(name){

                _name=name;
            }
            service.getName=function(){

                return _name;
            }
            service.getConfigName=function(){

                return that.name+'age:'+that.age;
            }

            return service;
        }
    });
    /*$provide.provider('providerServices02',function () {
        this.$get=function () {
            return   'this is providerServices01';
        }
    });
    $provide.('factoryServices01',function(){
        return {
            message:'this is factoryServices01'
        }

    });
    $provide.factory('factoryServices02',function(){

        return 'this is factoryServices01  text';
    });*/
    $provide.service('serviceServices02',function(){
            return 'this is serviceServices02 text';//与factory的区别 无法返回字符串
        //  console.log(serviceServices02); Object {}
    });
}).provider('providerServices03',function () {
    this.$get=function () {
        return   'this is providerServices03';
    }
}).service('serviceServices03',function(){
    return {
        message:'this is serviceServices03'
    }
}).factory('factoryServices03',function(){
    return {
        message:'this is factoryServices03'
    }
}).config(["$provide","providerServices01Provider",function ($provide,providerServices01Provider) {
    $provide.service('serviceServices01',["$http","$rootScope","$log","$filter",function($http,$rootScope,$log,$filter){
        var  _name;
        this.setName=function (name) {
            _name=name;
        }
        this.getName=function () {
            return _name;
        }
        this.getData=function () {
            $log.warn("log service warn");
            var  myUrl ="http://www.phonegap100.com/appapi.php?a=getPortalList&catid=20&page=1&callback=JSON_CALLBACK";
            return $http.jsonp(myUrl,{cache:true})/*
                .success(function (data) {
                    console.log("-----getData----success-----");
                    console.log(data);

                }).error(function (err) {
                    console.log("-----getData----error-----");
                    console.log(err);
                })*/;
        }
    }]);
    providerServices01Provider.name="xxxxx";
    providerServices01Provider.age=30;
}]).config(["$interpolateProvider",function ($interpolateProvider) {
    /*$interpolateProvider.startSymbol('##');
    $interpolateProvider.endSymbol('##');*/
}]).config(['$routeProvider',function($routeProvider){
    $routeProvider
        .when('/div1',{
            templateUrl : 'template1.html',
            controller : 'div1Controller'
        })
        .when('/div2',{
            template : '<p>这是div2{{text}}</p>',
            controller : 'div2Controller'
        })
        .when('/div3',{
            template : '<p>这是div3{{text}}</p>',
            controller : 'div3Controller'
        })
       .when('/content/:id/:cateid',{
            template : '<p>这是content{{id}}</p>',
            controller : 'div4Controller'
        })
        .otherwise({
            redirectTo : '/div1'
        });

}])
.directive("hello",function(){
    return{
        restrict:'E',
        template:'<div class="red">hello 2131313<strong>你好</strong>everyone</div>',
        replace:true
    }

});
