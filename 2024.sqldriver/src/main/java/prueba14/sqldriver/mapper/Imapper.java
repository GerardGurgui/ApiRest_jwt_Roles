package prueba14.sqldriver.mapper;

/*
 * Interfaz generica para mapear las entidades a DTO y viceversa
 * Input Output
 * */

public interface Imapper<I, O> {

    O map(I input);

}
