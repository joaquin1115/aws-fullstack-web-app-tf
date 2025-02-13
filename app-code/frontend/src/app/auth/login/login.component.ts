import {Component, OnInit} from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import {AmplifyAuthenticatorModule, AuthenticatorService} from '@aws-amplify/ui-angular';
import { Amplify } from "aws-amplify";
import { I18n } from 'aws-amplify/utils';
import { fetchUserAttributes } from 'aws-amplify/auth';
import { translations } from '@aws-amplify/ui-angular';
import { Hub } from 'aws-amplify/utils';
import { environment } from '../../../environments/environment';
I18n.putVocabularies(translations);
I18n.setLanguage('es');

I18n.putVocabularies({
  es: {
    'Sign In': 'Iniciar sesión',
    'Enter your Username': 'Ingrese su nombre de usuario'
  },
});

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    AmplifyAuthenticatorModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})

export class LoginComponent implements OnInit{

  constructor(
    public authenticator: AuthenticatorService,
    private authService: AuthService,
    private router: Router,
  ) {
    Amplify.configure({
      Auth: {
        Cognito: {
          userPoolId: environment.cognito.userPoolId,
          userPoolClientId: environment.cognito.userPoolClientId,
          loginWith: {
            email: true,
          },
          signUpVerificationMethod: "code",
          userAttributes: {
            email: {
              required: true,
            }
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
    });
  }

  ngOnInit() {
    Hub.listen('auth', async ({ payload }) => {
      if (payload.event == 'signedIn') {
        const customAttributes = await fetchUserAttributes();
        await this.authService.initializeUser(customAttributes);
        this.router.navigate(['pages/home']);
      }
    });
  }
}
