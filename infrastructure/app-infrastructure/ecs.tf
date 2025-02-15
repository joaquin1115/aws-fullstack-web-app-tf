# ECR Repository
resource "aws_ecr_repository" "my_ecr_repo" {
  name                 = "my-ecr-repo"
  image_tag_mutability = "MUTABLE"
  force_delete         = true
  image_scanning_configuration {
    scan_on_push = true
  }
}

# ECS Cluster
resource "aws_ecs_cluster" "main" {
  name = "PrivateIntegrationsCluster"
}

# Task Definition
resource "aws_ecs_task_definition" "service" {
  family                   = "private-integrations-service"
  cpu                      = var.fargate_cpu
  memory                   = var.fargate_memory
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  task_role_arn           = aws_iam_role.ecs_task_role.arn
  execution_role_arn      = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name  = "web"
      image = "${aws_ecr_repository.my_ecr_repo.repository_url}:latest"
      portMappings = [
        {
          containerPort = var.app_port
          protocol      = "tcp"
        }
      ]
      essential = true
      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
          name: "SPRING_PROFILES_ACTIVE",
          value: "prod"
        },
        {
          name: "SPRING_DATASOURCE_URL",
          value: "jdbc:postgresql://${aws_db_instance.appdb.address}:${aws_db_instance.appdb.port}/${aws_db_instance.appdb.db_name}"
        },
        {
          name: "SPRING_DATASOURCE_USERNAME",
          value: "${aws_db_instance.appdb.username}"
        },
        {
          name: "SPRING_DATASOURCE_PASSWORD",
          value: "${var.db_password}"
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs.name
          "awslogs-region"        = data.aws_region.current.name
          "awslogs-stream-prefix" = "PrivateIntegrationsService"
        }
      }
    }
  ])
}

# ECS Service
resource "aws_ecs_service" "main" {
  name            = "private-integrations-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.service.arn
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [aws_subnet.private1.id, aws_subnet.private2.id]
    security_groups  = [aws_security_group.ecs_service.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.main.arn
    container_name   = "web"
    container_port   = var.app_port
  }

  depends_on = [aws_lb_listener.front_end]
}
