export type Categoria = 'DOCE' | 'SALGADO' | 'BEBIDA' | 'SOBREMESA';

export interface Recipe {
  id?: number;
  nome: string;
  categoria: Categoria;
  tempoPreparo: number;
  porcoes: number;
  ingredientes: string[];
  modoPreparo: string;
  dataCadastro?: string;
}

export interface ApiErrorResponse {
  timestamp: string;
  status: number;
  message: string;
  errors?: Record<string, string>;
}
