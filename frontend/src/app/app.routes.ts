import {Routes} from '@angular/router';
import {LogInComponent} from './pages/log-in/log-in.component';
import {RegisterComponent} from './pages/register/register.component';
import {HomeComponent} from './pages/home/home.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'login', component: LogInComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent }
];
