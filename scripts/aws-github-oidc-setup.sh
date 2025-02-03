#!/bin/bash
# create-github-oidc.sh

# Exit on any error
set -e

echo "Creating OpenID Connect provider..."
OIDC_PROVIDER_ARN=$(aws iam create-open-id-connect-provider \
    --url "https://token.actions.githubusercontent.com" \
    --thumbprint-list "6938fd4d98bab03faadb97b34396831e3780aea1" \
    --client-id-list "sts.amazonaws.com" \
    --query "OpenIDConnectProviderArn" \
    --output text)

echo "OIDC Provider ARN: $OIDC_PROVIDER_ARN"

# Create trust policy JSON with the correct OIDC Provider ARN
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
                    "token.actions.githubusercontent.com:sub": "repo:joaquin1115/aws-fullstack-web-app-tf:*"
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

# Create permissions policy JSON (you'll need to customize this based on your needs)
cat > permissionspolicyforGitHubOIDC.json << EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "*",
            "Resource": "*"
        }
    ]
}
EOF

echo "Creating IAM role..."
ROLE_ARN=$(aws iam create-role \
    --role-name GitHubAction-AssumeRoleWithAction \
    --assume-role-policy-document file://trustpolicyforGitHubOIDC.json \
    --query 'Role.Arn' \
    --output text)

echo "Role ARN: $ROLE_ARN"

echo "Creating IAM policy..."
POLICY_ARN=$(aws iam create-policy \
    --policy-name GitHubActionPermissionsPolicy \
    --policy-document file://permissionspolicyforGitHubOIDC.json \
    --query 'Policy.Arn' \
    --output text)

echo "Policy ARN: $POLICY_ARN"

echo "Attaching policy to role..."
aws iam attach-role-policy \
    --role-name GitHubAction-AssumeRoleWithAction \
    --policy-arn "$POLICY_ARN"

echo "Setup complete!"

# Cleanup temporary files
rm -f trustpolicyforGitHubOIDC.json permissionspolicyforGitHubOIDC.json
