# API Gateway HTTP API
resource "aws_apigatewayv2_api" "main" {
  name          = "private-integrations-tutorial"
  protocol_type = "HTTP"
}

# VPC Link
resource "aws_apigatewayv2_vpc_link" "main" {
  name               = "private-integrations-vpc-link"
  security_group_ids = [aws_security_group.vpc_link.id]
  subnet_ids         = [aws_subnet.private1.id, aws_subnet.private2.id]
}

# API Integration
resource "aws_apigatewayv2_integration" "main" {
  api_id           = aws_apigatewayv2_api.main.id
  integration_type = "HTTP_PROXY"
  
  connection_type      = "VPC_LINK"
  connection_id        = aws_apigatewayv2_vpc_link.main.id
  integration_uri      = aws_lb_listener.front_end.arn
  integration_method   = "ANY"
  
  payload_format_version = "1.0"
}

# API Authorizer
resource "aws_apigatewayv2_authorizer" "auth" {
  api_id           = aws_apigatewayv2_api.main.id
  authorizer_type  = "JWT"
  identity_sources = ["$request.header.Authorization"]
  name             = "cognito-authorizer"

  jwt_configuration {
    audience = [aws_cognito_user_pool_client.client.id]
    issuer   = "https://cognito-idp.${var.aws_region}.amazonaws.com/${aws_cognito_user_pool.main.id}"
  }
}

# API Route
resource "aws_apigatewayv2_route" "main" {
  api_id    = aws_apigatewayv2_api.main.id
  route_key = "ANY /{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.main.id}"
  authorization_type = "JWT"
  authorizer_id = aws_apigatewayv2_authorizer.auth.id
}

# API Stage
resource "aws_apigatewayv2_stage" "main" {
  api_id      = aws_apigatewayv2_api.main.id
  name        = "$default"
  auto_deploy = true

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_logs.arn
    format         = "$context.identity.sourceIp - - [$context.requestTime] \"$context.httpMethod $context.routeKey $context.protocol\" $context.status $context.responseLength $context.requestId"
  }
}
