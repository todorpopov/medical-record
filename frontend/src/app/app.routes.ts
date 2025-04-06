import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LogInComponent } from './pages/log-in/log-in.component';
import { RegisterComponent } from './pages/register/register.component';

export const routes: Routes = [
  { path: '', component: AppComponent},
  { path: 'login', component: LogInComponent },
  { path: 'register', component: RegisterComponent }
];
