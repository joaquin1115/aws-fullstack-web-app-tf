resource "aws_db_subnet_group" "appdb" {
  name       = "${var.project}-db-subnet-group-main"
  subnet_ids = [aws_subnet.rds_private1.id, aws_subnet.rds_private2.id]

  tags = {
    "environment" = var.environment
    "Name"        = "${var.project}-db-subnet-group-main"
    "project"     = var.project
  }
}

resource "aws_db_instance" "appdb" {
  identifier     = "${var.project}-db-instance"
  db_name        = var.db_name
  instance_class = "db.t3.micro"

  allocated_storage     = 10
  max_allocated_storage = 0
  storage_type          = "gp2"

  engine              = "postgres"
  engine_version      = "17.2"
  username            = var.db_username
  password            = var.db_password
  port                = var.db_port
  publicly_accessible = false

  allow_major_version_upgrade = false
  auto_minor_version_upgrade  = true
  availability_zone           = data.aws_availability_zones.available.names[0]

  db_subnet_group_name   = aws_db_subnet_group.appdb.id
  vpc_security_group_ids = [aws_security_group.rds_sg.id]

  delete_automated_backups = true

  deletion_protection = false

  enabled_cloudwatch_logs_exports = ["postgresql", "upgrade"]

  skip_final_snapshot = true

  monitoring_interval = 0

  tags = {
    "environment" = var.environment
    "Name"        = "${var.project}-db-instance-main"
    "project"     = var.project
  }
  copy_tags_to_snapshot = true
}