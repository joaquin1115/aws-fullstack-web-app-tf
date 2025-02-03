# Application Load Balancer
resource "aws_lb" "main" {
  name               = "private-integrations-tutorial"
  internal           = true
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets           = [aws_subnet.private1.id, aws_subnet.private2.id]

  enable_deletion_protection = false
}

# ALB Listener
resource "aws_lb_listener" "front_end" {
  load_balancer_arn = aws_lb.main.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.main.arn
  }
}

# Target Group
resource "aws_lb_target_group" "main" {
  name        = "private-integrations-tg"
  port        = var.app_port
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.main.id

  health_check {
    enabled = true
    path    = var.health_check_path
  }
}