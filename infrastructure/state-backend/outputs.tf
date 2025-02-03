output "state_bucket_name" {
  value       = aws_s3_bucket.terraform_state.id
  description = "Name of the S3 bucket for Terraform state"
}

output "state_bucket_arn" {
  value       = aws_s3_bucket.terraform_state.arn
  description = "ARN of the S3 bucket for Terraform state"
}

output "dynamodb_table_name" {
  value       = aws_dynamodb_table.terraform_state_lock.name
  description = "Name of the DynamoDB table for state locking"
}

output "dynamodb_table_arn" {
  value       = aws_dynamodb_table.terraform_state_lock.arn
  description = "ARN of the DynamoDB table for state locking"
}