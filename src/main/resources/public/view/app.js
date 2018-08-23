var app = angular.module('app', []);

app.controller('TaskController', [
		'$scope',
		'TaskService',
		function($scope, TaskCRUDService) {

			$scope.startAddTask = function() {
				console.log("Adding");
				var task = {
					name : "",
					description : "",
					editing : true
				};
				$scope.pendingTasks.push(task);
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
				var all = $scope.pendingTasks.concat($scope.completedTasks);
				for (i in all) {
					var task = all[i];
					if (task.editing) {
						if (task.id != null) {
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
				TaskCRUDService.getCompletedTasks().then(function success(response) {
					$scope.completedTasks = response.data;
					$scope.errorMessage = '';
				}, function error(response) {
					$scope.errorMessage = 'Error getting tasks!';
				});
				TaskCRUDService.getPendingTasks().then(function success(response) {
					$scope.pendingTasks = response.data;
					$scope.errorMessage = '';
				}, function error(response) {
					$scope.errorMessage = 'Error getting tasks!';
				});
			}

		} ]);

app.service('TaskService', [
		'$http',
		function($http) {

			this.getTask = function(taskId) {
				return $http({
					method : 'GET',
					url : 'tasks/' + taskId
				});
			}

			this.createTask = function(name, description) {
				return $http({
					method : 'POST',
					url : 'tasks',
					data : {
						name : name,
						description : description
					}
				});
			}

			this.deleteTask = function(id) {
				return $http({
					method : 'DELETE',
					url : 'tasks/' + id
				})
			}

			this.updateTask = function(id, name, description, created,
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

			this.getAllTasks = function() {
				return $http({
					method : 'GET',
					url : 'tasks'
				});
			}

			this.completeTask = function(id) {
				return $http({
					method : 'PATCH',
					url : 'tasks/' + id + '/complete'
				});
			}

			this.getPendingTasks = function() {
				return $http({
					method : 'GET',
					url : 'tasks/pending'
				});
			}

			this.getCompletedTasks = function() {
				return $http({
					method : 'GET',
					url : 'tasks/completed'
				});
			}

		} ]);