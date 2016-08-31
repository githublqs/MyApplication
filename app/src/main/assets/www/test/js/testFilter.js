var filter = angular.module("myApp.rHelloFilter", []);
filter.filter("rHello",function(){
    return function(input,n1,n2){
        console.log(input);
        console.log(n1);
        console.log(n2);
        return input.replace(/Hello/,"你好");
    }
});
