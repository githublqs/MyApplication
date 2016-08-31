/**
 * Created by lqs on 2016/8/30.
 */
app.controller("personController",["$scope","$filter",function($scope,$filter) {
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
    var arrayObj = new Array();
    //arrayObj.push(personCopy);
    /*arrayObj[0]=$scope.personCopy;
    arrayObj[1]=angular.copy($scope.person);
    arrayObj[1].firstName="John1";
    arrayObj[1].lastName="Doe2";
    var ret=$filter('orderBy')(arrayObj,{name:'firstName'});//没成功
    console.log(ret);*/
    var xxx="xxxxxHelloxxxxx";
   $filter('rHello')(xxx);
    console.log(xxx);






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
app.run(["$rootScope","$timeout",function ($rootScope,$timeout) {
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