/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BrhTestModule } from '../../../test.module';
import { DiscountProcessDeleteDialogComponent } from 'app/entities/discount-process/discount-process-delete-dialog.component';
import { DiscountProcessService } from 'app/entities/discount-process/discount-process.service';

describe('Component Tests', () => {
    describe('DiscountProcess Management Delete Component', () => {
        let comp: DiscountProcessDeleteDialogComponent;
        let fixture: ComponentFixture<DiscountProcessDeleteDialogComponent>;
        let service: DiscountProcessService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BrhTestModule],
                declarations: [DiscountProcessDeleteDialogComponent]
            })
                .overrideTemplate(DiscountProcessDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DiscountProcessDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DiscountProcessService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
