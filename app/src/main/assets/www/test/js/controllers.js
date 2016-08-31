/**
 * Created by lqs on 2016/8/30.
 */

app.controller("personController",["$scope","$filter","providerServices03","serviceServices03","factoryServices03","providerServices01","serviceServices02","serviceServices01","$location",'$anchorScroll',"$sce","$cacheFactory","$localStorage",
  function ($scope,$filter,providerServices03,serviceServices03,factoryServices03,providerServices01,serviceServices02,serviceServices01,$location,$anchorScroll,$sce,$cacheFactory,$localStorage) {
      //$location.search({'age':20});//跳转到http://localhost:63342/www/test/test1.html?_ijt=iephel07fs244rr67au1ktt9e8#?age=20
      //console.log($location.absUrl());//http://localhost:63342/www/test/test1.html?_ijt=iephel07fs244rr67au1ktt9e8
      //$location.hash('hello');//$anchorScroll 服务 则会跳转到  $$absUrl: "http://localhost:63342/www/test/test1.html?_ijt=iephel07fs244rr67au1ktt9e8##hello" 并且定位到 id为 hello 的元素,锚链接效果
      //console.log($location.host());//localhost
      //console.log($location.path("/new"));// 跳转到http://localhost:63342/www/test/test1.html?_ijt=iephel07fs244rr67au1ktt9e8#/new#hello //后退键可返回
      //$location.path("/new").replace();//跳转到 http://localhost:63342/www/test/test1.html?_ijt=iephel07fs244rr67au1ktt9e8#/new#hello 无法后退返回
      //console.log($location.protocol());//返回 http

      var cache=$cacheFactory("cacheFactoryId");
      cache.put("name", "lqs");
      $localStorage.$default({
          counter: 42
      });


      $scope.trustAsHtml=$sce.trustAsHtml('<h1>trustAsHtml</h1>');
      $scope.trustAsHtmlFunc=function(){
        return $sce.trustAsHtml('<h1>trustAsHtml</h1>');
      };


    $scope.person= {
        firstName:"John2",
        lastName : "Doe1",
        fullName : function () {
            var x=$scope.person;
            return x.firstName +" "+ x.lastName;

        }
    };
   // $scope.personCopy=angular.copy($scope.person);
    //console.log(rHello);
   // var arrayObj = new Array();
    //arrayObj.push(personCopy);
    /*arrayObj[0]=$scope.personCopy;
    arrayObj[1]=angular.copy($scope.person);
    arrayObj[1].firstName="John1";
    arrayObj[1].lastName="Doe2";
    var ret=$filter('orderBy')(arrayObj,{name:'firstName'});//没成功
    console.log(ret);*/
   /* var xxx="xxxxxHelloxxxxx";
   $filter('rHello')(xxx);
    console.log(xxx);*/
     /*
    console.log(providerServices02);*/

     /*console.log(factoryServices01);
    console.log(factoryServices02);

   */
      console.log(providerServices03);
      console.log(factoryServices03);
      console.log(serviceServices03);
      console.log(serviceServices02);
      providerServices01.setName('李四1111');
      console.log(providerServices01);

      console.log(providerServices01.getName());
      console.log(providerServices01.getConfigName());
      console.log("---------------");
      serviceServices01.setName("serviceServices01");
      console.log(serviceServices01.getName());

      serviceServices01.getData()
       .success(function (data) {
       console.log("-----getData----success-----");
       console.log(data);

       }).error(function (err) {
       console.log("-----getData----error-----");
       console.log(err);
       });

    $scope.includeFileUr="beInclude.html";
    $scope.tpl="tpl.html";
    $scope.json={"aa":1,bb:"111"};
    $scope.nowTime=1288323623006;
}]);

app.controller("nameController", function($scope,$http) {
    /*$scope.names =[
     {Name: "lqs", Country: "sy"},
     {Name: "lqs2", Country: "sz"}
     ];*/

    // 先在Controller中定义function: myFilter
    $scope.myFilter = function (item) {

        return false;

    };
    var response={ "records":[ {"Name":"Alfreds Futterkiste","City":"Berlin","Country":"Germany"}, {"Name":"Ana Trujillo Emparedados y helados","City":"México D.F.","Country":"Mexico"}, {"Name":"Antonio Moreno Taquería","City":"México D.F.","Country":"Mexico"}, {"Name":"Around the Horn","City":"London","Country":"UK"}, {"Name":"B's Beverages","City":"London","Country":"UK"}, {"Name":"Berglunds snabbköp","City":"Luleå","Country":"Sweden"}, {"Name":"Blauer See Delikatessen","City":"Mannheim","Country":"Germany"}, {"Name":"Blondel père et fils","City":"Strasbourg","Country":"France"}, {"Name":"Bólido Comidas preparadas","City":"Madrid","Country":"Spain"}, {"Name":"Bon app'","City":"Marseille","Country":"France"}, {"Name":"Bottom-Dollar Marketse","City":"Tsawassen","Country":"Canada"}, {"Name":"Cactus Comidas para llevar","City":"Buenos Aires","Country":"Argentina"}, {"Name":"Centro comercial Moctezuma","City":"México D.F.","Country":"Mexico"}, {"Name":"Chop-suey Chinese","City":"Bern","Country":"Switzerland"}, {"Name":"Comércio Mineiro","City":"São Paulo","Country":"Brazil"} ] };
    $scope.names=response.records;
    $http.jsonp(
        "http://www.phonegap100.com/appapi.php?a=getPortalList&catid=20&page=1&callback=JSON_CALLBACK"
    ).success(function (data) {
        //alert(data);
        // $scope.names =response.records;
    });
});
app.controller("costController", ["$scope",function($scope) {

    $scope.price=10;


}]);

app.controller('div1Controller',function($scope){
    $scope.text='外部页面';
});
app.controller('div2Controller',function($scope){
    $scope.text='div2Controller';
});
app.controller('div3Controller',function($scope){
    $scope.text='div3Controller';
});

app.controller('div4Controller',['$scope','$routeParams',function($scope,$routeParams){
    console.log($routeParams);
    $scope.id=$routeParams.id;

    $scope.text='div4Controller';
}]);

//module初始化执行操作
app.run(["$rootScope","$timeout",function ($rootScope,$timeout) {
    $rootScope.$on("$routeChangeStart", function (event, next, current) {

        console.log("---------$routeChangeStart-----------------");
        console.log(event);
        console.log(next);
        console.log(current);
        console.log("---------$routeChangeStart--------------end---");
    });
    $rootScope.name="0";
    var  i=0;
    var exitFlag=false;
    var setToutDo=function() {
        if(exitFlag)
            return;
        setTimeout(function () {
            i++;
            $rootScope.$apply(function () {

                $rootScope.name = i;
            });

            setToutDo();


        }, 1000);
    };
    $rootScope.show=function(){
        setToutDo();
    };
    $rootScope.resetDelay=function(len){
        $timeout(function () {
            exitFlag=true;
            $timeout(function () {
                $rootScope.name = 0;
            },1000);

        },len);
    };




}]);