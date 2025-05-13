import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SobreAnimePhixComponent } from './sobre-anime-phix.component';

describe('SobreAnimePhixComponent', () => {
  let component: SobreAnimePhixComponent;
  let fixture: ComponentFixture<SobreAnimePhixComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SobreAnimePhixComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SobreAnimePhixComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
