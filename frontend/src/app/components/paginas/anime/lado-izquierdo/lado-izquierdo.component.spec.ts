import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LadoIzquierdoComponent } from './lado-izquierdo.component';

describe('LadoIzquierdoComponent', () => {
  let component: LadoIzquierdoComponent;
  let fixture: ComponentFixture<LadoIzquierdoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LadoIzquierdoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LadoIzquierdoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
