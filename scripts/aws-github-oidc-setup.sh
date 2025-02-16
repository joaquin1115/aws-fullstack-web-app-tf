#!/bin/bash

# Configuration file to store settings
CONFIG_FILE="github_oidc_config"

# Function to prompt for role name and repo if not set
function prompt_for_settings() {
    read -p "Enter the IAM Role name (current: ${ROLE_NAME:-GitHubAction-AssumeRoleWithAction}): " input_role
    ROLE_NAME=${input_role:-$ROLE_NAME}

    read -p "Enter the GitHub repository (org/repo) (current: ${GITHUB_REPO:-joaquin1115/aws-fullstack-web-app-tf}): " input_repo
    GITHUB_REPO=${input_repo:-$GITHUB_REPO}

    # Save settings
    echo "ROLE_NAME=$ROLE_NAME" > "$CONFIG_FILE"
    echo "GITHUB_REPO=$GITHUB_REPO" >> "$CONFIG_FILE"
}

# Load saved settings if they exist
if [ -f "$CONFIG_FILE" ]; then
    source "$CONFIG_FILE"
else
    prompt_for_settings
fi

# Function to create IAM Role and OIDC setup
function create_github_oidc() {
    echo "Creating OpenID Connect provider..."
    OIDC_PROVIDER_ARN=$(aws iam create-open-id-connect-provider \
        --url "https://token.actions.githubusercontent.com" \
        --thumbprint-list "6938fd4d98bab03faadb97b34396831e3780aea1" \
        --client-id-list "sts.amazonaws.com" \
        --query "OpenIDConnectProviderArn" \
        --output text)
    echo "OIDC Provider ARN: $OIDC_PROVIDER_ARN"

    cat > trustpolicyforGitHubOIDC.json << EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "Federated": "${OIDC_PROVIDER_ARN}"
            },
            "Action": "sts:AssumeRoleWithWebIdentity",
            "Condition": {
                "StringLike": {
                    "token.actions.githubusercontent.com:sub": "repo:${GITHUB_REPO}:*"
                },
                "ForAllValues:StringEquals": {
                    "token.actions.githubusercontent.com:iss": "https://token.actions.githubusercontent.com",
                    "token.actions.githubusercontent.com:aud": "sts.amazonaws.com"
                }
            }
        }
    ]
}
EOF

    echo "Creating IAM role..."
    ROLE_ARN=$(aws iam create-role \
        --role-name "$ROLE_NAME" \
        --assume-role-policy-document file://trustpolicyforGitHubOIDC.json \
        --query 'Role.Arn' \
        --output text)
    echo "Role ARN: $ROLE_ARN"

    POLICY_ARN=""
    if [ -f "permissionspolicyforGitHubOIDC.json" ]; then
        echo "Creating IAM policy from existing permissions file..."
        POLICY_ARN=$(aws iam create-policy \
            --policy-name "${ROLE_NAME}-PermissionsPolicy" \
            --policy-document file://permissionspolicyforGitHubOIDC.json \
            --query 'Policy.Arn' \
            --output text)
    else
        echo "No permissions file found. Assigning AdministratorAccess managed policy."
        POLICY_ARN="arn:aws:iam::aws:policy/AdministratorAccess"
    fi
    echo "Policy ARN: $POLICY_ARN"

    echo "Attaching policy to role..."
    aws iam attach-role-policy \
        --role-name "$ROLE_NAME" \
        --policy-arn "$POLICY_ARN"

    echo "Setup complete!"

    # Cleanup temporary files
    rm -f trustpolicyforGitHubOIDC.json
}

# Function to clean up resources
function cleanup_github_oidc() {
    echo "Starting cleanup..."
    AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

    echo "Detaching policy from role..."
    aws iam detach-role-policy \
        --role-name "$ROLE_NAME" \
        --policy-arn "arn:aws:iam::${AWS_ACCOUNT_ID}:policy/${ROLE_NAME}-PermissionsPolicy" || true

    echo "Deleting role..."
    aws iam delete-role --role-name "$ROLE_NAME" || true

    echo "Deleting policy..."
    aws iam delete-policy \
        --policy-arn "arn:aws:iam::${AWS_ACCOUNT_ID}:policy/${ROLE_NAME}-PermissionsPolicy" || true

    echo "Deleting OIDC provider..."
    aws iam delete-open-id-connect-provider \
        --open-id-connect-provider-arn "arn:aws:iam::${AWS_ACCOUNT_ID}:oidc-provider/token.actions.githubusercontent.com" || true

    echo "Cleanup complete!"
}

# Display menu
while true; do
    echo "\nSelect an option:"
    echo "1) Set up GitHub Actions IAM Role"
    echo "2) Clean up resources"
    echo "3) Change settings"
    echo "4) Exit"
    read -p "Enter choice [1-4]: " choice

    case $choice in
        1) create_github_oidc ;;
        2) cleanup_github_oidc ;;
        3) prompt_for_settings ;;
        4) exit 0 ;;
        *) echo "Invalid option. Please choose again." ;;
    esac
done
