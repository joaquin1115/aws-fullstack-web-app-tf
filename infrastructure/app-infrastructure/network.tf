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

# Internet Gateway
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC"
  }
}

# Public Subnets
resource "aws_subnet" "public1" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.0.0/19"
  availability_zone       = data.aws_availability_zones.available.names[0]
  map_public_ip_on_launch = true

  tags = {
    Name                  = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet1"
    "aws-cdk:subnet-name" = "Public"
    "aws-cdk:subnet-type" = "Public"
  }
}

resource "aws_subnet" "public2" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.32.0/19"
  availability_zone       = data.aws_availability_zones.available.names[1]
  map_public_ip_on_launch = true

  tags = {
    Name                  = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet2"
    "aws-cdk:subnet-name" = "Public"
    "aws-cdk:subnet-type" = "Public"
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
resource "aws_route_table" "public1" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet1"
  }
}

resource "aws_route_table" "public2" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet2"
  }
}

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
resource "aws_route_table_association" "public1" {
  subnet_id      = aws_subnet.public1.id
  route_table_id = aws_route_table.public1.id
}

resource "aws_route_table_association" "public2" {
  subnet_id      = aws_subnet.public2.id
  route_table_id = aws_route_table.public2.id
}

resource "aws_route_table_association" "private1" {
  subnet_id      = aws_subnet.private1.id
  route_table_id = aws_route_table.private1.id
}

resource "aws_route_table_association" "private2" {
  subnet_id      = aws_subnet.private2.id
  route_table_id = aws_route_table.private2.id
}

# NAT Gateway and EIP
resource "aws_eip" "nat1" {
  domain = "vpc"

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet1"
  }
}

resource "aws_eip" "nat2" {
  domain = "vpc"

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet2"
  }
}

resource "aws_nat_gateway" "nat1" {
  allocation_id = aws_eip.nat1.id
  subnet_id     = aws_subnet.public1.id

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet1"
  }
}

resource "aws_nat_gateway" "nat2" {
  allocation_id = aws_eip.nat2.id
  subnet_id     = aws_subnet.public2.id

  tags = {
    Name = "PrivateIntegrationsStack/PrivateIntegrationsTutorialVPC/PublicSubnet2"
  }
}

# Routes
resource "aws_route" "public1_igw" {
  route_table_id         = aws_route_table.public1.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.main.id
}

resource "aws_route" "public2_igw" {
  route_table_id         = aws_route_table.public2.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.main.id
}

resource "aws_route" "private1_nat" {
  route_table_id         = aws_route_table.private1.id
  destination_cidr_block = "0.0.0.0/0"
  nat_gateway_id         = aws_nat_gateway.nat1.id
}

resource "aws_route" "private2_nat" {
  route_table_id         = aws_route_table.private2.id
  destination_cidr_block = "0.0.0.0/0"
  nat_gateway_id         = aws_nat_gateway.nat2.id
}
