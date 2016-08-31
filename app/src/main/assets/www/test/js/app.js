/**
 * Created by Administrator on 2016/8/30.
 */
var app = angular.module("myApp", ['myApp.rHelloFilter']);
app.directive("hello",function(){
    return{
        restrict:'E',
        template:'<div class="red">hello 2131313<strong>你好</strong>everyone</div>',
        replace:true
    }

})