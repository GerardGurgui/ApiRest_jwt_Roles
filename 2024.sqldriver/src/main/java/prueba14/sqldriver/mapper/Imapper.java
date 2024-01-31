package prueba14.sqldriver.mapper;

/*
 * Interfaz generica para mapear las entidades a DTO y viceversa
 * Input Output
 * */

import prueba14.sqldriver.entities.Player;

public interface Imapper<I, O> {

    O map(I input);

    O mapUpdate(I input, Player player);

}
