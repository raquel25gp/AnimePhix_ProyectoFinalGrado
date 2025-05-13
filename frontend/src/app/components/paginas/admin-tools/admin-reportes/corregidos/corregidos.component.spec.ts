import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CorregidosComponent } from './corregidos.component';

describe('CorregidosComponent', () => {
  let component: CorregidosComponent;
  let fixture: ComponentFixture<CorregidosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorregidosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CorregidosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
