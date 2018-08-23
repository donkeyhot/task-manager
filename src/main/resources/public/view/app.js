var app = angular.module('app', []);

app.controller('TaskCRUDCtrl', [
		'$scope',
		'TaskCRUDService',
		function($scope, TaskCRUDService) {

			$scope.startAddTask = function() {
				console.log("Adding");
				var task = {
					name : "",
					description : "",
					editing : true
				};
				$scope.allTasks.push(task);
			}

			$scope.startEditTask = function(task) {
				task.editing = true;
				console.log("Editing " + angular.toJson(task))
			}

			$scope.deleteTask = function(task) {
				console.log("Deleting " + angular.toJson(task));
				TaskCRUDService.deleteTask(task.id).then(function success(response) {
					$scope.refreshAll();
					$scope.errorMessage = '';
				}, function error(response) {
					$scope.errorMessage = 'Error deleting task!';
				})
			}

			$scope.completeTask = function(task) {
				console.log("Completing " + angular.toJson(task));
				TaskCRUDService.completeTask(task.id).then(function success(response) {
					$scope.errorMessage = '';
					$scope.refreshAll();
				}, function error(response) {
					$scope.errorMessage = 'Error completing task!';
				});
			}

			$scope.submitEditing = function() {
				for (i in $scope.allTasks) {
					var task = $scope.allTasks[i];
					if (task.editing) {
						if (task.id != null){
							console.log("Updating " + angular.toJson(task));
							TaskCRUDService.updateTask(task.id, task.name, task.description,
									task.created, task.completed).then(
									function success(response) {
										$scope.errorMessage = '';
										$scope.refreshAll();
									}, function error(response) {
										$scope.errorMessage = 'Error updating task!';
									});
						} else {
							console.log("Creating " + angular.toJson(task));
							TaskCRUDService.createTask(task.name, task.description).then(
									function success(response) {
										$scope.errorMessage = '';
										$scope.refreshAll();
									}, function error(response) {
										$scope.errorMessage = 'Error creating task!';
									});
						}

					}
				}
			}

			$scope.refreshAll = function() {
				TaskCRUDService.getAllTasks().then(function success(response) {
					$scope.allTasks = response.data;
					$scope.enabledEdit = [];
					$scope.errorMessage = '';
				}, function error(response) {
					$scope.errorMessage = 'Error getting tasks!';
				});
			}

		} ]);

app.service('TaskCRUDService', [
		'$http',
		function($http) {

			this.getTask = function getTask(taskId) {
				return $http({
					method : 'GET',
					url : 'tasks/' + taskId
				});
			}

			this.createTask = function addTask(name, description) {
				return $http({
					method : 'POST',
					url : 'tasks',
					data : {
						name : name,
						description : description
					}
				});
			}

			this.deleteTask = function deleteTask(id) {
				return $http({
					method : 'DELETE',
					url : 'tasks/' + id
				})
			}

			this.updateTask = function updateTask(id, name, description, created,
					completed) {
				return $http({
					method : 'PATCH',
					url : 'tasks/' + id,
					data : {
						name : name,
						description : description,
					}
				})
			}

			this.getAllTasks = function getAllTasks() {
				return $http({
					method : 'GET',
					url : 'tasks'
				});
			}

			this.completeTask = function completeTask(id) {
				return $http({
					method : 'PATCH',
					url : 'tasks/' + id + '/complete'
				});
			}

			this.getPendingTasks = function getPendingTasks() {
				return $http({
					method : 'GET',
					url : 'tasks/pending'
				});
			}

		} ]);