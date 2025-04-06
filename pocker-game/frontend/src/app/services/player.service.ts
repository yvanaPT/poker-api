import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  constructor() { }

  private apiUrl = 'http://localhost:8080';
  private http = inject(HttpClient);


  getPlayerRanking(pokerGameId: string): Observable<any> {
    var pokerGameUrl = this.apiUrl + "/get-player-ranking";

    return this.http.get<any>(pokerGameUrl, {
      params: { pokerGameId: pokerGameId }
    });
  }

  addPlayer(pokerGameId: string, playerName: string): Observable<any> {
    var pokerGameUrl = this.apiUrl + "/add-player/" + pokerGameId;
    const body = {
      username: playerName
    };
    return this.http.post<any>(pokerGameUrl, body);
  }

  updatePlayerScore(pokerGameId: string, playerId: string, score: number): Observable<any> {
    var pokerGameUrl = this.apiUrl + "/update-player/poker-game/player/score";
    const parameters = {
      pokerGameId: pokerGameId,
      playerId: playerId,
    };
    const body = score;
    
    return this.http.patch<string>(pokerGameUrl, body, {
      headers: { 'Content-Type': 'application/json' },
      params: parameters
    });
  }
}
