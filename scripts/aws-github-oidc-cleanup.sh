#!/bin/bash
# cleanup-github-oidc.sh

# Exit on any error
set -e

echo "Starting cleanup..."

# Get AWS account ID
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

# Detach policy from role
echo "Detaching policy from role..."
aws iam detach-role-policy \
    --role-name GitHubAction-AssumeRoleWithAction \
    --policy-arn "arn:aws:iam::${AWS_ACCOUNT_ID}:policy/GitHubActionPermissionsPolicy"

# Delete role
echo "Deleting role..."
aws iam delete-role \
    --role-name GitHubAction-AssumeRoleWithAction

# Delete policy
echo "Deleting policy..."
aws iam delete-policy \
    --policy-arn "arn:aws:iam::${AWS_ACCOUNT_ID}:policy/GitHubActionPermissionsPolicy"

# Delete OIDC provider
echo "Deleting OIDC provider..."
aws iam delete-open-id-connect-provider \
    --open-id-connect-provider-arn "arn:aws:iam::${AWS_ACCOUNT_ID}:oidc-provider/token.actions.githubusercontent.com"

echo "Cleanup complete!"
