import { Component } from '@angular/core';
import {DashboardComponent} from '../dashboard/dashboard.component';

@Component({
  selector: 'app-layout',
  imports: [
    DashboardComponent
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

}
