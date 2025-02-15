# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "ecs_logs" {
  name              = "/ecs/private-integrations-service"
  retention_in_days = 30
}

# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "ecs_db_migration_logs" {
  name              = "/ecs/db-migration"
  retention_in_days = 30
}

# API Logs
resource "aws_cloudwatch_log_group" "api_logs" {
  name              = "/aws/apigateway/private-integrations-api"
  retention_in_days = 731
}