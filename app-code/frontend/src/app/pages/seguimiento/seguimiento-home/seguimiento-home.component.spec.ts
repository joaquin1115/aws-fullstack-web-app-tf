import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeguimientoHomeComponent } from './seguimiento-home.component';

describe('SeguimientoHomeComponent', () => {
  let component: SeguimientoHomeComponent;
  let fixture: ComponentFixture<SeguimientoHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeguimientoHomeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SeguimientoHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
