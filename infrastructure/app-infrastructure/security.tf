# Security Groups
resource "aws_security_group" "vpc_link" {
  name_prefix = "private-integrations-vpc-link"
  description = "Security group for the VPC Link"
  vpc_id      = aws_vpc.main.id

  ingress {
    description      = "Allow HTTP in port 80"
    from_port        = 80
    to_port          = 80
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
  }
}

resource "aws_security_group_rule" "vpc_link_egress" {
  type              = "egress"
  description       = "Allow outbound traffic to ALB"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  source_security_group_id = aws_security_group.alb.id
  security_group_id        = aws_security_group.vpc_link.id
}

resource "aws_security_group" "alb" {
  name_prefix = "private-integrations-alb"
  vpc_id      = aws_vpc.main.id
}

resource "aws_security_group_rule" "alb_ingress" {
  type                     = "ingress"
  from_port                = 80
  to_port                  = 80
  protocol                 = "tcp"
  source_security_group_id = aws_security_group.vpc_link.id
  security_group_id        = aws_security_group.alb.id
}

resource "aws_security_group_rule" "alb_egress" {
  type                     = "egress"
  from_port                = var.app_port
  to_port                  = var.app_port
  protocol                 = "tcp"
  source_security_group_id = aws_security_group.ecs_service.id
  security_group_id        = aws_security_group.alb.id
}

resource "aws_security_group" "ecs_service" {
  name_prefix = "private-integrations-ecs"
  vpc_id      = aws_vpc.main.id

  egress {
    description      = "Allow all outbound traffic by default"
    from_port         = 0
    to_port           = 0
    protocol          = "-1"
    cidr_blocks       = ["0.0.0.0/0"]
  }
}

resource "aws_security_group_rule" "ecs_ingress" {
  type                     = "ingress"
  from_port                = var.app_port
  to_port                  = var.app_port
  protocol                 = "tcp"
  source_security_group_id = aws_security_group.alb.id
  security_group_id        = aws_security_group.ecs_service.id
}

resource "aws_security_group" "rds_sg" {
  name = "${var.environment}-rds-sg"
  description = "${var.environment} Security Group"
  vpc_id = aws_vpc.main.id

  // allows traffic from the SG itself
  ingress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      self = true
  }

  //allow traffic for TCP 5432
  ingress {
      from_port = 5432
      to_port   = 5432
      protocol  = "tcp"
      security_groups = ["${aws_security_group.ecs_service.id}"]
  }

  // outbound internet access
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
