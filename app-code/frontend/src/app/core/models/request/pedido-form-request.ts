import { ClienteRequest } from "./cliente-request";
import { PersonaRequest } from "./persona-request";
import { RepresentanteRequest } from "./representante-request";

export interface PedidoFormRequest {
  personaRequest?: PersonaRequest;
  clienteRequest?: ClienteRequest;
  representanteRequest?: RepresentanteRequest;
}