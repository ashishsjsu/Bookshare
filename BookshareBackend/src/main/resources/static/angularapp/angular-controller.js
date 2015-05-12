
angular.module("BookShare", ['ui.router', 'ui.bootstrap'])
	.controller("appHome", ["$rootScope", "$scope", "$location", "$state","student", "mapper", "$stateParams", appDashboard])
	.controller("loginController", ["$rootScope", "$scope", "$location", "$http", "$state", "student", LoginStudent])
	.controller("booksController", ["$rootScope", "$scope", "$state","student","getBooks",  "mapper", "resultService", "biddingService", "$stateParams", booksController])
	.controller("booksListController", ["$rootScope", "$scope", "$state", "myBookList", "mapper", "$stateParams", booksListController])
	.controller("bidController", ["$rootScope", "$scope", "$state", "$stateParams", "student","bookToBid", "oldBids", "resultService", "biddingService", bidController])
	
	.factory('student', ['$state', '$http', studentFactory])
	.factory('mapper', ['$state', '$http', mapperFactory])
	.factory('resultService', ['$state', '$http', searchResultFactory])
	.factory('biddingService', ['$state', '$http', biddingFactory])
	
	.config(['$stateProvider', '$urlRouterProvider', appConfigHandler]);

/*========================================== STATES START ============================================*/ 

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
		url: '/home/listBook/:user',
		templateUrl: 'templates/partials/listBooks.html',
		controller: 'booksListController',
		resolve: {
			myBookList: ['$stateParams', 'mapper', 
						function($stateParams, mapper){
							console.log("Books list controller resolve " + $stateParams.user);
							return mapper.ListBook($stateParams.user);
			}]
		}
	})
	.state('home.bidBook', {
		url: '/home/bidBook/:bookId',
		templateUrl: 'templates/partials/bid.html',
		controller: 'bidController',
		resolve: {
			bookToBid: ['$stateParams', 'mapper', 'resultService',
			           function($stateParams, mapper, resultService){
							return resultService;						 
			}],
			oldBids: ['$stateParams', 'biddingService', 
			          function($stateParams, biddingService){
							console.log("home.bidbbok resolve " + $stateParams.bookId);
							//retrieve a list of exsting bids on a book
							return biddingService.listBids($stateParams.bookId);
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

/*========================================== STATES End ================================================*/

/*========================================= SERVICES Start =============================================*/

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
        })
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
		return $http.get(studentEmail+'/books/')
		.success(function(response){
			//angular.copy(response[0], mapperObj);
			if(mapperObj.mapper != null || mapperObj.mapper != undefined){
				mapperObj.mapper = [];
			}
			for(var i=0; i<response.length; i++){
				mapperObj.mapper.push(response[i]);
			}
			return mapperObj.mapper;
			//$state.go('home.listBook');
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
            $state.go('home.listBook', {user : currUser});
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


function biddingFactory($state, $http){
	var bidObj = { 
		bids : [] 
	};
	
	function placeBid(bid){
		
		return $http.post(bid.bidderId+"/bidbook", bid)
		.success(function(response){				
			listBids(bid.bookTitle);
			$state.go('home.bidBook');				
		})
		.error(function(response, status){
			alert("Error placing bid " + response);
		})
	}
	
	function listBids(bookTitle){
		console.log("List bids");
		return $http.get("/book/"+bookTitle+"/bids").success(function(response){
			console.log("Bids retrieved " + JSON.stringify(response));
			if(bidObj.bids != null || bidObj.bids != undefined){
				bidObj.bids = [];
				console.log("Cleared");
			}
			for(var i=0; i<response.length; i++){
				bidObj.bids.push(response[i]);
			}
			console.log("Bids pushed " + JSON.stringify(response));
			
		})
		.error(function(response, status){
			alert("Error getting bids " + response);
		})
	}
	
	return{
		bidObj: bidObj,
		placeBid: placeBid,
		listBids: listBids
	}
}

/*========================================== SERVICES End ==============================================*/


/*========================================== CONTROLLERS ===============================================*/
//loginController callback method
function LoginStudent($rootScope, $scope, $location, $http, $state, studentservice){
	
	var studentService = studentservice;

	//student login function
	 $scope.LoginStudent = function(){
		 
		 studentService.Login(
			$scope.student, 
			function() { 
	 			$rootScope.isAuthenticated = true; 	 			
	 			$http.get("/login").success(function(response) {
	 				angular.copy(response, studentService.userObj);
	 				$state.go("home");
	 			})
	 			.error(function(response, status){
	 				alert("Error retrieving user");
	 			});
			 }, 
			 function() { 
				if(studentService.userObj.error == "LoginErr") {
					$scope.errorMsg = "Login failed. Invalid Email/password combination.";
					$rootScope.authenticated = false;
				}
			 }	
		 )
	}
	 
	 
	 $scope.registerStudent = function(){
		 studentService.registerStudent(
			 $scope.signup, 
			 function(){
			 	studentService.Login(
		 			$scope.signup, 
		 			function(){ 
			 			$rootScope.isAuthenticated = true; 	 			
			 			$http.get("/login").success(function(response){
			 				angular.copy(response, studentService.userObj);
			 				$state.go("home");
			 			})
			 			.error(function(response, status){
			 				alert("Error retrieving user");
			 			});
					}
		 		)
		 	}, 
			function(){
				if(studentService.userObj.error == "LoginErr"){
					$scope.errorMsg = "Login failed. Invalid Email/password combination.";
					$rootScope.authenticated = false;
				}
			}
	 	)
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
	
	$scope.myBooks = function(){
		//mapperService.ListBook(student.userObj.email);
        $state.go('home.listBook', {user : student.userObj.email});
	}
	
	$scope.searchBook = function(){
		$state.go('home.searchBook', {query : $scope.keyval});
		//$scope.keyval = "";
	}
  
    $scope.booksList = mapper.mapperObj.mapper;		
		
    $scope.addBook = function(){		
        mapperService.AddBook(student.userObj.email, $scope.newBook);
	}
	
	$scope.loadAddBook = function(){
		//scope  params for adding a book
	    $scope.newBook = {};
	    $scope.newBook.forBuy = false;
	    $scope.newBook.forRent = false;
	    $scope.new
	    $scope.radioModel = 'New';
	    $scope.newBook.available = true;
		mapperService.loadAddBook();
	}
}

// Controller for Books 
function booksController($rootScope, $scope, $state, student, getBooks, mapper, resultService, biddingService, $stateParams){
	
	//getBooks will return data from service
	$scope.getBooks = getBooks;
	$scope.booksList = mapper.mapperObj.mapper;
	$scope.AuthUser = student.userObj.email;
	
	$scope.loadBiddingPage = function(book){
		//save the search result in service so that it is accessible for bidding 
		resultService.setSearchResults(book, function(){ $state.go('home.bidBook', { bookId : book.bookTitle}); });
	}
}


function booksListController($rootScope, $scope, $state, myBookList, mapper, $stateParams){
	
	//getBooks will return data from service
	console.log("bookListController loaded " + JSON.stringify(myBookList)  + "  " + JSON.stringify(mapper.mapperObj.mapper));
	$scope.booksList = mapper.mapperObj.mapper;
}

// Controller for bidding on Books.
function bidController($rootScope, $scope, $state, $stateParams, student, bookToBid, oldBids, resultService, biddingService){
	
	console.log("Bid controller loaded "  + JSON.stringify(biddingService.bidObj.bids));
	$scope.bids = biddingService.bidObj.bids;
	$scope.book = resultService.results;
	
	$scope.placeBid = function(book){
		var newBid = {"bidPrice" : $scope.bidPrice, "ownerEmail": book.ownerId, "bookId": book.bookId, "bidDate": new Date(), "bookTitle" : book.bookTitle, "basePrice": book.sellPrice, "bidderId": student.userObj.email};
		console.log(JSON.stringify(newBid));
		biddingService.placeBid(newBid);
		$scope.bidPrice = "";
	}
	
	$scope.refreshBids = function(){
		console.log("refresh bids");
		$scope.bids = biddingService.bidObj.bids;
	}
}