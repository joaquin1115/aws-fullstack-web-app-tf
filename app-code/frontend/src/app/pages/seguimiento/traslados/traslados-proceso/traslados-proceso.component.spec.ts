import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrasladosProcesoComponent } from './traslados-proceso.component';

describe('TrasladosProcesoComponent', () => {
  let component: TrasladosProcesoComponent;
  let fixture: ComponentFixture<TrasladosProcesoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrasladosProcesoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TrasladosProcesoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
