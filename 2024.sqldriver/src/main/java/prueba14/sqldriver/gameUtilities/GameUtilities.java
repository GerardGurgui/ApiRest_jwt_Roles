package prueba14.sqldriver.gameUtilities;

import prueba14.sqldriver.entities.Dice;
import prueba14.sqldriver.entities.Player;

public class GameUtilities {

    public static Dice LaunchDice(){

        Dice dice = new Dice();
        dice.setDado1(numRandom());

        dice.setDado2(numRandom());

        int resultado = dice.SumarDados();

        dice.setResultadoTirada(resultado);

        return dice;
    }

    public static int numRandom(){

        int numero = (int) ((Math.random() * ((6 - 1) + 1)) + 1);

        return numero;
    }

    public static boolean checkWinRound(int resultado){

        boolean rondaGanada = false;

        if (resultado == 7){

            rondaGanada = true;

        }

        return rondaGanada;
    }

    public static boolean checkWinGame(int puntuacion){

        boolean ganadorPartida = false;


        if (puntuacion == 3){

            ganadorPartida = true;
        }

        return ganadorPartida;
    }
    /////PORCENTAJE-ESTADISTICAS
    //(TOTAL TIRADAS * LAS QUE HA GANADO / JUGADORES)*100

    public static int checkAveragePlayer(Player player){

        //puntuacion (cuantos 7 tiene)
        //el total de tiradas que ha realizado

        int totalPuntuacion = player.getPuntuacion();

        int totalTiradas = player.getThrowsDices().size();

        return (totalPuntuacion * 100) / totalTiradas;

    }

//    public static int mostrarJugadorConMasPuntuacion(List<Player> players){
//
//        int puntuacion = 0;
//
//
//        for (Player player : players) {
//
//
//
//        }
//
//    }

}
