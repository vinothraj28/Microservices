import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { AuthService } from '../core/services/auth/auth.service';

@Component({
  selector: 'app-authorize',
  imports: [],
  templateUrl: './authorize.component.html',
  styleUrls: ['./authorize.component.css']
})
export class AuthorizeComponent implements OnInit {

  constructor(private readonly authService: AuthService) { }

  async ngOnInit(): Promise<void> {
    await this.authService.authorize();
  }
}
