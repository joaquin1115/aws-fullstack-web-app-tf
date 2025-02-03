variable "aws_region" {
    description = "The AWS region things are created in"
}

variable "ecs_task_execution_role_name" {
    description = "ECS task execution role name"
    default = "myEcsTaskExecutionRole"
}

variable "ecs_task_role_name" {
    description = "ECS task role name"
    default = "myEcsTaskRole"
}

variable "health_check_path" {
  type = string
  default = "/"
}

variable "app_port" {
    description = "Port exposed by the docker image to redirect traffic to"
    default = 8080
}

variable "app_count" {
    description = "Number of docker containers to run"
    default = 2
}

variable "fargate_cpu" {
    description = "Fargate instance CPU units to provision (1 vCPU = 1024 CPU units)"
    type        = string
    default = "512"
}

variable "fargate_memory" {
    description = "Fargate instance memory to provision (in MiB)"
    type        = string
    default = "2048"
}

variable "environment" {
  default = "test"
}

variable "project" {
  default = "fullstack-web-app"
}

variable "db_name" {
  description = "The name for the database"
  type        = string
}

variable "db_username" {
  description = "The username for the database"
  type        = string
}

variable "db_password" {
  description = "The password for the database"
  type        = string
  sensitive   = true
}

variable "db_port" {
  description = "The port for the database"
  type        = number
  default = 5432
}