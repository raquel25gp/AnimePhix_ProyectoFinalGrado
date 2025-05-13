import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAnimesComponent } from './admin-animes.component';

describe('AdminAnimesComponent', () => {
  let component: AdminAnimesComponent;
  let fixture: ComponentFixture<AdminAnimesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminAnimesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminAnimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
