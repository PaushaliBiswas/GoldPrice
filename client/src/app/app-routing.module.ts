import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { CalculatorComponent } from './components/calculator/calculator.component';
import { LoginComponent } from './components/login/login.component';
import { AppService } from './services/app.service';


const routes: Routes = [
  { path : '', redirectTo : 'home/login', pathMatch : 'full' },
  { path : 'home/login', component : LoginComponent },
  { path : 'home/calculator', component : CalculatorComponent, canActivate: [AppService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
