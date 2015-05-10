
angular.module("BookShare", ['ui.router', 'ui.bootstrap'])
	.controller("appHome", ["$rootScope", "$scope", "$location", "student", "mapper", "$stateParams", appDashboard])
	.controller("loginController", ["$rootScope", "$scope", "$location", "$http", "$state", "student", LoginStudent])
	.controller("signupController", ["$scope", SignupStudent])
	
	.factory('student', ['$state', '$http', studentFactory])
	.factory('mapper', ['$state', '$http', mapperFactory])
	
	.config(['$stateProvider', '$urlRouterProvider', appConfigHandler]);

function appConfigHandler($stateProvider, $urlRouterProvider){
	$stateProvider
    	.state('login', {
		url : '/login',
		templateUrl : '/templates/login.html',
		controller : 'loginController'
	})
	.state('register', {
		url : '/register',
		templateUrl : '/templates/register.html',
		controller : 'loginController'
	})
	.state('home', {
		url : '/home',
		templateUrl : '/templates/dashboard.html',
		controller : 'appHome'
	})
	.state('home.profile', {
		url: '/home/profile',
		templateUrl: 'templates/partials/profile.html',
		controller: 'appHome'
	})
	.state('home.addBook', {
		url: '/home/addBook',
		templateUrl: 'templates/partials/addBook.html',
		controller: 'appHome'
	})
	.state('home.listBook', {
		url: '/home/listBook',
		templateUrl: 'templates/partials/listBooks.html',
		controller: 'appHome'
	})
	.state('home.history', {
		url: '/home/history',
		templateUrl: 'templates/partials/history.html',
		controller: 'appHome'
	});
    
	$urlRouterProvider.otherwise('/login');
}

//student factory callback method
function studentFactory($state, $http){
	var userObj = {
		user : []
	};
	
	function LoadSignup(){
		$state.go('register');
	}

	function Login(student, success, failure){
		
        return $http.post("/login", "username=" + student.email +
                "&password=" + student.password, {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                } )
		.success(function(response){
			 
			localStorage.setItem("session", {});		 
			angular.copy(response, userObj);
			success();
		
		})
		.error(function(response, status){
			alert("Login failed " + JSON.stringify(response));
			angular.copy({'error':'LoginErr'}, userObj);
			failure();
		});
	}
	
	function registerStudent(newstudent, success, failure){
		
		return $http.post("/student", newstudent)
		.success(function(response){
			console.log("Student registered " + JSON.stringify(response));
			success();
		})
		.error(function(response, status){
			console.log("Student registration error  " + JSON.stringify(response) + " " +status);
			failure();
		});
	}
	
	return {
		userObj : userObj,
		Login : Login,
		LoadSignup : LoadSignup,
		registerStudent : registerStudent
	};
}


//mapper factory callback method
function mapperFactory($state, $http){
	var mapperObj = {
		mapper : []
	};
	
	function LoadProfile(){
		$state.go('home.profile');
	}
	
	function loadAddBook(){
		$state.go('home.addBook');
	}
	
	function ListBook(studentEmail){
		return $http.get('/listUserbooks/' + studentEmail)
		.success(function(response){
			//angular.copy(response[0], mapperObj);
			if(mapperObj.mapper != null || mapperObj.mapper != undefined){
				mapperObj.mapper = [];
			}
			for(var i=0; i<response.length; i++){
				mapperObj.mapper.push(response[i]);
			}
			
			$state.go('home.listBook');
		})
		.error(function(response, status){
			alert("Error gettin books " + response);
		});
	}
	
	function SearchBook(keyval){
		return $http.get('/searchbook/' + keyval)
		.success(function(response){
			console.log("Logged user search : " + JSON.stringify(response));
			if(mapperObj.mapper != null || mapperObj.mapper != undefined){
				mapperObj.mapper = [];
			}
			for(var i=0; i<response.length; i++){
				mapperObj.mapper.push(response[i]);
			}
			$state.go('home.listBook');
		})
		.error(function(response, status){
			console.log("Login POST error: " + response + " status " + status);
		});
	}
	
	function AddBook(currUser, book){
		return $http.post("/"+ currUser +"/book", book)
		.success(function(response){
            console.log("New book added " + JSON.stringify(response));
            $state.go('home.listBook');
        })
        .error(function(response, status){
            alert("Error adding a book "  + JSON.stringify(response));
        })
	}
	
	function History(){
		$state.go('home.profile');
	}
	
	return {
		mapperObj : mapperObj,
		LoadProfile : LoadProfile,
		History : History,
		ListBook : ListBook,
		AddBook : AddBook,
		SearchBook : SearchBook,
		loadAddBook: loadAddBook
	};
}

//register callback method
function SignupStudent($scope){

	
}


//loginController callback method
function LoginStudent($rootScope, $scope, $location, $http, $state, studentservice){
	
	var studentService = studentservice;

//student login function
	 $scope.LoginStudent = function(){
		 
		 studentService.Login($scope.student, function()
				 { 
			 			$rootScope.isAuthenticated = true; 	 			
			 			$http.get("/login").success(function(response){
			 				angular.copy(response, studentService.userObj);
			 				$state.go("home");
			 			})
			 			.error(function(response, status){
			 				alert("Error retrieving user");
			 			});
				 }, 
				 function(){ 
			 
						if(studentService.userObj.error == "LoginErr")
						{
							$scope.errorMsg = "Login failed. Invalid Email/password combination.";
							$rootScope.authenticated = false;
						}			 
		 })
	}
	 
	 
	 $scope.registerStudent = function(){
		 
		 studentService.registerStudent($scope.signup, function(){
			 
			 studentService.Login($scope.signup, function(){ 
	 			$rootScope.isAuthenticated = true; 	 			
	 			$http.get("/login").success(function(response){
	 				angular.copy(response, studentService.userObj);
	 				$state.go("home");
	 			})
	 			.error(function(response, status){
	 				alert("Error retrieving user");
	 			});
			})
		 }, 
		 function(){
			 if(studentService.userObj.error == "LoginErr"){
				$scope.errorMsg = "Login failed. Invalid Email/password combination.";
				$rootScope.authenticated = false;
			 }
		 })
	 }

	 
	 $scope.loadSignup = function(){
		 console.log("Signup controller");
		 studentService.LoadSignup()
	 }
}

//appHome
function appDashboard($rootScope, $scope, $location, student, mapper, $stateParams){
	
	var mapperService = mapper;
	if(!$rootScope.isAuthenticated){
		$location.path("#/login");
	}
	
	$scope.profile = {};
	
	if(student.userObj != null || student.userObj != undefined){ 
		//set scope in Dashboard
		$scope.student = student.userObj.firstName; 
		//set scope in profile partial page
		$scope.FirstName = student.userObj.firstName;
		$scope.LastName = student.userObj.lastName;
		$scope.Email = student.userObj.email;
		$scope.Phone = student.userObj.phone;
		$scope.University = student.userObj.university;
			
	}
	$scope.booksList = mapper.mapperObj.mapper;
		
	console.log("Book scope :" + JSON.stringify(mapper.mapperObj) +  " " + JSON.stringify($scope.booksList.mapper));
	
	$scope.logout = function(){
		
		$rootScope.isAuthenticated = false;
		localStorage.removeItem("session");
		student.userObj = null;
		$location.path("#/login");
	}
	
	$scope.profile = function(){
		mapperService.LoadProfile();
	}
	
	$scope.history = function(){
		mapperService.History();
	}
	
	$scope.myBooks = function(){
		mapperService.ListBook(student.userObj.email);
	}
	
	$scope.searchBook = function(){
		mapperService.SearchBook($scope.keyval);
		$scope.keyval = "";
	}

	//scope  params for adding a book
    $scope.newBook = {};
    $scope.newBook.forBuy = false;
    $scope.newBook.forRent = false;
    $scope.radioModel = 'New';
    
    $scope.booksList = mapper.mapperObj.mapper;		
	console.log("Book scope :" + JSON.stringify(mapper.mapperObj) +  " " + JSON.stringify($scope.booksList.mapper));
	
	$scope.addBook = function(){
		
        console.log("Calling Addbook " + student.userObj.email + " " +JSON.stringify($scope.newBook));      
        mapperService.AddBook(student.userObj.email, $scope.newBook);
	}
	
	$scope.loadAddBook = function(){
		mapperService.loadAddBook();
	}
}
