import { Component } from '@angular/core';
import { inject, signal } from '@angular/core';
import { PlayerService } from '../../services/player.service';
import { CommonModule } from '@angular/common';
import { PokerGameService } from '../../services/poker-game.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-poker-game',
  imports: [CommonModule, FormsModule],
  templateUrl: './poker-game.component.html',
  styleUrl: './poker-game.component.css'
})
export class PokerGameComponent {

  title = "Poker"

  private playerService = inject(PlayerService);
  private pokerGameService = inject(PokerGameService)

  pokerGameList = signal<any[]>([]);
  playerRanking = signal<any[]>([]);
  selectedGameId: string | null = null;
  pokerGameResponseMessage: string | null = null;
  playerResponseMessage: string | null = null;
  playerScoreResponseMessage: string | null = null;

  newGame = {
    name: ''
  };
  newPlayer = {
    pokerGameId: '',
    name: ''
  };
  newScore = {
    pokerGameId: '',
    playerId: '',
    score : 0
  }

  createPokerGame() {
    this.pokerGameService.createPokerGame(this.newGame.name).subscribe({
      next: () => {
        this.newGame.name = '';
        this.fetchPokerGames();
        this.pokerGameResponseMessage = 'Partie créée avec succès '
      },
      error: () => {
        this.pokerGameResponseMessage = 'Oups, impossible de créer cette partie';
      }
    })
    setTimeout(() => {
      this.pokerGameResponseMessage = null;
    }, 3000);
  }

  fetchPokerGames() {
    this.pokerGameService.getPokerGame().subscribe(data => {
      this.pokerGameList.set(data);
    });
  }

  fetchPlayerRanking(pokerGameId: string) {
    this.selectedGameId = pokerGameId;
    this.newScore.pokerGameId = pokerGameId;
    this.playerService.getPlayerRanking(pokerGameId).subscribe({
      next: (data) => {
        this.playerRanking.set(data);
      },
      error: () => {
        this.playerRanking.set([]);
      }
    });
  }

  addNewPlayer() {
    this.playerService.addPlayer(this.newPlayer.pokerGameId, this.newPlayer.name)
      .subscribe({
        next: () => {
          this.fetchPlayerRanking(this.newPlayer.pokerGameId)
          this.newPlayer.pokerGameId = ''
          this.newPlayer.name = ''
          this.playerResponseMessage = 'Joueur ajouté avec succès '
        },
        error: () => {
          this.playerResponseMessage = 'Oups, impossible d\'ajouter ce joueur à la partie';
        }
      });
      setTimeout(() => {
        this.playerResponseMessage = null;
      }, 3000);
  }

  UpdatePlayerScore() {
    this.playerService.updatePlayerScore(this.newScore.pokerGameId, this.newScore.playerId, this.newScore.score)
      .subscribe({
        next: () => {
          this.fetchPlayerRanking(this.newScore.pokerGameId)
          this.newScore.playerId = ''
          this.newScore.score = 0
          this.newScore.pokerGameId = ''
          this.playerScoreResponseMessage = 'Score modifié avec succès';
        },
        error: (err) => {
          console.log(err)
          this.fetchPlayerRanking(this.newScore.pokerGameId)
          this.playerScoreResponseMessage = 'Oups, impossible de modifier le score de ce joueur';
        }
      });
      setTimeout(() => {
        this.playerScoreResponseMessage = null;
      }, 3000);
  }
}


