import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LogInComponent } from './pages/log-in/log-in.component';

export const routes: Routes = [
  { path: '', component: AppComponent},
  { path: 'login', component: LogInComponent }
];
