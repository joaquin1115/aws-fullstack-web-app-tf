import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withFetch } from '@angular/common/http';

import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideAnimations } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(withFetch()),
    provideAnimations(),
  ]
};

export const awsConfig = {
  Auth: {
    Cognito: {
      userPoolId: process.env['COGNITO_USER_POOL_ID'] ?? '',
      userPoolClientId: process.env['COGNITO_USER_POOL_CLIENT_ID'] ?? '',
      loginWith: {
        email: true,
      },
      signUpVerificationMethod: "code",
      userAttributes: {
        email: {
          required: true,
        },
        "custom:employee_id": {
          required: false,
        },
        "custom:dni": {
          required: false,
        },
        "custom:area": {
          required: false,
        },
        "custom:position": {
          required: false,
        },
        "custom:is_representative": {
          required: false,
        },
      },
      passwordFormat: {
        minLength: 8,
        requireLowercase: true,
        requireUppercase: true,
        requireNumbers: true,
        requireSpecialCharacters: true,
      },
    },
  },
};
