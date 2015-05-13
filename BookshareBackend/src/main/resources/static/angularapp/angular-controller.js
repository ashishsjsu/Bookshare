
angular.module("BookShare", ['ui.router', 'ui.bootstrap'])
	.controller("appHome", ["$rootScope", "$scope", "$location", "$state","student", "mapper", "$stateParams", appDashboard])
	.controller("loginController", ["$rootScope", "$scope", "$location", "$http", "$state", "student", LoginStudent])
	.controller("booksController", ["$rootScope", "$scope", "$state","student","getBooks",  "mapper", "resultService", "biddingService", "$stateParams", booksController])
	.controller('browseBooks', ["$rootScope", "$scope", "$state", "student", "biddingService", "browseAll", "mapper", browseBooks])
	.controller("booksListController", ["$rootScope", "$scope", "$state", "myBookList", "biddingService", "mapper", "$stateParams", booksListController])
	.controller("bidController", ["$rootScope", "$scope", "$state", "$stateParams", "student","bookToBid", "oldBids", "resultService", "biddingService", bidController])
	.controller("booksBidController", ["$rootScope", "$scope", "$state", "myBookBidList", "biddingService", "mapper", "$stateParams", booksBidController])
	.controller("transactionsController", ["$scope", "$state", "$stateParams", "purchaseHistory", "mapper", "student", transactionsController])
	
	.factory('student', ['$state', '$stateParams', '$http', studentFactory])
	.factory('mapper', ['$state', '$http', mapperFactory])
	.factory('resultService', ['$state', '$http', searchResultFactory])
	.factory('biddingService', ['$state', '$stateParams', '$http', biddingFactory])
	
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
	.state('home.browseBooks',{
		url: '/home/browse',
		templateUrl: 'templates/partials/searchBooks.html',
		controller: 'browseBooks',
		resolve: {
			browseAll : ['$stateParams', 'mapper', 
			             function($stateParams, mapper){
							console.log("Browse all controller");
							return mapper.ListBook("all");
			}]
		}
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
	.state('home.viewBid', {
		url: '/home/:book/bids',
		templateUrl: 'templates/partials/viewBids.html',
		controller: 'booksBidController',
		resolve: {
			myBookBidList: ['$stateParams', 'biddingService',
				function($stateParams,biddingService){
					console.log("Bid list controller resolve " + $stateParams.book);
					return biddingService.listBids($stateParams.book);
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
		url: '/home/history/:email',
		templateUrl: 'templates/partials/history.html',
		controller: 'transactionsController',
		resolve:{
			purchaseHistory : ['$stateParams', 'mapper', 
			                   function($stateParams, mapper){
								console.log("Getting purchase hostory for " + $stateParams.email);
								return mapper.getHistory($stateParams.email);
			}]
		}
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
function studentFactory($state, $stateParams, $http){
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
			//angular.copy(response, userObj);
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
	
	function logout(success){
		
		return $http.post("/logout",{}).success(function(response){
			console.log("Logged out " + JSON.stringify(response));
			success();
		})
		.error(function(response, status){
			alert("Error logging out! " +  JSON.stringify(response));
		})
	}
	
	function getLoggedInUser(success){
		return $http.get("/login").success(function(response) {
				angular.copy(response, userObj);
				console.log("getLoggedInUser " + JSON.stringify(userObj));	
				success();
			})
			.error(function(response, status){
				alert("Error retrieving user");
			});
	}
	
	return {
		userObj : userObj,
		Login : Login,
		LoadSignup : LoadSignup,
		registerStudent : registerStudent,
		logout: logout,
		getLoggedInUser: getLoggedInUser
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
	
	function getHistory(email){
		return $http.get('/student/'+email+'/history')
		.success(function(response){
			if(mapperObj.mapper != null || mapperObj.mapper != undefined){
				mapperObj.mapper = [];
			}
			for(var i=0; i<response.length; i++){
				mapperObj.mapper.push(response[i]);
			}
			return mapperObj.mapper;
		})
		.error(function(response, status){
    		alert("Error retrieving transactions "  + JSON.stringify(response));

		})
	}
	
	return {
		mapperObj : mapperObj,
		LoadProfile : LoadProfile,
		getHistory : getHistory,
		ListBook : ListBook,
		AddBook : AddBook,
		SearchBook : SearchBook,
		loadAddBook: loadAddBook
	};
}


function biddingFactory($state, $stateParams, $http){
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
			return bidObj.bids;
		})
		.error(function(response, status){
			alert("Error getting bids " + response);
		})
	}
	
	function acceptBid(bid){
		console.log("Accept bid " + bid.bidId + " " + bid.bookTitle);
		return $http.post("/book/"+ bid.bookTitle +"/bid/"+ bid.bidId +"/accept", bid).success(function(response){
			console.log("Bid accepeted " + JSON.stringify(response));
		})
		.error(function(response, status){
			alert("Error accepting bid " + JSON.stringify(response));
		});
	}
	
	function buyBook(email, book){
		
		//var transaction = {"bookTitle": book.bookTitle, "buyer":email, "seller":book.ownerId, "transactionType":"BUY", "transactionDate": new Date(), "sellingPrice" : book.sellPrice};
		console.log("book " +JSON.stringify(book));
		return $http.post("student/"+email+"/book/"+book.bookTitle+"/buy", book).success(function(response){
			console.log("Book bought/sold " + JSON.stringify(response));
			/*$state.transitionTo($state.current, $stateParams, {
			    reload: true,
			    inherit: false,
			    notify: true
			});*/
			$state.go('home.history', {email : email});
		})
		.error(function(response, status){
			alert("Error buying "+ JSON.stringify(response));
		})
	}	
	
	function rentBook(email, book){
		
		console.log("book " +JSON.stringify(book));
		return $http.post("student/"+email+"/book/"+book.bookTitle+"/rent", book).success(function(response){
			console.log("Book rented " + JSON.stringify(response));
			/*$state.transitionTo($state.current, $stateParams, {
			    reload: true,
			    inherit: false,
			    notify: true
			});*/
			$state.go('home.history', {email : email});
		})
		.error(function(response, status){
			alert("Error buying "+ JSON.stringify(response));
		})
	}	
	
	return{
		bidObj: bidObj,
		placeBid: placeBid,
		listBids: listBids,
		acceptBid: acceptBid,
		buyBook: buyBook,
		rentBook: rentBook
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
	 				console.log("response " + JSON.stringify(response));
	 				angular.copy(response, studentService.userObj);
	 				localStorage.setItem("usersession", response);
	 				console.log("studentService.userObj " + JSON.stringify(studentService.userObj));
	 				$state.go("home.browseBooks");
	 			})
	 			.error(function(response, status){
	 				alert("Error retrieving user");
	 			});
			 }, 
			 function() { 
				if(studentService.userObj.error == "LoginErr") {
					$scope.errorMsg = "Login failed. Invalid Email/password combination.";
					$rootScope.authenticated = false;
					$scope.student.email = "";
					$scope.student.password = "";					
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
			 				localStorage.setItem("usersession", {'firstName' : response.firstName});
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
	
	//$scope.profile = {};
	//$scope.booksList = {};
	console.log(JSON.stringify(student.userObj));
	
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
	else{
			console.log('Getting logged in user ' + JSON.stringify(localStorage.getItem('usersession')));
		$scope.student = localStorage.getItem('usersession').firstName;
	}
	
	$scope.logout = function(){
		student.logout(function(){
			$rootScope.isAuthenticated = false;
			localStorage.removeItem("session");
			localStorage.removeItem("usersession");			
			student.userObj = null;
			$location.path("#/login");
		});
		
		
	}
	
	$scope.profile = function(){
		mapperService.LoadProfile();
	}
	
	$scope.history = function(){
		$state.go('home.history', {email :  student.userObj.email});
	}
	
	$scope.myBooks = function(){
		//mapperService.ListBook(student.userObj.email);
        $state.go('home.listBook', {user : student.userObj.email});
	}
	
	$scope.searchBook = function(){
		$state.go('home.searchBook', {query : $scope.keyval});
		//$scope.keyval = "";
	}
  
    //$scope.booksList = mapper.mapperObj.mapper;		
		
    $scope.addBook = function(){		
        mapperService.AddBook(student.userObj.email, $scope.newBook);
	}
	
	$scope.loadAddBook = function(){
		//scope  params for adding a book
	    $scope.newBook = {};
	    $scope.newBook.forBuy = false;
	    $scope.newBook.forRent = false;
	    $scope.radioModel = 'New';
	    $scope.newBook.available = true;
		mapperService.loadAddBook();
	}
	
	$scope.browseBooks = function(){
		$state.go('home.browseBooks');
	}
	
	
}

// Controller for Books 
function booksController($rootScope, $scope, $state, student, getBooks, mapper, resultService, biddingService, $stateParams){
	
	//getBooks will return data from service
	$scope.keyval = "";
	$scope.getBooks = getBooks;
	if(mapper.mapperObj.mapper != null || mapper.mapperObj.mapper != undefined){
		$scope.booksList = mapper.mapperObj.mapper;
	}

	$scope.AuthUser = student.userObj.email;
	
	$scope.loadBiddingPage = function(book){
		//save the search result in service so that it is accessible for bidding 
		resultService.setSearchResults(book, function(){ $state.go('home.bidBook', { bookId : book.bookTitle}); });
	}
	
	$scope.buyBook = function(book){
		biddingService.buyBook(student.userObj.email, book);
	}
	
	$scope.rentBook = function(book){
		console.log("RENT");
		biddingService.rentBook(student.userObj.email, book);
	}
	
}


function booksListController($rootScope, $scope, $state, myBookList, biddingService, mapper, $stateParams){
	
	//getBooks will return data from service
	console.log("bookListController loaded " + JSON.stringify(myBookList)  + "  " + JSON.stringify(mapper.mapperObj.mapper));

	$scope.booksList = mapper.mapperObj.mapper;
	//$scope.bidsList = biddingService.bidObj.bids;
	
	$scope.displayBids = function(bookName){
		console.log('bids clicked');
		$state.go("home.viewBid", {book:bookName});
	}
}

function booksBidController($rootScope, $scope, $state, myBookBidList, biddingService, mapper, $stateParams){
	
	if(biddingService.bidObj != null || biddingService.bidObj != undefined){
			$scope.bidsList = biddingService.bidObj.bids;
	}
	//bind book title
	$scope.bookTitle = $stateParams.book;
	$scope.acceptBid = function(bid){
		console.log("Accept bid " + JSON.stringify(bid));
		biddingService.acceptBid(bid);
	}
}

// Controller for bidding on Books.
function bidController($rootScope, $scope, $state, $stateParams, student, bookToBid, oldBids, resultService, biddingService){
	
	if(biddingService != undefined){
		if(biddingService.bidObj != undefined){
				console.log("Bid controller loaded "  + JSON.stringify(biddingService.bidObj.bids));
				$scope.bids = biddingService.bidObj.bids;
		}			
	}
	$scope.book = resultService.results;
	
	$scope.placeBid = function(book){
		var newBid = {"bidPrice" : $scope.bidPrice, "ownerEmail": book.ownerId, "bookId": book.bookId, "bidDate": new Date(), "bookTitle" : book.bookTitle, "basePrice": book.sellPrice, "bidderId": student.userObj.email, "status": "Open"};
		console.log(JSON.stringify(newBid));
		biddingService.placeBid(newBid);
		$scope.bidPrice = "";
	}
	
	$scope.refreshBids = function(){
		console.log("refresh bids");
		$scope.bids = biddingService.bidObj.bids;
	}
}

function browseBooks($rootScope, $scope, $state, student, biddingService, browseAll, mapper)
{
	$scope.AuthUser = student.userObj.email;

	console.log("Browse books controller loaded " + JSON.stringify(mapper.mapperObj));
	$scope.booksList = mapper.mapperObj.mapper;
	$scope.buyBook = function(book){
		biddingService.buyBook(student.userObj.email, book);
	}
	$scope.rentBook = function(book){
		console.log("RENT");
		biddingService.rentBook(student.userObj.email, book);
	}
}


function transactionsController($scope, $state, $stateParams, purchaseHistory, mapper, student){
	
	console.log("Transactions controller loaded " + JSON.stringify(mapper.mapperObj.mapper));
	$scope.resultSet = mapper.mapperObj.mapper;
}