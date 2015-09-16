app.controller('chatController', ['$scope', function($scope) {
	
	$scope.title = '> Pollbus | Chat';
	$scope.pipeStr = " | ";
	$scope.sessionInfo = null;
	$scope.user = false;

	
	$scope.userName = "";
	$scope.userPassword = "";
	$scope.loggedIn = false;
	$scope.loggingIn = false;

	$scope.login = function() {
		console.log("OUT: Login called for user: " + $scope.userName);
		$scope.loggingIn = true;
		$scope.chatService.query(
			"/chat-session", "login",					// service + method name to call
			[$scope.userName, $scope.userPassword],		// parameters
			$scope.onLogin								// result handle
		);
	};

	$scope.onLogin = function(result) {
		console.log("IN: Login result: " + result);
		$scope.$apply(function() {
			$scope.loggingIn = false;
			$scope.loggedIn = result;
			$scope.sessionInfo = "Logged in as: " + $scope.userName;
		});
	}

	$scope.onLogin.onfail = function(error) {
		// alert(JSON.stringify(error));
		$scope.$apply(function(){ $scope.loggingIn = false; });
		console.log(error);
	}

	$scope.logout = function() {
		console.log("OUT:Logging out!");
		$scope.chatService.query(
			"/chat-session", "logoff",					// service + method name to call
			[],											// parameters
			$scope.onLogout								// result handle
		);
	}

	$scope.onLogout = function(){
		console.log("IN: Server accepted logout!");
		$scope.$apply(function(){
			$scope.loggedIn = false;
			$scope.userName = null;
			$scope.userPassword = null;
			delete $scope.channelId;
			$scope.channels = {};
			delete $scope.sessionInfo;
		});
	}


	$scope.channels = {};
	// $scope.testChannel = null;

	$scope.channelId = null;

	$scope.joinChannel = function(){
		$scope.chatService.query("/chat-session", "joinChannel", [$scope.channelId], $scope.onJoinChannel);		
	}


	var Chat = {};
	// Model of a channel
	Chat.Channel = function(service, args) {
		
		var _this = this;
		var _chatService = service;

		this.messages = [];
		this.id = args["id"];
		this.users = args["userNames"];
		this.owner = args["owner"];
		this.isSending = false;

		this.msg = function() {
			console.log("OUT: msg():" + this.id);
			_this.isSending = true;
			// $scope.chatService.query("/chat-session", "msg", [this.id, this.message], this.onMessageSend);
			_chatService.query("/chat-session", "msg", [this.id, this.message], this.onMessageSend);
		}
		this.onMessageSend = function(success) {
			console.log("IN: server said sending was success: " + success);
			$scope.$apply(function(){
				_this.isSending = false;
			});
			this.onfail = function(error) {
				console.log("IN: failed to send message: " + error);
			}			
		}

		this.onMessage = function(user, message){
			$scope.$apply(function() {
				_this.messages.push({user: user, txt: message});
			});
		}
		this.onUpdate = function(channelData) {
			// if you wanna more sophisitcated funcionality 
			// USER X has joined etc ..
			// you need to start here 
			// for now just reflect all changes on the channel at once
			$scope.$apply(function() {
				_this.id = channelData["id"];
				_this.users = channelData["userNames"];
				_this.owner = channelData.owner;
			});
		}

	}


	$scope.onJoinChannel = function(channel) {
		console.log("IN: joined Channel!" + JSON.stringify(channel));
		console.log("applying some functions!");
		var chDt = channel; // JSON.eval(channel);
		var ch = new Chat.Channel($scope.chatService, channel);
		console.log("NOW:" + JSON.stringify(ch));
		$scope.$apply(function() {
			$scope.channels[ch.id] = ch;
		});

	}

	$scope.leaveChannel = function() {
		console.log("Leave channel: " + $scope.channelId)
		$scope.chatService.query("/chat-session", "leaveChannel", [$scope.channelId], $scope.onLeaveChannel);
	}
	$scope.onLeaveChannel = function(success) {
		console.log("leaveChannel:" + success);
		$scope.$apply(function() {
			delete $scope.channels[$scope.channelId];
		});
	}

	// <!-- SERVICE CONNECTION -->
	var url = "ws://" + "localhost:8086/" + "s/pod";
	$scope.chatService = new Jamp.BaratineClient(url);
	var chatEvents = $scope.chatService.createListener();
	console.log("CREATED LISTENER: " + JSON.stringify(chatEvents));

	chatEvents.onMessage = function(channelId, messageId, userId, message) {

		// $scope.handleMessage(channelId, userId, message);
		console.log("pollbus-chat-events.on-message:");
		console.log("channel: " + channelId);
		console.log("messageId: " + messageId);
		console.log("userId: " + userId);
		console.log("msg: " + message);

		if($scope.channels[channelId] !== null) {
			$scope.channels[channelId].onMessage(userId, message);
		}
	}

	chatEvents.onChannelUpdate = function(channelId, channelData){
		console.log("pollbus-chat-events.on-channel-update:");
		console.log("channelId: " + channelId);
		console.log("channelDt: " + JSON.stringify(channelData));

		if($scope.channels[channelId]) {
			$scope.channels[channelId].onUpdate(channelData);
		}
	}
	
	chatEvents.onClose = function (channelId) {
		$scope.handleChannelClose(channelId);
	}

	$scope.chatService.onSession = function () {
        console.log("on-session: setting callback on server ... " + JSON.stringify(chatEvents));
        $scope.chatService.query("/chat-session", "setCallback", [chatEvents]);
    };

}]);