import {Component, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-side-bare',
  templateUrl: './side-bare.html',
  imports: [
    RouterLink
  ]
})
export class SideBareComponent implements OnInit {
    ngOnInit(): void {
      console.log('ngOnInit');
    }

}

