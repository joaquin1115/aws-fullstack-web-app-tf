resource "aws_cognito_user_pool" "main" {
  name = "my-app-user-pool"

  password_policy {
    minimum_length    = 8
    require_lowercase = true
    require_numbers   = true
    require_symbols   = true
    require_uppercase = true
  }

  schema {
    name                     = "employee_id"
    attribute_data_type      = "Number"
    developer_only_attribute = false
    mutable                 = false
    required                = false

    number_attribute_constraints {
      min_value = 1
      max_value = 1000000
    }
  }

  schema {
    name                     = "dni"
    attribute_data_type      = "String"
    developer_only_attribute = false
    mutable                 = false
    required                = false

    string_attribute_constraints {
      min_length = 8
      max_length = 8
    }
  }

  schema {
    name                     = "area"
    attribute_data_type      = "String"
    developer_only_attribute = false
    mutable                 = true
    required                = false

    string_attribute_constraints {
      min_length = 1
      max_length = 120
    }
  }

  schema {
    name                     = "position"
    attribute_data_type      = "String"
    developer_only_attribute = false
    mutable                 = true
    required                = false

    string_attribute_constraints {
      min_length = 1
      max_length = 120
    }
  }

  schema {
    name                     = "is_representative"
    attribute_data_type      = "Boolean"
    developer_only_attribute = false
    mutable                 = true
    required                = false
  }

  username_attributes      = ["email"]
  auto_verified_attributes = ["email"]
}

resource "aws_cognito_user_pool_client" "client" {
  name = "my-app-client"

  user_pool_id = aws_cognito_user_pool.main.id

  explicit_auth_flows = [
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH",
    "ALLOW_USER_SRP_AUTH"
  ]
}
