output "alb_dns_name" {
  description = "The DNS name of the load balancer"
  value       = aws_lb.main.dns_name
}

output "service_url" {
  description = "The URL of the service"
  value       = "http://${aws_lb.main.dns_name}"
}

output "api_endpoint" {
  description = "Your API's invoke URL"
  value       = aws_apigatewayv2_api.main.api_endpoint
}

output "frontend_bucket_name" {
  value = aws_s3_bucket.spa.id
}

output "app_domain" {
  value = aws_cloudfront_distribution.app_distribution.domain_name
}

output "db_identifier" {
  value = aws_db_instance.appdb.identifier
}

output "ecs_cluster_name" {
  value = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  value = aws_ecs_service.main.name
}

output "ecs_security_group_id" {
  value = aws_security_group.ecs_service.id
}

output "private_subnet_id" {
  value = aws_subnet.private1.id
}

output "cloudfront_distribution_id" {
  value = aws_cloudfront_distribution.app_distribution.id
}

output "ecr_repository_name" {
  value = aws_ecr_repository.my_ecr_repo.name
}

output "user_pool_id" {
  value = aws_cognito_user_pool.main.id
}

output "user_pool_client_id" {
  value = aws_cognito_user_pool_client.client.id
}

output "ecs_task_execution_role_name" {
  value = aws_iam_role.ecs_task_execution_role.name
}