import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminComentariosComponent } from './admin-comentarios.component';

describe('AdminComentariosComponent', () => {
  let component: AdminComentariosComponent;
  let fixture: ComponentFixture<AdminComentariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminComentariosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminComentariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
