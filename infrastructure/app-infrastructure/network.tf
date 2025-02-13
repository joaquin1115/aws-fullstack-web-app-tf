# VPC Configuration
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  instance_tenancy     = "default"

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC"
  }
}

# Private Subnets for ECS
resource "aws_subnet" "private1" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.64.0/19"
  availability_zone       = data.aws_availability_zones.available.names[0]
  map_public_ip_on_launch = false

  tags = {
    Name                  = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PrivateSubnet1"
    "aws-cdk:subnet-name" = "Private"
    "aws-cdk:subnet-type" = "Private"
  }
}

resource "aws_subnet" "private2" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.96.0/19"
  availability_zone       = data.aws_availability_zones.available.names[1]
  map_public_ip_on_launch = false

  tags = {
    Name                  = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PrivateSubnet2"
    "aws-cdk:subnet-name" = "Private"
    "aws-cdk:subnet-type" = "Private"
  }
}

# Private Subnets for RDS
resource "aws_subnet" "rds_private1" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.128.0/19"
  availability_zone       = data.aws_availability_zones.available.names[0]
  map_public_ip_on_launch = false

  tags = {
    Name                  = "RDS/PrivateSubnet1"
    "aws-cdk:subnet-name" = "Private"
    "aws-cdk:subnet-type" = "Private"
  }
}

resource "aws_subnet" "rds_private2" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.160.0/19"
  availability_zone       = data.aws_availability_zones.available.names[1]
  map_public_ip_on_launch = false

  tags = {
    Name                  = "RDS/PrivateSubnet2"
    "aws-cdk:subnet-name" = "Private"
    "aws-cdk:subnet-type" = "Private"
  }
}

# Route Tables
resource "aws_route_table" "private1" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PrivateSubnet1"
  }
}

resource "aws_route_table" "private2" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PrivateSubnet2"
  }
}

# Route Table Associations
resource "aws_route_table_association" "private1" {
  subnet_id      = aws_subnet.private1.id
  route_table_id = aws_route_table.private1.id
}

resource "aws_route_table_association" "private2" {
  subnet_id      = aws_subnet.private2.id
  route_table_id = aws_route_table.private2.id
}

#VPC endpoints
resource "aws_vpc_endpoint" "ecr_dkr" {
  vpc_id              = aws_vpc.main.id
  service_name        = "com.amazonaws.${var.aws_region}.ecr.dkr"
  vpc_endpoint_type   = "Interface"
  private_dns_enabled = true

  security_group_ids = [aws_security_group.vpc_endpoints.id]
  subnet_ids         = [aws_subnet.private1.id, aws_subnet.private2.id]

  tags = {
    "Name" = "${var.environment}-ecr-dkr"
  }
}

resource "aws_vpc_endpoint" "ecr_api" {
  vpc_id              = aws_vpc.main.id
  service_name        = "com.amazonaws.${var.aws_region}.ecr.api"
  vpc_endpoint_type   = "Interface"
  private_dns_enabled = true

  security_group_ids = [aws_security_group.vpc_endpoints.id]
  subnet_ids         = [aws_subnet.private1.id, aws_subnet.private2.id]

  tags = {
    "Name" = "${var.environment}-ecr-api"
  }
}

resource "aws_vpc_endpoint" "s3" {
  vpc_id            = aws_vpc.main.id
  service_name      = "com.amazonaws.${var.aws_region}.s3"
  vpc_endpoint_type = "Gateway"

  route_table_ids = [aws_route_table.private1.id, aws_route_table.private2.id]

  tags = {
    "Name" = "${var.environment}-s3"
  }
}

resource "aws_vpc_endpoint" "cloudwatch_logs" {
  vpc_id              = aws_vpc.main.id
  service_name        = "com.amazonaws.${var.aws_region}.logs"
  vpc_endpoint_type   = "Interface"
  private_dns_enabled = true

  security_group_ids = [aws_security_group.vpc_endpoints.id]
  subnet_ids         = [aws_subnet.private1.id, aws_subnet.private2.id]

  tags = {
    "Name" = "${var.environment}-cloudwatch-logs"
  }
}
