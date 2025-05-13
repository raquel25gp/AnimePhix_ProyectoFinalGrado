import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuentasAdminsComponent } from './cuentas-admins.component';

describe('CuentasAdminsComponent', () => {
  let component: CuentasAdminsComponent;
  let fixture: ComponentFixture<CuentasAdminsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CuentasAdminsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CuentasAdminsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
