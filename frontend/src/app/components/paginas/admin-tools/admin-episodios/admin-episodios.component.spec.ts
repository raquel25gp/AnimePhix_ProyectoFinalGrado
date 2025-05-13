import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEpisodiosComponent } from './admin-episodios.component';

describe('AdminEpisodiosComponent', () => {
  let component: AdminEpisodiosComponent;
  let fixture: ComponentFixture<AdminEpisodiosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminEpisodiosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEpisodiosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
