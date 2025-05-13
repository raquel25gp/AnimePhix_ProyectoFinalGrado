import { TestBed } from '@angular/core/testing';

import { VistoService } from './visto.service';

describe('VistoService', () => {
  let service: VistoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VistoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
