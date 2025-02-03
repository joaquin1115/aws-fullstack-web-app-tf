import { Injectable } from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import { FetchUserAttributesOutput } from 'aws-amplify/auth';

interface UserAttributes {
  employeeId?: string;
  dni?: string;
  area?: string;
  position?: string;
  isRepresentative?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<UserAttributes | null>(null);
  currentUser$: Observable<UserAttributes | null> = this.currentUserSubject.asObservable();

  constructor() { }

  async initializeUser(customAttributes: FetchUserAttributesOutput): Promise<void> {
    const userAttributes: UserAttributes = {
      employeeId: customAttributes['custom:employee_id'],
      dni: customAttributes['custom:dni'],
      area: customAttributes['custom:area'],
      position: customAttributes['custom:position'],
      isRepresentative: customAttributes['custom:is_representative'] === 'true'
    };

    this.currentUserSubject.next(userAttributes);
  }

  isLogged(): boolean {
    return !!this.currentUserSubject.getValue();
  }

  getUserRole(): string | undefined {
    return this.currentUserSubject.getValue()?.position;
  }

  getUserDni(): string | undefined {
    return this.currentUserSubject.getValue()?.dni;
  }

  getUserId(): string | undefined {
    return this.currentUserSubject.getValue()?.employeeId;
  }

  getUserArea(): string | undefined {
    return this.currentUserSubject.getValue()?.area;
  }

  isUserRepresentative(): boolean | undefined {
    return this.currentUserSubject.getValue()?.isRepresentative;
  }

  getUserAttributes(): UserAttributes | null {
    return this.currentUserSubject.getValue();
  }
}
