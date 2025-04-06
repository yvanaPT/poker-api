import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PokerGameComponent } from './pokerGame/poker-game/poker-game.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, PokerGameComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'poker-game';
}
