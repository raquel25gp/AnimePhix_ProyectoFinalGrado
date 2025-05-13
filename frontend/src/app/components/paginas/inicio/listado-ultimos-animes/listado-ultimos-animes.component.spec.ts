import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoUltimosAnimesComponent } from './listado-ultimos-animes.component';

describe('ListadoUltimosAnimesComponent', () => {
  let component: ListadoUltimosAnimesComponent;
  let fixture: ComponentFixture<ListadoUltimosAnimesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListadoUltimosAnimesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListadoUltimosAnimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
