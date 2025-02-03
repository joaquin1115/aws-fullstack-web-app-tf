import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamosVisorComponent } from './reclamos-visor.component';

describe('ReclamosVisorComponent', () => {
  let component: ReclamosVisorComponent;
  let fixture: ComponentFixture<ReclamosVisorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReclamosVisorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReclamosVisorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
