import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate, Router,
  RouterStateSnapshot, UrlTree
} from '@angular/router';
import {LocalStorageService} from '../services/local-storage.service';
import {HttpClient} from '@angular/common/http';
import {ApiResponse} from '../common/interfaces/api.response';
import {environment} from '../../environments/environment';
import {catchError, map, Observable, of} from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  private api: string = environment.apiGateway

  constructor(
    private readonly httpClient: HttpClient,
    private readonly localStorageService: LocalStorageService,
    private readonly router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    const page = route.data['page'];
    const url: string = `${this.api}/pages/${page}`;

    const tokenExists = this.localStorageService.isUserLoggedIn()
    if (page === 'login' || page === 'register') {
      if (tokenExists) {
        this.localStorageService.removeUserAuth();
        return of(this.router.parseUrl(''));
      } else {
        return of(true);
      }
    }

    return this.httpClient.get<ApiResponse>(url).pipe(
      map(response => response.code === 'SUCCESS'),
      catchError(err => {
        return of(this.router.parseUrl(''));
      })
    );
  }
}
