import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PokerGameService {

  constructor() { }

  private apiUrl = 'http://localhost:8080'; 
  private http = inject(HttpClient);

  getPokerGame(): Observable<any> {
    var pokerGameUrl = this.apiUrl+"/get-poker-game";
    
    return this.http.get<any>(pokerGameUrl);
  }

  createPokerGame(name : string) : Observable<any> {
    var pokerGameUrl = this.apiUrl+"/create-poker-game";
    const body = {
      name: name
    };
    return this.http.post(pokerGameUrl, body);
  }
}
