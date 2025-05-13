import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoUltimosEpisodiosComponent } from './listado-ultimos-episodios.component';

describe('ListadoUltimosEpisodiosComponent', () => {
  let component: ListadoUltimosEpisodiosComponent;
  let fixture: ComponentFixture<ListadoUltimosEpisodiosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListadoUltimosEpisodiosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListadoUltimosEpisodiosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
