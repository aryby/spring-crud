import { Component, inject, OnInit } from '@angular/core';
import {ActivatedRoute, NavigationEnd, NavigationStart, Router, RouterOutlet} from '@angular/router';
import {HeaderComponent} from "./common/header/header.component";
import {SideBareComponent} from "./common/layouts/side-bare";
import {Title} from "@angular/platform-browser";
import {filter,map,switchMap,tap} from "rxjs";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [
    HeaderComponent,
    RouterOutlet,
    SideBareComponent,

  ],
providers: [
  FormsModule,
  ReactiveFormsModule,
],
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  router = inject(Router);

  msgSuccess = null;
  msgInfo = null;
  msgError = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private titleService: Title,
  ) {
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => this.activatedRoute),
        map((route) => {
          while (route.firstChild) route = route.firstChild;
          return route;
        }),
        filter((route) => route.outlet === 'primary'),
        switchMap((route) => {
          return route.data.pipe(
            map((routeData: any) => {
              const title = routeData['title'];
              return { title };
            }),
          );
        }),
        tap((data: any) => {
          let title = data.title;
          title = (title ? title + ' | ' : '') + 'GoToANGENCY - Multipurpose Tailwind Dashboard Template';
          this.titleService.setTitle(title);
        }),
      )
      .subscribe();
  }

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        const navigationState = this.router.getCurrentNavigation()?.extras.state;
        this.msgSuccess = navigationState?.['msgSuccess'] || null;
        this.msgInfo = navigationState?.['msgInfo'] || null;
        this.msgError = navigationState?.['msgError'] || null;
      }
    });
  }

}
