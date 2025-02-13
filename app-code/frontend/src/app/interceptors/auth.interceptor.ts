import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { fetchAuthSession } from '@aws-amplify/auth';
import { from } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    return from(fetchAuthSession()).pipe(
        switchMap(session => {
            const token = session.tokens?.idToken?.toString(); // Get ID Token
            const authReq = token
                ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
                : req;
            return next(authReq);
        }),
        catchError(error => {
            console.error('Error fetching auth token:', error);
            return next(req); // Proceed without modifying the request if there's an error
        })
    );
};
