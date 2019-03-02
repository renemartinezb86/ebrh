import { Moment } from 'moment';

export interface IDiscountProcess {
    id?: number;
    csvFilePath?: string;
    createdDate?: Moment;
    sqlFilePath?: string;
}

export class DiscountProcess implements IDiscountProcess {
    constructor(public id?: number, public csvFilePath?: string, public createdDate?: Moment, public sqlFilePath?: string) {}
}
