import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearAnimeComponent } from './crear-anime.component';

describe('CrearAnimeComponent', () => {
  let component: CrearAnimeComponent;
  let fixture: ComponentFixture<CrearAnimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearAnimeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearAnimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
