var app = angular.module('app', []);

app.controller('TaskCRUDCtrl', [
		'$scope',
		'TaskCRUDService',
		function($scope, TaskCRUDService) {

			$scope.updateTask = function() {
				TaskCRUDService.updateTask($scope.task.id, $scope.task.name,
						$scope.task.description).then(function success(response) {
					$scope.message = 'Task data updated!';
					$scope.errorMessage = '';
				}, function error(response) {
					$scope.errorMessage = 'Error updating task!';
					$scope.message = '';
				});
			}

			$scope.getTask = function() {
				var id = $scope.task.id;
				TaskCRUDService.getTask($scope.task.id).then(
						function success(response) {
							$scope.task = response.data;
							$scope.task.id = id;
							$scope.message = '';
							$scope.errorMessage = '';
						}, function error(response) {
							$scope.message = '';
							if (response.status === 404) {
								$scope.errorMessage = 'Task not found!';
							} else {
								$scope.errorMessage = "Error getting task!";
							}
						});
			}

			$scope.addTask = function() {
				if ($scope.task != null && $scope.task.name) {
					TaskCRUDService
							.addTask($scope.task.name, $scope.task.description)
							.then(function success(response) {
								$scope.message = 'Task added!';
								$scope.errorMessage = '';
							}, function error(response) {
								$scope.errorMessage = 'Error adding task!';
								$scope.message = '';
							});
				} else {
					$scope.errorMessage = 'Please enter a name!';
					$scope.message = '';
				}
			}

			$scope.deleteTask = function() {
				TaskCRUDService.deleteTask($scope.task.id).then(
						function success(response) {
							$scope.message = 'Task deleted!';
							$scope.task = null;
							$scope.errorMessage = '';
						}, function error(response) {
							$scope.errorMessage = 'Error deleting task!';
							$scope.message = '';
						})
			}

			$scope.getAllTasks = function() {
				TaskCRUDService.getAllTasks().then(function success(response) {
					$scope.tasks = response.data._embedded.tasks;
					$scope.message = '';
					$scope.errorMessage = '';
				}, function error(response) {
					$scope.message = '';
					$scope.errorMessage = 'Error getting tasks!';
				});
			}

		} ]);

app.service('TaskCRUDService', [ '$http', function($http) {

	this.getTask = function getTask(taskId) {
		return $http({
			method : 'GET',
			url : 'tasks/' + taskId
		});
	}

	this.addTask = function addTask(name, description) {
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

	this.updateTask = function updateTask(id, name, description) {
		return $http({
			method : 'PATCH',
			url : 'tasks/' + id,
			data : {
				name : name,
				description : description
			}
		})
	}

	this.getAllTasks = function getAllTasks() {
		return $http({
			method : 'GET',
			url : 'tasks'
		});
	}

} ]);