import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user';
import { Observable } from 'rxjs';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { GoldPrice } from '../models/gold-price';

@Injectable({
  providedIn: 'root'
})
export class AppService implements CanActivate{

  baseUrl = 'http://localhost:8085/api/gold-price/';
  constructor(private http: HttpClient, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean{
    if(sessionStorage.getItem('username') != null) return true;
    this.router.navigateByUrl('home/login');
    return false;
  }

  login(username: string, password: string){
    var body = { username: username, password: password };
    var header = { headers: new HttpHeaders({'Content-Type':  'application/json'}) };
    var response$ = this.http.post( this.baseUrl + 'login', body, header );
    response$.subscribe( (res: User) => {
      sessionStorage.setItem('username', res.username);
      sessionStorage.setItem('usertype', res.userType);
     } );
    return response$;
  }

  getDiscount(){
    return this.http.get( this.baseUrl + 'discount' );
  }

  getUserType(){
    return sessionStorage.getItem('usertype');
  }

  download(goldPrice: GoldPrice){
    var header = new HttpHeaders({'Content-Type':  'application/json'});
    return this.http.post( this.baseUrl + 'download', goldPrice, {headers : header, responseType : 'blob', observe: 'response'});
  }

  logout(){
    sessionStorage.clear();
    this.router.navigateByUrl('home/login');
  }
}
