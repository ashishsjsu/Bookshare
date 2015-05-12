
angular.module("BookShare", ['ui.router', 'ui.bootstrap'])
	.controller("appHome", ["$rootScope", "$scope", "$location", "$state","student", "mapper", "$stateParams", appDashboard])
	.controller("loginController", ["$rootScope", "$scope", "$location", "$http", "$state", "student", LoginStudent])
	.controller("booksController", ["$rootScope", "$scope", "$state","student","getBooks",  "mapper", "resultService", "$stateParams", booksController])
	.controller("bidController", ["$rootScope", "$scope", "$state", "$stateParams","bookToBid", "resultService", bidController])
	
	.factory('student', ['$state', '$http', studentFactory])
	.factory('mapper', ['$state', '$http', mapperFactory])
	.factory('resultService', ['$state', '$http', searchResultFactory])
	
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
	.state('home.bidBook', {
		url: '/home/bidBook/:bookid',
		templateUrl: 'templates/partials/bid.html',
		controller: 'bidController',
		resolve: {
			bookToBid: ['$stateParams', 'mapper', 'resultService',
			           function($stateParams, mapper, resultService){
						
							return resultService;						 
			}]
		}
	})
	.state('home.searchBook', {
		url: '/home/searchBook/:query',
		templateUrl: 'templates/partials/searchBooks.html',
		controller: 'booksController',
		resolve: {
			getBooks: ['$stateParams', 'mapper',
			           function($stateParams, mapper){
						 console.log("Bookscontroller resolve: " + $stateParams.query);
						//this will retrieve the book for search query before the controller is loaded
					   	return mapper.SearchBook($stateParams.query);
			}]
		}//resolve
	
	})
	.state('home.history', {
		url: '/home/history',
		templateUrl: 'templates/partials/history.html',
		controller: 'appHome'
	})
	
	$urlRouterProvider.otherwise('/login');
}


//save book search result in this factory
function searchResultFactory($state, $http){
	
	var results = {};
	
	function setSearchResults(bookResult, success){
		angular.copy(bookResult, results);
		success();
	}
	
	function getSavedSearch(){
		 return results;
	}
	
	return{
		results: results,
		setSearchResults: setSearchResults 
	}
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


function testFactory($http){
	
	var keyval = {};
	
	function saveQueryParams(query, success){		
		keyval = query;
		success();
	}
	
	function getQueryParams(){
		return keyval;
	}
	
	return {
		keyval: keyval,
		saveQueryParams : saveQueryParams,
		getQueryParams : getQueryParams
	}
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
		return $http.get(studentEmail+'/books/')
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
		return $http.get('/books/' + keyval)
		.success(function(response){
			if(mapperObj.mapper != null || mapperObj.mapper != undefined){
				mapperObj.mapper = [];
				console.log("Cleared");
			}
			for(var i=0; i<response.length; i++){
				mapperObj.mapper.push(response[i]);
			}
			return mapperObj.mapper;
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
		$state.go('home.bidBook');
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
function appDashboard($rootScope, $scope, $location, $state, student, mapper, $stateParams){
	
	var mapperService = mapper;
	if(!$rootScope.isAuthenticated){
		$location.path("#/login");
	}
	
	$scope.profile = {};
	$scope.booksList = {};
	
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
	
	$scope.booksList = mapper.mapperObj.mapper;
	
	$scope.myBooks = function(){
		mapperService.ListBook(student.userObj.email);
	}
	
	$scope.searchBook = function(){
		$state.go('home.searchBook', {query : $scope.keyval});
		//$scope.keyval = "";
	}

	//scope  params for adding a book
    $scope.newBook = {};
    $scope.newBook.forBuy = false;
    $scope.newBook.forRent = false;
    $scope.radioModel = 'New';
    
    $scope.booksList = mapper.mapperObj.mapper;		
		
    $scope.addBook = function(){
		
        mapperService.AddBook(student.userObj.email, $scope.newBook);
	}
	
	$scope.loadAddBook = function(){
		mapperService.loadAddBook();
	}
}


function booksController($rootScope, $scope, $state, student, getBooks, mapper, resultService, $stateParams){
	
	//getBooks will return data from service
	$scope.getBooks = getBooks;
	$scope.booksList = mapper.mapperObj.mapper;
	
	$scope.loadBiddingPage = function(book){
		//save the search result in service so that it is accessible for bidding 
		resultService.setSearchResults(book, function(){ $state.go('home.bidBook'); });
	}
	
}


function bidController($rootScope, $scope, $state, $stateParams, bookToBid, resultService){
	
	
	console.log("Bid controller loaded "  + JSON.stringify(resultService.results));
	$scope.book = resultService.results;
}
