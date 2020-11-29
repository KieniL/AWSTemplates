export * from './createschema.service';
import { CreateschemaService } from './createschema.service';
export * from './getSchemas.service';
import { GetSchemasService } from './getSchemas.service';
export * from './tablemanage.service';
import { TablemanageService } from './tablemanage.service';
export * from './tablescreatedefault.service';
import { TablescreatedefaultService } from './tablescreatedefault.service';
export const APIS = [CreateschemaService, GetSchemasService, TablemanageService, TablescreatedefaultService];
